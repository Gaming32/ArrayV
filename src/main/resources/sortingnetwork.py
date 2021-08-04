#!/usr/bin/env python3

# The MIT License (MIT)
# 
# Copyright (c) 2015 Brian Pursley
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

import argparse
import sys


class Comparator:
	def __init__(self, s):
		inputs = s.strip().split(":")
		input1 = int(inputs[0])
		input2 = int(inputs[1])
		if input1 < input2:
			self.i1 = input1
			self.i2 = input2
		else:
			self.i1 = input2
			self.i2 = input1

	def __str__(self):
		return "%s:%s" % (self.i1, self.i2)

	def __hash__(self):
		return ("%s:%s" % (self.i1, self.i2)).__hash__()

	def overlaps(self, other):
		return (self.i1 < other.i1 < self.i2) or \
				(self.i1 < other.i2 < self.i2) or \
				(other.i1 < self.i1 < other.i2) or \
				(other.i1 < self.i2 < other.i2)

	def has_same_input(self, other):
		return self.i1 == other.i1 or \
				self.i1 == other.i2 or \
				self.i2 == other.i1 or \
				self.i2 == other.i2


class ComparisonNetwork(list):
	def __str__(self):
		result = ""
		group = []
		for c in self:
			for other in group:
				if c.has_same_input(other):
					result += ','.join(map(str, group)) + "\n"
					del group[:]
					break
			group.append(c)
		result += ','.join(map(str, group))
		return result

	def is_sorting_network(self, show_progress):
		# Use the zero-one principle to determine if this comparison network is a sorting network
		number_of_inputs = self.get_max_input() + 1
		max_sequence_to_check = (1 << number_of_inputs) - 1
		for i in range(0, max_sequence_to_check):
			ones_count = count_ones(i)
			if ones_count > 0:
				zeros_count = number_of_inputs - ones_count
				expected_sorted_sequence = ((1 << ones_count) - 1) << zeros_count
			else:
				expected_sorted_sequence = 0
			if self.sort_binary_sequence(i) != expected_sorted_sequence:
				return False
			if show_progress and i % 100 == 0:
				print("\rChecking... %s%%" % round(i * 100 / max_sequence_to_check), end="")
		if show_progress:
			print("\r", end="")
		return True

	def sort_binary_sequence(self, sequence):
		result = sequence
		# Apply all comparators to the binary sequence
		for c in self:
			# Compare the two bits at the comparator's input positions and swap if needed
			pos0 = (result >> c.i1) & 1
			pos1 = (result >> c.i2) & 1
			if pos0 > pos1:
				result ^= (1 << c.i1) | (1 << c.i2)
		return result

	def sort_sequence(self, sequence):
		result = list(sequence)
		for c in self:
			if result[c.i1] > result[c.i2]:
				result[c.i1], result[c.i2] = result[c.i2], result[c.i1]
		return result

	def get_max_input(self):
		max_input = 0
		for c in self:
			if c.i2 > max_input:
				max_input = c.i2
		return max_input

	def svg(self):
		scale = 1
		x_scale = scale * 35
		y_scale = scale * 20

		comparators_svg = ""
		w = x_scale
		group = {}
		for c in self:

			# If the comparator inputs are the same position as any other comparator in the group, then start a new group
			for other in group:
				if c.has_same_input(other):
					for _, pos in group.items():
						if pos > w:
							w = pos
					w += x_scale
					group = {}
					break

			# Adjust the comparator x position to avoid overlapping any existing comparators in the group
			cx = w
			for other, other_pos in group.items():
				if other_pos >= cx and c.overlaps(other):
					cx = other_pos + x_scale / 3

			# Generate two circles and a line representing the comparator
			y0 = y_scale + c.i1 * y_scale
			y1 = y_scale + c.i2 * y_scale
			comparators_svg += \
				"<circle cx='%s' cy='%s' r='%s' style='stroke:black;stroke-width:1;fill=yellow' />" % (cx, y0, 3) + \
				"<line x1='%s' y1='%s' x2='%s' y2='%s' style='stroke:black;stroke-width:%s' />" % (cx, y0, cx, y1, 1) + \
				"<circle cx='%s' cy='%s' r='%s' style='stroke:black;stroke-width:1;fill=yellow' />" % (cx, y1, 3)
			# Add this comparator to the current group
			group[c] = cx

		# Generate line SVG elements
		lines_svg = ""
		w += x_scale
		n = self.get_max_input() + 1
		for i in range(0, n):
			y = y_scale + i * y_scale
			lines_svg += "<line x1='%s' y1='%s' x2='%s' y2='%s' style='stroke:black;stroke-width:%s' />" % (
				0, y, w, y, 1)

		h = (n + 1) * y_scale
		return \
			"<?xml version='1.0' encoding='utf-8'?>" + \
			"<!DOCTYPE svg>" + \
			"<svg width='%spx' height='%spx' xmlns='http://www.w3.org/2000/svg'>" % (w, h) + \
			comparators_svg + \
			lines_svg + \
			"</svg>"


def count_ones(x):
	result = 0
	while x > 0:
		result += x & 1
		x = x >> 1
	return result


def read_comparison_network(filename):
	cn = ComparisonNetwork()
	if filename:
		with open(filename, 'r') as f:
			for line in f:
				for c in line.split(","):
					cn.append(Comparator(c))
	else:
		for line in sys.stdin:
			for c in line.split(","):
				cn.append(Comparator(c))
	return cn


def main():
	parser = argparse.ArgumentParser()
	parser.add_argument("-i", "--input", metavar="inputfile",
						help="specify a file containing comparison network definition")
	parser.add_argument("-o", "--output", metavar="outputfile", nargs='?', const='',
						help="specify a file for saving the comparison network definition")
	parser.add_argument("-c", "--check", action="store_true", help="check whether it is a sorting network")
	parser.add_argument("--show-progress", action="store_true", help="show percent complete while checking whether it is a sorting network")
	parser.add_argument("-s", "--sort", metavar="list", nargs='?', const='',
						help="sorts the list using the input comparison network")
	parser.add_argument("--svg", metavar="outputfile", nargs='?', const='', help="generate SVG")
	args = parser.parse_args()

	if args.check:
		cn = read_comparison_network(args.input)
		if cn.is_sorting_network(args.show_progress):
			print("It is a sorting network!")
		else:
			print("It is not a sorting network.")

	if args.svg or args.svg == "":
		cn = read_comparison_network(args.input)
		if args.svg == "":
			print(cn.svg())
		else:
			with open(args.svg, "w") as f:
				f.write(cn.svg())

	if args.output or args.output == "":
		cn = read_comparison_network(args.input)
		if args.output == "":
			print(str(cn))
		else:
			with open(args.output, "w") as f:
				f.write(str(cn))

	if args.sort or args.sort == "":
		cn = read_comparison_network(args.input)
		if args.sort == "":
			input_sequence = eval(sys.stdin.readline())
		else:
			input_sequence = eval(args.sort)
		for sorted_item in cn.sort_sequence(input_sequence):
			print(sorted_item)


if __name__ == "__main__":
	main()

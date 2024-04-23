package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */
@SortMeta(name = "Stackless Binary Quick")
public final class StacklessBinaryQuickSort extends Sort {
	public StacklessBinaryQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int stabVal(int idx) {
		if (arrayVisualizer.doingStabilityCheck())
			return arrayVisualizer.getStabilityValue(idx);
		else
			return idx;
	}

	private int partition(int[] array, int a, int b, int bit) {
		int i = a - 1, j = b;

		while (true) {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			} while (i < j && !Reads.getBit(array[i], bit));

			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			} while (j > i && Reads.getBit(array[j], bit));

			if (i < j)
				Writes.swap(array, i, j, 1, true, false);
			else
				return i;
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int q = Reads.analyzeBit(array, length), m = 0,
				i = 0, b = length;

		while (i < length) {
			int p = b - i < 1 ? i : this.partition(array, i, b, q);

			if (q == 0) {
				m += 2;
				while (!Reads.getBit(m, q + 1))
					q++;

				i = b;
				Highlights.clearMark(2);
				arrayVisualizer.toggleAnalysis(true);
				while (b < length && (this.stabVal(array[b]) >> (q + 1)) == (m >> (q + 1))) {
					Highlights.markArray(1, b);
					Delays.sleep(0.5);
					b++;
				}
				arrayVisualizer.toggleAnalysis(false);
			} else {
				b = p;
				q--;
			}
		}
	}
}

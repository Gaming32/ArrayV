package io.github.arrayv.sorts.insert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
  Copyright (c) rosettacode.org.
  Permission is granted to copy, distribute and/or modify this document
  under the terms of the GNU Free Documentation License, Version 1.2
  or any later version published by the Free Software Foundation;
  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
  Texts.  A copy of the license is included in the section entitled "GNU
  Free Documentation License".
 *
 */

public final class PatienceSort extends Sort {
	public PatienceSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Patience");
		this.setRunAllSortsName("Patience Sort");
		this.setRunSortName("Patience Sort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	final private class Pile extends Stack<Integer> implements Comparable<Pile> {
		private static final long serialVersionUID = 1L;

		public int compare(Pile y) {
			return Reads.compareValues(peek(), y.peek());
		}

		@Override
		public int compareTo(Pile y) {
			return Reads.compareValues(peek(), y.peek());
		}
	}

	private void binarySearch(ArrayList<Pile> list, Pile find) {
		int at = list.size() / 2;
		int change = list.size() / 4;

		long compsBefore = Reads.getComparisons();
		while(list.get(at).compare(find) != 0 && change > 0){
			Reads.setComparisons(compsBefore);
			Highlights.markArray(1, at);
			Delays.sleep(0.5);

			if(list.get(at).compare(find) < 0)
				at += change;
			else
				at -= change;

			change /= 2;
		}
		Reads.setComparisons(compsBefore);

		Highlights.markArray(1, at);
		Delays.sleep(0.5);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		ArrayList<Pile> piles = new ArrayList<>();

		// sort into piles
		for (int x = 0; x < length; x++) {
			Pile newPile = new Pile();

			Highlights.markArray(2, x);
			Writes.mockWrite(length, Math.min(newPile.size(), length - 1), array[x], 1);

			newPile.push(array[x]);
			Writes.changeAllocAmount(1);

			int i = Collections.binarySearch(piles, newPile);
			if(!piles.isEmpty()) {
				this.binarySearch(piles, newPile);
			}
			if (i < 0) i = ~i;
			if (i != piles.size()) {
				Writes.mockWrite(length, Math.min(piles.get(i).size(), length - 1), array[x], 0);
				piles.get(i).push(array[x]);
				Writes.changeAllocAmount(1);
			}
			else {
				Writes.mockWrite(length, Math.min(piles.size(), length - 1), newPile.get(0), 0);
				piles.add(newPile);
				Writes.changeAllocAmount(1);
			}
		}

		Highlights.clearMark(2);

		// priority queue allows us to retrieve least pile efficiently
		PriorityQueue<Pile> heap = new PriorityQueue<>(piles);

		for (int c = 0; c < length; c++) {
			Writes.mockWrite(length, Math.min(heap.size(), length - 1), 0, 0);
			Pile smallPile = heap.poll();

			Writes.mockWrite(length, Math.min(smallPile.size(), length - 1), 0, 0);
			Writes.write(array, c, smallPile.pop(), 1, true, false);
			Writes.changeAllocAmount(-1);

			if (!smallPile.isEmpty()) {
				Writes.mockWrite(length, Math.min(heap.size(), length - 1), smallPile.get(0), 0);
				heap.offer(smallPile);
				Writes.changeAllocAmount(-1);
			}
		}

		Writes.clearAllocAmount();
	}
}

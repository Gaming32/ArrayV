package io.github.arrayv.sorts.exchange;

import java.util.Random;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
@SortMeta(listName = "Stable Quick (Parallel)", runName = "Parallel Stable Quick Sort", unreasonableLimit = 4096)
public final class StableQuickSortParallel extends Sort {
	public StableQuickSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int[] array;
	private int[] tmp;

	private class QuickSortInt extends Thread {
		private int a, b;

		QuickSortInt(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			StableQuickSortParallel.this.quickSortInt(a, b);
		}
	}

	private class QuickSortExt extends Thread {
		private int a, b;

		QuickSortExt(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			StableQuickSortParallel.this.quickSortExt(a, b);
		}
	}

	private int partitionInt(int a, int b) {
		Random r = new Random();
		int p = a + r.nextInt(b - a);

		int piv = array[p];
		int j = a, k = b - 1;

		while (j < p && Reads.compareValues(array[j], piv) <= 0)
			j++;
		if (j < p)
			Writes.write(tmp, k--, array[j], 1, false, true);

		for (int i = j + 1; i < p; i++) {
			if (Reads.compareValues(array[i], piv) <= 0)
				Writes.write(array, j++, array[i], 1, true, false);

			else {
				Highlights.markArray(2, k);
				Writes.write(tmp, k--, array[i], 1, false, true);
			}
		}
		for (int i = p + 1; i < b; i++) {
			if (Reads.compareValues(array[i], piv) < 0)
				Writes.write(array, j++, array[i], 1, true, false);

			else {
				Highlights.markArray(2, k);
				Writes.write(tmp, k--, array[i], 1, false, true);
			}
		}
		Writes.write(array, j, piv, 1, true, false);

		return j;
	}

	private int partitionExt(int a, int b) {
		Random r = new Random();
		int p = a + r.nextInt(b - a);

		int piv = tmp[p];
		int j = b - 1, k = a;

		while (j > p && Reads.compareValues(tmp[j], piv) > 0)
			j--;
		if (j > p)
			Writes.write(array, k++, tmp[j], 1, true, false);

		for (int i = j - 1; i > p; i--) {
			if (Reads.compareValues(tmp[i], piv) > 0) {
				Highlights.markArray(2, j);
				Writes.write(tmp, j--, tmp[i], 1, false, true);
			} else
				Writes.write(array, k++, tmp[i], 1, true, false);
		}
		for (int i = p - 1; i >= a; i--) {
			if (Reads.compareValues(tmp[i], piv) >= 0) {
				Highlights.markArray(2, j);
				Writes.write(tmp, j--, tmp[i], 1, false, true);
			} else
				Writes.write(array, k++, tmp[i], 1, true, false);
		}
		Writes.write(array, k, piv, 1, true, false);

		return k;
	}

	private void quickSortInt(int a, int b) {
		int len = b - a;

		if (len < 2)
			return;

		int p = this.partitionInt(a, b);

		QuickSortInt left = new QuickSortInt(a, p);
		QuickSortExt right = new QuickSortExt(p + 1, b);
		left.start();
		right.start();

		try {
			left.join();
			right.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void quickSortExt(int a, int b) {
		int len = b - a;

		if (len < 2) {
			if (len == 1)
				Writes.write(array, a, tmp[a], 1, true, false);
			return;
		}

		int p = this.partitionExt(a, b);

		QuickSortInt left = new QuickSortInt(a, p);
		QuickSortExt right = new QuickSortExt(p + 1, b);
		left.start();
		right.start();

		try {
			left.join();
			right.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
		this.tmp = Writes.createExternalArray(length);
		this.quickSortInt(0, length);
		Writes.deleteExternalArray(tmp);
	}
}

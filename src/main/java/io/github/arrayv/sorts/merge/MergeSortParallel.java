package io.github.arrayv.sorts.merge;

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
@SortMeta(name = "Parallel Merge", unreasonableLimit = 4096)
public final class MergeSortParallel extends Sort {
	public MergeSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int[] array;
	private int[] tmp;

	private class MergeSort extends Thread {
		private int a, b;

		MergeSort(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			MergeSortParallel.this.mergeSort(a, b);
		}
	}

	private void merge(int a, int m, int b) {
		int i = a, j = m, k = a;

		while (i < m && j < b) {
			if (Reads.compareValues(array[i], array[j]) <= 0) {
				Highlights.markArray(1, i);
				Writes.write(tmp, k++, array[i++], 1, false, true);
			} else {
				Highlights.markArray(2, j);
				Writes.write(tmp, k++, array[j++], 1, false, true);
			}
		}
		while (i < m) {
			Highlights.markArray(1, i);
			Writes.write(tmp, k++, array[i++], 1, false, true);
		}
		while (j < b) {
			Highlights.markArray(2, j);
			Writes.write(tmp, k++, array[j++], 1, false, true);
		}

		Highlights.clearMark(2);
		while (a < b)
			Writes.write(array, a, tmp[a++], 1, true, false);
	}

	private void mergeSort(int a, int b) {
		int len = b - a;

		if (len < 2)
			return;

		int m = (a + b) / 2;

		MergeSort left = new MergeSort(a, m);
		MergeSort right = new MergeSort(m, b);
		left.start();
		right.start();

		try {
			left.join();
			right.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		this.merge(a, m, b);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
		this.tmp = Writes.createExternalArray(length);
		this.mergeSort(0, length);
		Writes.deleteExternalArray(tmp);
	}
}

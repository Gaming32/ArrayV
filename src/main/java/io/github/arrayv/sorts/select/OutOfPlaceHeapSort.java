package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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
@SortMeta(name = "Out-of-Place Heap")
public final class OutOfPlaceHeapSort extends Sort {
	public OutOfPlaceHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	// source: https://en.wikipedia.org/wiki/Heapsort#Bottom-up_heapsort

	private void siftDown(int[] array, int i, int b) {
		int j = i;
		for (; 2 * j + 1 < b; j = 2 * j + 2 < b
				? (Reads.compareValues(array[2 * j + 2], array[2 * j + 1]) > 0 ? 2 * j + 2 : 2 * j + 1)
				: 2 * j + 1)
			;
		for (; Reads.compareValues(array[i], array[j]) > 0; j = (j - 1) / 2)
			;
		for (; j > i; j = (j - 1) / 2)
			Writes.swap(array, i, j, 1, true, false);
	}

	private void findNext(int[] array, int b) {
		int i = 0;
		int l = 1;
		int r = 2;

		while (r < b && !(array[l] == -1 && array[r] == -1)) {
			if (array[l] == -1) {
				Writes.swap(array, i, r, 1, true, false);
				i = r;
			} else if (array[r] == -1) {
				Writes.swap(array, i, l, 1, true, false);
				i = l;
			} else if (Reads.compareValues(array[r], array[l]) > 0) {
				Writes.swap(array, i, r, 1, true, false);
				i = r;
			} else {
				Writes.swap(array, i, l, 1, true, false);
				i = l;
			}
			l = 2 * i + 1;
			r = l + 1;
		}
		if (l < b && array[l] != -1)
			Writes.swap(array, i, l, 1, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		for (int i = (length - 1) / 2; i >= 0; i--)
			this.siftDown(array, i, length);

		int[] tmp = Writes.createExternalArray(length);

		for (int i = length - 1; i >= 0; i--) {
			Writes.write(tmp, i, array[0], 0, true, true);
			Writes.write(array, 0, -1, 1, false, false);

			this.findNext(array, length);
		}
		Highlights.clearMark(2);
		Writes.arraycopy(tmp, 0, array, 0, length, 1, true, false);
		Writes.deleteExternalArray(tmp);
	}
}

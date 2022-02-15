package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 Gaming32 Morewenn and aphitorite

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

public final class BinaryDoubleInsertionSort extends Sort {
	public BinaryDoubleInsertionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Binary Double Insertion");
		this.setRunAllSortsName("Binary Double Insertion Sort");
		this.setRunSortName("Binary Double Insertsort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int leftBinarySearch(int[] array, int a, int b, int val, double sleep) {
		while(a < b) {
			int m = a+(b-a)/2;

			Highlights.markArray(1, a);
			Highlights.markArray(2, m);
			Highlights.markArray(3, b);
			Delays.sleep(sleep);

			if(Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}
	private int rightBinarySearch(int[] array, int a, int b, int val, double sleep) {
		while(a < b) {
			int m = a+(b-a)/2;

			Highlights.markArray(1, a);
			Highlights.markArray(2, m);
			Highlights.markArray(3, b);
			Delays.sleep(sleep);

			if(Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}

	private void insertToLeft(int[] array, int a, int b, int temp, double sleep) {
		Highlights.clearMark(2);

		while(a > b) Writes.write(array, a, array[--a], sleep, true, false);
		Writes.write(array, b, temp, sleep, true, false);
	}
	private void insertToRight(int[] array, int a, int b, int temp, double sleep) {
		Highlights.clearMark(2);

		while(a < b) Writes.write(array, a, array[++a], sleep, true, false);
		Writes.write(array, a, temp, sleep, true, false);
	}

	public void doubleInsertion(int[] array, int a, int b, double compSleep, double sleep) {
		if(b-a < 2) return;

		int j = a+(b-a-2)/2+1, i = a+(b-a-1)/2;

		if(j > i && Reads.compareIndices(array, i, j, compSleep, true) == 1)
			Writes.swap(array, i, j, compSleep, true, false);

		i--;
		j++;

		while(j < b) {
			if(Reads.compareIndices(array, i, j, compSleep, true) == 1) {
				int l = array[j];
				int r = array[i];

				int m = this.rightBinarySearch(array, i+1, j, l, compSleep);
				this.insertToRight(array, i, m-1, l, sleep);
				this.insertToLeft(array, j, this.leftBinarySearch(array, m, j, r, compSleep), r, sleep);
			}
			else {
				int l = array[i];
				int r = array[j];

				int m = this.leftBinarySearch(array, i+1, j, l, compSleep);
				this.insertToRight(array, i, m-1, l, sleep);
				this.insertToLeft(array, j, this.rightBinarySearch(array, m, j, r, compSleep), r, sleep);
			}
			i--;
			j++;
		}
		Highlights.clearAllMarks();
	}

	public void customDoubleInsert(int[] array, int a, int b, double sleep) {
		this.doubleInsertion(array, a, b, sleep, sleep);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.doubleInsertion(array, 0, currentLength, 1, 0.05);
	}
}

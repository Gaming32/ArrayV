package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
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

public final class ClassicTreeSort extends Sort {
	public ClassicTreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Classic Tree");
		this.setRunAllSortsName("Classic Unbalanced Tree Sort");
		this.setRunSortName("Classic Unbalanced Treesort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int idx;

	private void traverse(int[] array, int[] temp, int[] lower, int[] upper, int r) {
		Highlights.markArray(1, r);
		Delays.sleep(1);

		if(lower[r] != 0) this.traverse(array, temp, lower, upper, lower[r]);

		Writes.write(temp, this.idx++, array[r], 0, false, true);
		Highlights.markArray(1, r);
		Delays.sleep(1);

		if(upper[r] != 0) this.traverse(array, temp, lower, upper, upper[r]);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int[] lower = Writes.createExternalArray(currentLength);
		int[] upper = Writes.createExternalArray(currentLength);
		int[] next;

		for(int i = 1; i < currentLength; i++) {
			Highlights.markArray(2, i);
			int c = 0;

			while(true) {
				Highlights.markArray(1, c);
				Delays.sleep(0.5);

				next = Reads.compareValues(array[i], array[c]) < 0 ? lower : upper;

				if(next[c] == 0) {
					Writes.write(next, c, i, 0, false, true);
					break;
				}
				else c = next[c];
			}
		}
		Highlights.clearMark(2);

		int[] temp = Writes.createExternalArray(currentLength);
		this.idx = 0;
		this.traverse(array, temp, lower, upper, 0);
		Writes.arraycopy(temp, 0, array, 0, currentLength, 1, true, false);

		Writes.deleteExternalArray(lower);
		Writes.deleteExternalArray(upper);
		Writes.deleteExternalArray(temp);
	}
}

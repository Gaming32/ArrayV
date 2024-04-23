package io.github.arrayv.sorts.insert;

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
@SortMeta(name = "Simplified Library")
public final class SimplifiedLibrarySort extends Sort {
	public SimplifiedLibrarySort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	// simple library sort with O(n) extra memory + counter & pointer array (n size
	// combined)

	private final int R = 4; // rebalancing factor (gaps have dynamic size)

	private BinaryInsertionSort binaryInsert;

	private int getMinLevel(int n) {
		while (n >= 32)
			n = (n - 1) / R + 1;
		return n;
	}

	private int binarySearch(int[] array, int a, int b, int val, double sleep) {
		while (a < b) {
			int m = a + (b - a) / 2;
			Highlights.markArray(3, m);
			Delays.sleep(sleep);

			if (Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m + 1;
		}
		Highlights.clearMark(3);

		return a;
	}

	private void rebalance(int[] array, int[] temp, int[] cnts, int[] locs, int m, int b) {
		// do a partial sum to find locations
		Highlights.clearMark(2);
		for (int i = 0; i < m; i++)
			Writes.write(cnts, i + 1, cnts[i + 1] + cnts[i] + 1, 1, true, true);

		// place books in gaps into their correct locations
		for (int i = m, j = 0; i < b; i++, j++) {
			Highlights.markArray(2, i);
			Writes.write(temp, cnts[locs[j]], array[i], 1, true, true);
			Writes.write(cnts, locs[j], cnts[locs[j]] + 1, 0, false, true);
		}
		for (int i = 0; i < m; i++) {
			Highlights.markArray(2, i);
			Writes.write(temp, cnts[i], array[i], 1, true, true);
			Writes.write(cnts, i, cnts[i] + 1, 0, false, true);
		}
		Highlights.clearMark(2);

		// copy back to array & sort the gaps
		Writes.arraycopy(temp, 0, array, 0, b, 1, true, false);
		this.binaryInsert.customBinaryInsert(array, 0, cnts[0] - 1, 0.5);
		for (int i = 0; i < m - 1; i++)
			this.binaryInsert.customBinaryInsert(array, cnts[i], cnts[i + 1] - 1, 0.5);
		this.binaryInsert.customBinaryInsert(array, cnts[m - 1], cnts[m], 0.5);

		// reset count array
		for (int i = 0; i < m + 2; i++)
			Writes.write(cnts, i, 0, 0, false, true);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.binaryInsert = new BinaryInsertionSort(this.arrayVisualizer);

		if (length < 32) {
			this.binaryInsert.customBinaryInsert(array, 0, length, 1);
			return;
		}

		int j = this.getMinLevel(length);
		this.binaryInsert.customBinaryInsert(array, 0, j, 1);

		int maxLevel = j;
		for (; maxLevel * R < length; maxLevel *= R)
			;

		int[] temp = Writes.createExternalArray(length),
				cnts = Writes.createExternalArray(maxLevel + 2),
				locs = Writes.createExternalArray(length - maxLevel);

		for (int i = j, k = 0; i < length; i++) {
			if (R * j == i) {
				this.rebalance(array, temp, cnts, locs, j, i);
				j = i;
				k = 0;
			}

			// search which gap a book goes and save the result
			Highlights.markArray(2, i);
			int loc = this.binarySearch(array, 0, j, array[i], 0.5);

			Writes.write(cnts, loc + 1, cnts[loc + 1] + 1, 0, false, true);
			Writes.write(locs, k++, loc, 0, false, true);
		}
		this.rebalance(array, temp, cnts, locs, j, length);

		Writes.deleteExternalArray(temp);
		Writes.deleteExternalArray(cnts);
		Writes.deleteExternalArray(locs);
	}
}

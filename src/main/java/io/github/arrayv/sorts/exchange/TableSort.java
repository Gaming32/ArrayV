package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 aphitorite

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
@SortMeta(name = "Table")
public final class TableSort extends Sort {
	public TableSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private boolean stableComp(int[] array, int[] table, int a, int b) {
		int comp = Reads.compareIndices(array, table[a], table[b], 0.5, true);

		return comp > 0 || (comp == 0 && Reads.compareOriginalIndices(table, a, b, 0.5, false) > 0);
	}

	private void medianOfThree(int[] array, int[] table, int a, int b) {
		int m = a + (b - 1 - a) / 2;

		if (this.stableComp(array, table, a, m))
			Writes.swap(table, a, m, 1, true, true);

		if (this.stableComp(array, table, m, b - 1)) {
			Writes.swap(table, m, b - 1, 1, true, true);

			if (this.stableComp(array, table, a, m))
				return;
		}

		Writes.swap(table, a, m, 1, true, true);
	}

	private int partition(int[] array, int[] table, int a, int b, int p) {
		int i = a - 1, j = b;
		Highlights.markArray(3, p);

		while (true) {
			do
				i++;
			while (i < j && !this.stableComp(array, table, i, p));

			do
				j--;
			while (j >= i && this.stableComp(array, table, j, p));

			if (i < j)
				Writes.swap(table, i, j, 1, true, true);
			else
				return j;
		}
	}

	private void quickSort(int[] array, int[] table, int a, int b) {
		if (b - a < 3) {
			if (b - a == 2 && this.stableComp(array, table, a, a + 1))
				Writes.swap(table, a, a + 1, 1, true, true);
			return;
		}

		this.medianOfThree(array, table, a, b);
		int p = this.partition(array, table, a + 1, b, a);
		Writes.swap(table, a, p, 1, true, true);

		this.quickSort(array, table, a, p);
		this.quickSort(array, table, p + 1, b);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int[] table = Writes.createExternalArray(currentLength);
		for (int i = 0; i < currentLength; i++)
			Writes.write(table, i, i, 0.5, true, true);

		this.quickSort(array, table, 0, currentLength);
		Highlights.clearMark(3);

		for (int i = 0; i < table.length; i++) {
			Highlights.markArray(2, i);

			if (Reads.compareOriginalValues(i, table[i]) != 0) {
				int t = array[i];
				int j = i, next = table[i];

				do {
					Writes.write(array, j, array[next], 1, true, false);
					Writes.write(table, j, j, 1, true, true);

					j = next;
					next = table[next];
				} while (Reads.compareOriginalValues(next, i) != 0);

				Writes.write(array, j, t, 1, true, false);
				Writes.write(table, j, j, 1, true, true);
			}
		}

		Writes.deleteExternalArray(table);
	}
}

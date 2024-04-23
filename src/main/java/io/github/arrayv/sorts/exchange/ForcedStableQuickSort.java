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
@SortMeta(name = "Forced Stable Quick")
public final class ForcedStableQuickSort extends Sort {
	public ForcedStableQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void medianOfThree(int[] array, int[] key, int a, int b) {
		int m = a + (b - 1 - a) / 2;

		if (this.stableComp(array, key, a, m))
			this.stableSwap(array, key, a, m);

		if (this.stableComp(array, key, m, b - 1)) {
			this.stableSwap(array, key, m, b - 1);

			if (this.stableComp(array, key, a, m))
				return;
		}

		this.stableSwap(array, key, a, m);
	}

	private boolean stableComp(int[] array, int[] key, int a, int b) {
		int comp = Reads.compareIndices(array, a, b, 0.5, true);

		return comp > 0 || (comp == 0 && Reads.compareOriginalIndices(key, a, b, 0.5, false) > 0);
	}

	private void stableSwap(int[] array, int[] key, int a, int b) {
		Writes.swap(array, a, b, 0, true, false);
		Writes.swap(key, a, b, 1, false, true);
	}

	private int partition(int[] array, int[] key, int a, int b, int p) {
		int i = a - 1, j = b;
		Highlights.markArray(3, p);

		while (true) {
			do
				i++;
			while (i < j && !this.stableComp(array, key, i, p));

			do
				j--;
			while (j >= i && this.stableComp(array, key, j, p));

			if (i < j)
				this.stableSwap(array, key, i, j);
			else
				return j;
		}
	}

	private void quickSort(int[] array, int[] key, int a, int b) {
		if (b - a < 3) {
			if (b - a == 2 && this.stableComp(array, key, a, a + 1))
				this.stableSwap(array, key, a, a + 1);
			return;
		}

		this.medianOfThree(array, key, a, b);
		int p = this.partition(array, key, a + 1, b, a);
		this.stableSwap(array, key, a, p);

		this.quickSort(array, key, a, p);
		this.quickSort(array, key, p + 1, b);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] key = Writes.createExternalArray(length);
		for (int i = 0; i < length; i++)
			Writes.write(key, i, i, 0.5, true, true);

		this.quickSort(array, key, 0, length);

		Writes.deleteExternalArray(key);
	}
}

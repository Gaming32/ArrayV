package io.github.arrayv.sorts.exchange;

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
@SortMeta(listName = "Complete Graph", runName = "Complete Graph Sorting Network")
public final class CompleteGraphSort extends Sort {
	public CompleteGraphSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void compSwap(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.0125, true) > 0)
			Writes.swap(array, a, b, 0.0875, true, false);
	}

	private void split(int[] array, int a, int m, int b) {
		if (b - a < 2)
			return;

		int c = 0, len1 = (b - a) / 2;
		boolean odd = (b - a) % 2 == 1;

		if (odd) {
			if (m - a > b - m)
				c = a++;
			else
				c = --b;
		}
		for (int s = 0; s < len1; s++) {
			int i = a;

			for (int j = s; j < len1; j++)
				this.compSwap(array, i++, m + j);

			for (int j = 0; j < s; j++)
				this.compSwap(array, i++, m + j);
		}
		if (odd) {
			if (c < m)
				for (int j = 0; j < len1; j++)
					this.compSwap(array, c, m + j);
			else
				for (int j = 0; j < len1; j++)
					this.compSwap(array, a + j, c);
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int n = currentLength;
		int d = 2, end = 1 << (int) (Math.log(n - 1) / Math.log(2) + 1);

		while (d <= end) {
			int i = 0, dec = 0;

			while (i < n) {
				int j = i;
				dec += n;

				while (dec >= d) {
					dec -= d;
					j++;
				}
				int k = j;
				dec += n;

				while (dec >= d) {
					dec -= d;
					k++;
				}
				this.split(array, i, j, k);
				i = k;
			}
			d *= 2;
		}
	}
}

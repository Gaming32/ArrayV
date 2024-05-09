package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 _fluffyy, yuji implemented by aphitorite

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

@SortMeta(runName = "Iterative Diamond Sorting Network", listName = "Diamond (Iterative)")
public final class DiamondSortIterative extends Sort {
	public DiamondSortIterative(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void compSwap(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.05, true) == 1)
			Writes.swap(array, a, b, 0.05, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) throws Exception {
		int n = 1;
		for (; n < length; n *= 2)
			;

		int m = 4;
		for (; m <= n; m *= 2) {
			for (int k = 0; k < m / 2; k++) {
				int cnt = k <= m / 4 ? k : m / 2 - k;
				for (int j = 0; j < length; j += m)
					if (j + cnt + 1 < length)
						for (int i = j + cnt; i + 1 < Math.min(length, j + m - cnt); i += 2)
							this.compSwap(array, i, i + 1);
			}
		}
		m /= 2;
		for (int k = 0; k <= m / 2; k++)
			for (int i = k; i + 1 < Math.min(length, m - k); i += 2)
				this.compSwap(array, i, i + 1);
	}
}

package io.github.arrayv.sorts.concurrent;

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

@SortMeta(listName = "Weave (Iterative)", runName = "Iterative Weave Sorting Network")
public final class WeaveSortIterative extends Sort {
	public WeaveSortIterative(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int end;

	private void compSwap(int[] array, int a, int b) {
		if (b < this.end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.end = length;
		int n = 1;
		for (; n < length; n *= 2)
			;

		for (int i = 1; i < n; i *= 2)
			for (int j = 1; j <= i; j *= 2)
				for (int k = 0; k < n; k += n / j)
					for (int d = n / i / 2, m = 0, l = n / j - d; l >= n / j / 2; l -= d)
						for (int p = 0; p < d; p++, m++)
							this.compSwap(array, k + m, k + l + p);
	}
}

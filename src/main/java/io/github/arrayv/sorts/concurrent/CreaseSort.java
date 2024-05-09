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

@SortMeta(listName = "Crease", runName = "Crease Sorting Network")
public final class CreaseSort extends Sort {
	public CreaseSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void compSwap(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int max = 1;
		for (; max * 2 < length; max *= 2)
			;

		int next = max;
		while (next > 0) {
			for (int i = 0; i + 1 < length; i += 2)
				this.compSwap(array, i, i + 1);

			for (int j = max; j >= next && j > 1; j /= 2)
				for (int i = 1; i + j - 1 < length; i += 2)
					this.compSwap(array, i, i + j - 1);

			next /= 2;
		}
	}
}

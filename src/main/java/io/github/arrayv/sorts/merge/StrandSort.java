package io.github.arrayv.sorts.merge;

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
@SortMeta(name = "Strand")
public final class StrandSort extends Sort {
	public StrandSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	// reverses equal items order
	private void mergeTo(int[] array, int[] subList, int a, int m, int b) {
		int i = 0, s = m - a;

		while (i < s && m < b) {
			if (Reads.compareValues(subList[i], array[m]) < 0)
				Writes.write(array, a++, subList[i++], 0.5, true, false);

			else
				Writes.write(array, a++, array[m++], 0.5, true, false);
		}

		while (i < s)
			Writes.write(array, a++, subList[i++], 0.5, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] subList = Writes.createExternalArray(length);

		int j = length, k = j;

		while (j > 0) {
			Writes.write(subList, 0, array[0], 1, true, true);
			k--;

			for (int i = 0, p = 0, m = 1; m < j; m++) {
				if (Reads.compareValues(array[m], subList[i]) >= 0) {
					Writes.write(subList, ++i, array[m], 1, true, true);
					k--;
				} else {
					Highlights.markArray(2, p);
					Writes.write(array, p++, array[m], 0.1, false, false);
				}
			}
			Highlights.clearMark(2);

			this.mergeTo(array, subList, k, j, length);
			j = k;
		}
		Writes.deleteExternalArray(subList);
	}
}

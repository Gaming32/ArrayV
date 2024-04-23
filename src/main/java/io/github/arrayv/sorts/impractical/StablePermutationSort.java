package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/*
 *
MIT License

Copyright (c) 2021 Control, implemented by aphitorite

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
@SortMeta(name = "Stable Permutation", slowSort = true, unreasonableLimit = 11)
public final class StablePermutationSort extends BogoSorting {
	public StablePermutationSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int length;

	private boolean permute(int[] array, int[] idx, int len) {
		if (len < 2)
			return this.isArraySorted(array, this.length);

		for (int i = len - 2; i >= 0; i--) {
			if (this.permute(array, idx, len - 1))
				return true;

			Writes.swap(array, idx[i], idx[len - 1], 0, true, false);
			Writes.swap(idx, i, len - 1, this.delay, false, true);
		}
		if (this.permute(array, idx, len - 1))
			return true;

		int t = idx[len - 1];

		for (int i = len - 1; i > 0; i--)
			Writes.write(idx, i, idx[i - 1], 0, false, true);
		Writes.write(idx, 0, t, 0, false, true);

		t = array[idx[0]];

		for (int i = 1; i < len; i++)
			Writes.write(array, idx[i - 1], array[idx[i]], this.delay, true, false);
		Writes.write(array, idx[len - 1], t, this.delay, true, false);

		return false;
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.length = length;
		int[] idx = Writes.createExternalArray(length);

		for (int i = 0; i < length; i++)
			Writes.write(idx, i, i, this.delay, true, true);

		this.permute(array, idx, length);
		Writes.deleteExternalArray(idx);
	}
}

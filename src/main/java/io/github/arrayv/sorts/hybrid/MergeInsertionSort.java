package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
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
@SortMeta(name = "Merge-Insertion")
public final class MergeInsertionSort extends Sort {
	public MergeInsertionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void blockSwap(int[] array, int a, int b, int s, double sleep) {
		while (s-- > 0)
			Writes.swap(array, a--, b--, sleep, true, false);
	}

	private void blockInsert(int[] array, int a, int b, int s, double sleep) {
		while (a - s >= b) {
			this.blockSwap(array, a - s, a, s, sleep);
			a -= s;
		}
	}

	private void blockReversal(int[] array, int a, int b, int s, double sleep) {
		b -= s;
		while (b > a) {
			this.blockSwap(array, a, b, s, sleep);
			a += s;
			b -= s;
		}
	}

	private int blockSearch(int[] array, int a, int b, int s, int val) {
		while (a < b) {
			int m = a + (((b - a) / s) / 2) * s;

			if (Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m + s;
		}

		return a;
	}

	private void order(int[] array, int a, int b, int s, double sleep) {
		for (int i = a, j = i + s; j < b; i += s, j += 2 * s)
			this.blockInsert(array, j, i, s, sleep);

		int m = a + (((b - a) / s) / 2) * s;
		this.blockReversal(array, m, b, s, 1);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int k = 1;
		while (2 * k <= length) {
			for (int i = 2 * k - 1; i < length; i += 2 * k)
				if (Reads.compareValues(array[i - k], array[i]) > 0)
					this.blockSwap(array, i - k, i, k, 1);

			k *= 2;
		}

		double delay = 12;
		while (k > 0) {
			int a = k - 1, i = a + 2 * k, g = 2, p = 4;
			double sleep = Math.min(1, delay);

			while (i + 2 * k * g - k <= length) {
				this.order(array, i, i + 2 * k * g - k, k, sleep);
				int b = a + k * (p - 1);

				i += k * g - k;
				for (int j = i; j < i + k * g; j += k)
					this.blockInsert(array, j, this.blockSearch(array, a, b, k, array[j]), k, sleep);

				i += k * g + k;
				g = p - g;
				p *= 2;
			}
			while (i < length) {
				this.blockInsert(array, i, this.blockSearch(array, a, i, k, array[i]), k, sleep);
				i += 2 * k;
			}

			k /= 2;
			delay /= 2;
		}
	}
}

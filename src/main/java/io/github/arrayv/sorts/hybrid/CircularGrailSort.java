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
@SortMeta(name = "Circular Grail")
public final class CircularGrailSort extends Sort {
	public CircularGrailSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int n;

	private void circSwap(int[] array, int a, int b) {
		Writes.swap(array, a % n, b % n, 1, true, false);
	}

	private int circCompareIndices(int[] array, int a, int b) {
		return Reads.compareValues(array[a % n], array[b % n]);
	}

	private void shiftFW(int[] array, int a, int m, int b) {
		while (m < b)
			this.circSwap(array, a++, m++);
	}

	private void shiftBW(int[] array, int a, int m, int b) {
		while (m > a)
			this.circSwap(array, --b, --m);
	}

	private void insertion(int[] array, int a, int b) {
		for (int i = a + 1; i < b; i++)
			while (i > a && this.circCompareIndices(array, i - 1, i) > 0)
				this.circSwap(array, i, --i);
	}

	private void multiSwap(int[] array, int a, int b, int len) {
		for (int i = 0; i < len; i++)
			this.circSwap(array, a + i, b + i);
	}

	private void rotate(int[] array, int a, int m, int b) {
		int l = m - a, r = b - m;

		while (l > 0 && r > 0) {
			if (r < l) {
				this.multiSwap(array, m - r, m, r);
				b -= r;
				m -= r;
				l -= r;
			} else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
			}
		}
	}

	private void inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while (i < j && j < b) {
			if (this.circCompareIndices(array, i, j) > 0) {
				k = j;
				while (++k < b && this.circCompareIndices(array, i, k) > 0)
					;

				this.rotate(array, i, j, k);

				i += k - j;
				j = k;
			} else
				i++;
		}
	}

	private int merge(int[] array, int p, int a, int m, int b, boolean full) {
		int i = a, j = m;

		while (i < m && j < b) {
			if (this.circCompareIndices(array, i, j) <= 0)
				this.circSwap(array, p++, i++);

			else
				this.circSwap(array, p++, j++);
		}
		if (i < m) {
			if (i > p)
				this.shiftFW(array, p, i, m);
		} else if (full)
			this.shiftFW(array, p, j, b);

		return i < m ? i : j;
	}

	private boolean blockLessThan(int[] array, int a, int b, int bLen) {
		int cmp = this.circCompareIndices(array, a, b);

		return cmp == -1 || (cmp == 0 && this.circCompareIndices(array, a + bLen - 1, b + bLen - 1) == -1);
	}

	private void blockMerge(int[] array, int a, int m, int b, int bLen) {
		int b1 = b - (b - m - 1) % bLen - 1;

		if (b1 > m) {
			int b2 = b1;

			for (int i = m - bLen; i > a && this.blockLessThan(array, b1, i, bLen); i -= bLen, b2 -= bLen)
				;

			for (int j = a; j < b1 - bLen; j += bLen) {
				int min = j;

				for (int i = min + bLen; i < b1; i += bLen)
					if (this.blockLessThan(array, i, min, bLen))
						min = i;

				if (min != j)
					this.multiSwap(array, j, min, bLen);
			}
			int f = a;

			for (int i = a + bLen; i < b2; i += bLen) {
				f = this.merge(array, f - bLen, f, i, i + bLen, false);

				if (f < i) {
					this.shiftBW(array, f, i, i + bLen);
					f += bLen;
				}
			}
			this.merge(array, f - bLen, f, b1, b, true);
		} else
			this.merge(array, a - bLen, a, m, b, true);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.n = length;

		if (length <= 16) {
			this.insertion(array, 0, length);
			return;
		}

		int bLen = 1;
		for (; bLen * bLen < length; bLen *= 2)
			;

		int i = bLen, j = 1, len = length - i, b = length;

		while (j <= bLen) {
			for (; i + 2 * j < b; i += 2 * j)
				this.merge(array, i - j, i, i + j, i + 2 * j, true);
			if (i + j < b)
				this.merge(array, i - j, i, i + j, b, true);
			else
				this.shiftFW(array, i - j, i, b);

			i = b + bLen - j;
			b = i + len;
			j *= 2;
		}
		while (j < len) {
			for (; i + 2 * j < b; i += 2 * j)
				this.blockMerge(array, i, i + j, i + 2 * j, bLen);
			if (i + j < b)
				this.blockMerge(array, i, i + j, b, bLen);
			else
				this.shiftFW(array, i - bLen, i, b);

			i = b;
			b += len;
			j *= 2;
		}
		this.insertion(array, i - bLen, i);
		this.inPlaceMerge(array, i - bLen, i, b);

		this.rotate(array, 0, (i - bLen) % n, length);
	}
}

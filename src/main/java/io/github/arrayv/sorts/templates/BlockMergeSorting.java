package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.IndexedRotations;

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

public abstract class BlockMergeSorting extends Sort {
	protected BlockMergeSorting(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	protected final int MRUN = 16;

	protected void shiftFW(int[] array, int a, int m, int b) {
		while (m < b)
			Writes.swap(array, a++, m++, 1, true, false);
	}

	protected void shiftBW(int[] array, int a, int m, int b) {
		while (m > a)
			Writes.swap(array, --b, --m, 1, true, false);
	}

	protected void shiftFWExt(int[] array, int a, int m, int b) {
		Highlights.clearMark(2);
		while (m < b)
			Writes.write(array, a++, array[m++], 1, true, false);
	}

	protected void shiftBWExt(int[] array, int a, int m, int b) {
		Highlights.clearMark(2);
		while (m > a)
			Writes.write(array, --b, array[--m], 1, true, false);
	}

	protected void insertTo(int[] array, int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while (a > b)
			Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, temp, 0.5, true, false);
	}

	protected void insertToBW(int[] array, int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while (a < b)
			Writes.write(array, a, array[++a], 0.5, true, false);
		Writes.write(array, a, temp, 0.5, true, false);
	}

	protected void multiSwap(int[] array, int a, int b, int len) {
		for (int i = 0; i < len; i++)
			Writes.swap(array, a + i, b + i, 1, true, false);
	}

	protected void rotate(int[] array, int a, int m, int b) {
		Highlights.clearMark(2);
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
	}

	protected int leftBinSearch(int[] array, int a, int b, int val) {
		while (a < b) {
			int m = a + (b - a) / 2;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);

			if (Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m + 1;
		}
		return a;
	}

	protected int rightBinSearch(int[] array, int a, int b, int val) {
		while (a < b) {
			int m = a + (b - a) / 2;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);

			if (Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m + 1;
		}
		return a;
	}

	protected boolean buildRuns(int[] array, int a, int b) {
		boolean noSort = true;
		int i = a + 1, j = a;

		while (i < b) {
			if (Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
				while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1)
					i++;
				Writes.reversal(array, j, i - 1, 1, true, false);
			} else
				while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0)
					i++;

			if (i < b) {
				noSort = false;
				j = i - (i - j - 1) % this.MRUN - 1; // a%b, if(a%b == 0) -> a = b
			}
			while (i - j < this.MRUN && i < b) {
				this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
				i++;
			}
			j = i++;
		}
		return noSort;
	}

	protected int findKeys(int[] array, int a, int b, int nKeys, int n) {
		int p = a, pEnd = a + nKeys;

		Highlights.clearMark(2);
		for (int i = pEnd; i < b && nKeys < n; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			int loc = this.leftBinSearch(array, p, pEnd, array[i]);

			if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
				this.rotate(array, p, pEnd, i);
				int inc = i - pEnd;
				loc += inc;
				p += inc;
				pEnd += inc;

				this.insertTo(array, pEnd, loc);
				nKeys++;
				pEnd++;
			}
		}
		this.rotate(array, a, p, pEnd);
		return nKeys;
	}

	protected int findKeysBW(int[] array, int a, int b, int nKeys, int n) {
		int p = b - nKeys, pEnd = b;

		Highlights.clearMark(2);
		for (int i = p - 1; i >= a && nKeys < n; i--) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			int loc = this.leftBinSearch(array, p, pEnd, array[i]);

			if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
				this.rotate(array, i + 1, p, pEnd);
				int inc = p - (i + 1);
				loc -= inc;
				pEnd -= inc;
				p -= inc + 1;
				nKeys++;

				this.insertToBW(array, i, loc - 1);
			}
		}
		this.rotate(array, p, pEnd, b);
		return nKeys;
	}

	protected void binaryInsertion(int[] array, int a, int b) {
		for (int i = a + 1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
	}

	protected boolean boundCheck(int[] array, int a, int m, int b) {
		return m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0;
	}

	protected void mergeBW(int[] array, int a, int m, int b, int p) {
		if (this.boundCheck(array, a, m, b))
			return;

		int pLen = b - m;
		this.multiSwap(array, m, p, pLen);

		int i = pLen - 1, j = m - 1, k = b - 1;

		while (i >= 0 && j >= a) {
			if (Reads.compareValues(array[p + i], array[j]) >= 0)
				Writes.swap(array, k--, p + (i--), 1, true, false);
			else
				Writes.swap(array, k--, j--, 1, true, false);
		}
		while (i >= 0)
			Writes.swap(array, k--, p + (i--), 1, true, false);
	}

	protected void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;

		while (i < m && j < b) {
			if (Reads.compareValues(array[i], array[j]) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else
				Writes.swap(array, p++, j++, 1, true, false);
		}
		while (i < m)
			Writes.swap(array, p++, i++, 1, true, false);
		while (j < b)
			Writes.swap(array, p++, j++, 1, true, false);
	}

	protected void pingPongMerge(int[] array, int a, int m1, int m2, int m3, int b, int p) {
		if (Reads.compareValues(array[m1 - 1], array[m1]) > 0
				|| (m3 < b && Reads.compareValues(array[m3 - 1], array[m3]) > 0)) {
			int p1 = p + m2 - a, pEnd = p + b - a;

			this.mergeTo(array, a, m1, m2, p);
			this.mergeTo(array, m2, m3, b, p1);
			this.mergeTo(array, p, p1, pEnd, a);
		} else
			this.mergeBW(array, a, m2, b, p);
	}

	protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
		int s = m - a;

		Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);

		int i = 0, j = m;

		while (i < s && j < b) {
			if (Reads.compareValues(tmp[i], array[j]) <= 0)
				Writes.write(array, a++, tmp[i++], 1, true, false);
			else
				Writes.write(array, a++, array[j++], 1, true, false);
		}
		while (i < s)
			Writes.write(array, a++, tmp[i++], 1, true, false);
	}

	protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
		int s = b - m;

		Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);

		int i = s - 1, j = m - 1;

		while (i >= 0 && j >= a) {
			if (Reads.compareValues(tmp[i], array[j]) >= 0)
				Writes.write(array, --b, tmp[i--], 1, true, false);
			else
				Writes.write(array, --b, array[j--], 1, true, false);
		}
		while (i >= 0)
			Writes.write(array, --b, tmp[i--], 1, true, false);
	}

	protected void mergeWithBufFW(int[] array, int a, int m, int b, int p) {
		int i = m;

		while (a < m && i < b) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(array[a], array[i]) <= 0)
				Writes.swap(array, p++, a++, 1, true, false);
			else
				Writes.swap(array, p++, i++, 1, true, false);
		}

		if (a > p)
			this.shiftFW(array, p, a, m);

		this.shiftFW(array, p, i, b);
	}

	protected void mergeWithBufBW(int[] array, int a, int m, int b, int p) {
		int i = m - 1;
		b--;

		while (b >= m && i >= a) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(array[b], array[i]) >= 0)
				Writes.swap(array, --p, b--, 1, true, false);
			else
				Writes.swap(array, --p, i--, 1, true, false);
		}

		if (p > b)
			this.shiftBW(array, m, b + 1, p);

		this.shiftBW(array, a, i + 1, p);
	}

	protected void mergeWithBufFWExt(int[] array, int a, int m, int b, int p) {
		int i = m;

		while (a < m && i < b) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(array[a], array[i]) <= 0)
				Writes.write(array, p++, array[a++], 1, true, false);
			else
				Writes.write(array, p++, array[i++], 1, true, false);
		}

		if (a > p)
			this.shiftFWExt(array, p, a, m);

		this.shiftFWExt(array, p, i, b);
	}

	protected void mergeWithBufBWExt(int[] array, int a, int m, int b, int p) {
		int i = m - 1;
		b--;

		while (b >= m && i >= a) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(array[b], array[i]) >= 0)
				Writes.write(array, --p, array[b--], 1, true, false);
			else
				Writes.write(array, --p, array[i--], 1, true, false);
		}

		if (p > b)
			this.shiftBWExt(array, m, b + 1, p);

		this.shiftBWExt(array, a, i + 1, p);
	}

	protected void inPlaceMerge(int[] array, int a, int m, int b) {
		while (a < m && m < b) {
			a = this.rightBinSearch(array, a, m, array[m]);

			if (a == m)
				return;

			int i = this.leftBinSearch(array, m, b, array[a]);

			this.rotate(array, a, m, i);

			int t = i - m;
			m = i;
			a += t + 1;
		}
	}

	protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
		while (b > m && m > a) {
			int i = this.rightBinSearch(array, a, m, array[b - 1]);

			this.rotate(array, i, m, b);

			int t = m - i;
			m = i;
			b -= t + 1;

			if (m == a)
				break;

			b = this.leftBinSearch(array, m, b, array[m - 1]);
		}
	}
}

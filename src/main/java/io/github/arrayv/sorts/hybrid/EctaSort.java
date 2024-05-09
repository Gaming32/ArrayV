package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BinaryDoubleInsertionSort;
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
@SortMeta(name = "Ecta")
public final class EctaSort extends Sort {
	public EctaSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int getMinRun(int n) {
		int mRun = n;
		for (; mRun >= 32; mRun = (mRun + 1) / 2)
			;

		return mRun;
	}

	private void shift(int[] array, int a, int m, int b) {
		while (m < b)
			Writes.write(array, a++, array[m++], 1, true, false);
	}

	private void shiftBW(int[] array, int a, int m, int b) {
		while (m > a)
			Writes.write(array, --b, array[--m], 1, true, false);
	}

	private void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;

		while (i < m && j < b) {
			if (Reads.compareIndices(array, i, j, 0, false) <= 0)
				Writes.write(array, p++, array[i++], 1, true, false);
			else
				Writes.write(array, p++, array[j++], 1, true, false);
		}
		while (i < m)
			Writes.write(array, p++, array[i++], 1, true, false);
		while (j < b)
			Writes.write(array, p++, array[j++], 1, true, false);
	}

	private void pingPongMerge(int[] array, int a, int m1, int m2, int m3, int b, int p) {
		int p1 = p + m2 - a, pEnd = p + b - a;

		this.mergeTo(array, a, m1, m2, p);
		this.mergeTo(array, m2, m3, b, p1);
		this.mergeTo(array, p, p1, pEnd, a);
	}

	private void merge(int[] array, int a, int m, int b, int p) {
		int len = b - m, pEnd = p + len - 1;
		Writes.arraycopy(array, m--, array, p, len, 1, true, false);

		while (m >= a && pEnd >= p) {
			if (Reads.compareValues(array[m], array[pEnd]) > 0)
				Writes.write(array, --b, array[m--], 1, true, false);

			else
				Writes.write(array, --b, array[pEnd--], 1, true, false);
		}
		while (pEnd >= p)
			Writes.write(array, --b, array[pEnd--], 1, true, false);
	}

	private void mergeFromBuf(int[] array, int[] buf, int a, int m, int b, int bufLen) {
		int i = 0;

		while (i < bufLen && m < b) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(buf[i], array[m]) <= 0)
				Writes.write(array, a++, buf[i++], 1, true, false);
			else
				Writes.write(array, a++, array[m++], 1, true, false);
		}
		while (i < bufLen) {
			Highlights.markArray(2, i);
			Writes.write(array, a++, buf[i++], 1, true, false);
		}
	}

	private void dualMergeFromBufBW(int[] array, int[] buf, int a, int a1, int m, int b, int bufLen) {
		int i = bufLen - 1;
		bufLen -= b - (m--);

		while (i >= bufLen && m >= a1) {
			Highlights.markArray(2, i);

			if (Reads.compareValues(buf[i], array[m]) > 0)
				Writes.write(array, --b, buf[i--], 1, true, false);
			else
				Writes.write(array, --b, array[m--], 1, true, false);
		}
		if (m < a1)
			while (i >= 0)
				Writes.write(array, --b, buf[i--], 1, true, false);
		else
			this.mergeFromBuf(array, buf, a, a1, b, bufLen);
	}

	private int mergeSort(int[] array, int a, int b, int p, int mRun, int bufLen) {
		BinaryDoubleInsertionSort smallSort = new BinaryDoubleInsertionSort(this.arrayVisualizer);
		int i = a, j = mRun;

		for (; i + j <= b; i += j)
			smallSort.customDoubleInsert(array, i, i + j, 0.5);
		smallSort.customDoubleInsert(array, i, b, 0.5);

		while (4 * j <= bufLen) {
			for (i = a; i + 4 * j <= b; i += 4 * j)
				this.pingPongMerge(array, i, i + j, i + 2 * j, i + 3 * j, i + 4 * j, p);

			if (i + 3 * j < b)
				this.pingPongMerge(array, i, i + j, i + 2 * j, i + 3 * j, b, p);
			else if (i + 2 * j < b)
				this.pingPongMerge(array, i, i + j, i + 2 * j, b, b, p);
			else if (i + j < b)
				this.merge(array, i, i + j, b, p);

			j *= 4;
		}
		while (j <= bufLen) {
			for (i = a; i + 2 * j <= b; i += 2 * j)
				this.merge(array, i, i + j, i + 2 * j, p);

			if (i + j < b)
				this.merge(array, i, i + j, b, p);

			j *= 2;
		}

		return j;
	}

	private void blockCycle(int[] array, int[] keys, int a, int bLen, int t, int p, boolean excl, boolean fw) {
		int s = fw ? bLen : -bLen;

		for (int i = 0; i < t; i++) {
			if (Reads.compareOriginalValues(i, keys[i]) != 0) {
				Writes.arraycopy(array, a + i * s, array, p, bLen, 1, true, false);
				int j = i, next = keys[i];

				do {
					if (!(excl && j == t - 1))
						Writes.arraycopy(array, a + next * s, array, a + j * s, bLen, 1, true, false);
					Highlights.markArray(2, j);
					Writes.write(keys, j, j, 1, false, true);

					j = next;
					next = keys[next];
				} while (Reads.compareOriginalValues(next, i) != 0);

				Writes.arraycopy(array, p, array, a + j * s, bLen, 1, true, false);
				Highlights.markArray(2, j);
				Writes.write(keys, j, j, 1, false, true);
			}
		}
		Highlights.clearMark(2);
	}

	private void ectaMergeFW(int[] array, int[] tags, int a, int m, int b, int bLen) {
		int i = a, j = m, t = 0, tc = 0;
		int[] s = { 2 * bLen, 0 };
		int[] p = { a - 2 * bLen, m };

		do {
			int c = s[0] < bLen ? 1 : 0;

			for (int k = 0; k < bLen; k++) {
				if (i < m && j < b) {
					if (Reads.compareValues(array[i], array[j]) <= 0) {
						Writes.write(array, p[c] + k, array[i++], 1, true, false);
						s[0]++;
					} else {
						Writes.write(array, p[c] + k, array[j++], 1, true, false);
						s[1]++;
					}
				} else if (i < m) {
					Writes.write(array, p[c] + k, array[i++], 1, true, false);
					s[0]++;
				} else {
					Writes.write(array, p[c] + k, array[j++], 1, true, false);
					s[1]++;
				}
			}
			p[c] += bLen;
			s[c] -= bLen;

			Highlights.markArray(2, tc);
			Writes.write(tags, tc++, c == 0 ? t++ : -1, 1, false, true);
		} while (i < m || j < b);

		if (s[0] > 0)
			Writes.write(tags, tc, t++, 1, false, true);

		for (int k = 2; k < tc; k++) {
			if (tags[k] == -1) {
				Highlights.markArray(2, k);
				Writes.write(tags, k, t++, 1, false, true);
			}
		}
		this.blockCycle(array, tags, a - 2 * bLen, bLen, t, b - bLen, s[0] > 0, true);
	}

	private void ectaMergeBW(int[] array, int[] tags, int a, int m, int b, int bLen) {
		int i = b - 1, j = m - 1, t = 0, tc = 0;
		int[] s = { 2 * bLen, 0 };
		int[] p = { b + 2 * bLen, m };

		do {
			int c = s[0] < bLen ? 1 : 0;

			for (int k = 1; k <= bLen; k++) {
				if (i >= m && j >= a) {
					if (Reads.compareValues(array[i], array[j]) >= 0) {
						Writes.write(array, p[c] - k, array[i--], 1, true, false);
						s[0]++;
					} else {
						Writes.write(array, p[c] - k, array[j--], 1, true, false);
						s[1]++;
					}
				} else if (i >= m) {
					Writes.write(array, p[c] - k, array[i--], 1, true, false);
					s[0]++;
				} else {
					Writes.write(array, p[c] - k, array[j--], 1, true, false);
					s[1]++;
				}
			}
			p[c] -= bLen;
			s[c] -= bLen;

			Highlights.markArray(2, tc);
			Writes.write(tags, tc++, c == 0 ? t++ : -1, 1, false, true);
		} while (i >= m || j >= a);

		if (s[0] > 0)
			Writes.write(tags, tc, t++, 1, false, true);

		for (int k = 2; k < tc; k++) {
			if (tags[k] == -1) {
				Highlights.markArray(2, k);
				Writes.write(tags, k, t++, 1, false, true);
			}
		}
		this.blockCycle(array, tags, b + bLen, bLen, t, a, s[0] > 0, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		if (length < 256) {
			if (length <= 32) {
				BinaryDoubleInsertionSort smallSort = new BinaryDoubleInsertionSort(this.arrayVisualizer);
				smallSort.customDoubleInsert(array, 0, length, 0.5);
			} else {
				int mRun = this.getMinRun(length), bufLen = length / 2;
				int[] buf = Writes.createExternalArray(bufLen);

				Writes.arraycopy(array, bufLen, buf, 0, bufLen, 1, true, true);
				this.mergeSort(array, 0, bufLen, bufLen, mRun, bufLen);

				Writes.arraycopy(buf, 0, array, bufLen, bufLen, 1, true, false);
				Writes.arraycopy(array, 0, buf, 0, bufLen, 1, true, true);
				this.mergeSort(array, bufLen, length, 0, mRun, bufLen);

				this.mergeFromBuf(array, buf, 0, bufLen, length, bufLen);
				Writes.deleteExternalArray(buf);
			}
			return;
		}

		int mRun = this.getMinRun(length), bLen = mRun;
		for (; bLen * bLen < length / 2; bLen *= 2)
			;
		int bufLen = 2 * bLen + length % bLen;

		int a = bufLen, b = length, len = b - a;

		int[] buf = Writes.createExternalArray(bufLen);
		int[] tags = Writes.createExternalArray(len / bLen + 1);

		Writes.arraycopy(array, a, buf, 0, bufLen, 1, true, true);
		this.mergeSort(array, 0, a, a, this.getMinRun(bufLen), bufLen);

		Writes.arraycopy(buf, 0, array, a, bufLen, 1, true, false);
		Writes.arraycopy(array, 0, buf, 0, bufLen, 1, true, true);

		int i = a, j = this.mergeSort(array, a, b, 0, mRun, bufLen);
		boolean bw = false;

		while (j < len) {
			for (i = a; i + 2 * j <= b; i += 2 * j)
				this.ectaMergeFW(array, tags, i, i + j, i + 2 * j, bLen);

			if (i + j < b)
				this.ectaMergeFW(array, tags, i, i + j, b, bLen);
			else
				this.shift(array, i - 2 * bLen, i, b);

			j *= 2;
			a -= 2 * bLen;
			b -= 2 * bLen;

			if (j >= len) {
				bw = true;
				break;
			}

			for (i = a; i + 2 * j <= b; i += 2 * j)
				;

			if (i + j < b)
				this.ectaMergeBW(array, tags, i, i + j, b, bLen);
			else
				this.shiftBW(array, i, b, b + 2 * bLen);

			for (i -= 2 * j; i >= a; i -= 2 * j)
				this.ectaMergeBW(array, tags, i, i + j, i + 2 * j, bLen);

			j *= 2;
			a += 2 * bLen;
			b += 2 * bLen;
		}
		if (bw)
			this.dualMergeFromBufBW(array, buf, 0, a, b, length, bufLen);
		else
			this.mergeFromBuf(array, buf, 0, a, b, bufLen);

		Writes.deleteExternalArray(buf);
		Writes.deleteExternalArray(tags);
	}
}

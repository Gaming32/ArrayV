package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.select.MaxHeapSort;
import io.github.arrayv.sorts.templates.BlockMergeSorting;

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

public final class ChaliceSort extends BlockMergeSorting {
    public ChaliceSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Chalice");
        this.setRunAllSortsName("Chalice Sort");
        this.setRunSortName("Chalicesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	//stable merge sort using O(cbrt n) dynamic external buffer

	private int ceilCbrt(int n) {
		int a = 0, b = 11;

		while(a < b) {
			int m = (a+b)/2;

			if((1 << 3*m) >= n) b = m;
			else                a = m+1;
		}

		return 1 << a;
	}

	private int calcKeys(int bLen, int n) { //assumes keys needed is <= n/4
		int a = 1, b = n/4;

		while(a < b) {
			int m = (a+b)/2;

			if((n-4*m-1)/bLen-2 < m) b = m;
			else                     a = m+1;
		}

		return a;
	}

	private void laziestSortExt(int[] array, int[] tmp, int a, int b) {
		for(int i = a, s = tmp.length; i < b; i += s) {
			int j = Math.min(b, i+s);
			this.binaryInsertion(array, i, j);
			if(i > a) this.mergeBWExt(array, tmp, a, i, j);
		}
	}

	private int[] findKeysSm(int[] array, int a, int b, int a1, int b1, boolean full, int n) {
		int p = a, pEnd = 0;

		if(full) {
			for(; p < b; p++) {
				Highlights.markArray(1, p);
				int loc = this.leftBinSearch(array, a1, b1, array[p]);

				if(loc == b1 || Reads.compareValues(array[p], array[loc]) != 0) {
					pEnd = p+1;
					break;
				}
			}
			if(pEnd != 0) {
				for(int i = pEnd; i < b && pEnd-p < n; i++) {
					Highlights.markArray(1, i);
					int loc = this.leftBinSearch(array, a1, b1, array[i]);

					if(loc == b1 || Reads.compareValues(array[i], array[loc]) != 0) {
						loc = this.leftBinSearch(array, p, pEnd, array[i]);

						if(loc == pEnd || Reads.compareValues(array[i], array[loc]) != 0) {
							this.rotate(array, p, pEnd, i);

							int len1 = i-pEnd;
							p    += len1;
							loc  += len1;
							pEnd  = i+1;

							this.insertTo(array, i, loc);
						}
					}
				}
			}
			else pEnd = p;
		}
		else {
			pEnd = p+1;

			for(int i = pEnd; i < b && pEnd-p < n; i++) {
				Highlights.markArray(1, i);
				int loc = this.leftBinSearch(array, p, pEnd, array[i]);

				if(loc == pEnd || Reads.compareValues(array[i], array[loc]) != 0) {
					this.rotate(array, p, pEnd, i);

					int len1 = i-pEnd;
					p    += len1;
					loc  += len1;
					pEnd  = i+1;

					this.insertTo(array, i, loc);
				}
			}
		}
		int[] t = {p, pEnd};
		return t;
	}
	private int findKeys(int[] array, int[] tmp, int a, int b, int n, int s) { //searches for n keys s blocks at a time
		int[] t = this.findKeysSm(array, a, b, 0, 0, false, Math.min(n, s));
		int p = t[0], pEnd = t[1];

		if(s < n && pEnd-p == s) {
			for(n -= s; ; n -= s) {
				t = this.findKeysSm(array, pEnd, b, p, pEnd, true, Math.min(s, n));
				int keys = t[1]-t[0];

				if(keys == 0) break;

				if(keys < s || n == s) {
					this.rotate(array, pEnd, t[0], t[1]);

					t[0] = pEnd;
					pEnd += keys;

					this.mergeBWExt(array, tmp, p, t[0], pEnd); //merge can be done inplace + stable
					break;
				}
				else {
					this.rotate(array, p, pEnd, t[0]);

					p += t[0]-pEnd;
					pEnd = t[1];

					this.mergeBWExt(array, tmp, p, t[0], pEnd);
				}
			}
		}
		this.rotate(array, a, p, pEnd);
		return pEnd-p;
	}

	private int[] findBitsSm(int[] array, int a, int b, int a1, boolean bw, int n) {
		int p = a, pEnd, cmp = bw ? -1 : 1;

		while(p < b && Reads.compareIndices(array, p, a1, 1, true) != cmp) p++;
		a1++;

		if(p < b) {
			pEnd = p+1;

			for(int i = pEnd; i < b && pEnd-p < n; i++) {
				if(Reads.compareIndices(array, i, a1, 1, true) == cmp) {
					this.rotate(array, p, pEnd, i);

					p += i-pEnd;
					pEnd = i+1;
					a1++;
				}
			}
		}
		else pEnd = p;

		int[] t = {p, pEnd};
		return t;
	}
	private int findBits(int[] array, int[] tmp, int a, int b, int n, int s) {
		this.laziestSortExt(array, tmp, a, a+n);

		int a0 = a, a1 = a+n, c = 0, c0 = 0;

		for(int i = 0; c < n && i < 2; i++) {
			int p = a1, pEnd = p;

			while(true) {
				int[] t = this.findBitsSm(array, pEnd, b, a0, i == 1, Math.min(s, n-c));
				int bits = t[1]-t[0];

				if(bits == 0) break;

				a0 += bits;
				c  += bits;

				if(bits < s || c == n) {
					this.rotate(array, pEnd, t[0], t[1]);

					t[0] = pEnd;
					pEnd += bits;

					break;
				}
				else {
					this.rotate(array, p, pEnd, t[0]);

					p += t[0]-pEnd;
					pEnd = t[1];
				}
			}
			this.rotate(array, a1, p, pEnd);
			a1 += pEnd-p;

			if(i == 0) c0 = c;
		}

		//returns the count of ascending pairs of elements NOT how many bits found

		if(c < n) return -1;

		else {
			this.multiSwap(array, a+c0, a+n+c0, n-c0);
			return c0;
		}
	}

	private void bitReversal(int[] array, int a, int b) {
		int len = b-a, m = 0;
		int d1 = len>>1, d2 = d1+(d1>>1);

		for(int i = 1; i < len-1; i++) {
			int j = d1;

			for(
				int k = i, n = d2;
				(k&1) == 0;
				j -= n, k >>= 1, n >>= 1
			);
			m += j;
			if(m > i) Writes.swap(array, a+i, a+m, 1, true, false);
		}
	}
	private void unshuffle(int[] array, int a, int b) { //b-a is even
		int len = (b-a)>>1, c = 0;

		for(int n = 2; len > 0; len >>= 1, n *= 2) {
			if((len&1) == 1) {
				int a1 = a+c;

				this.bitReversal(array, a1,     a1+n);
				this.bitReversal(array, a1,     a1+n/2);
				this.bitReversal(array, a1+n/2, a1+n);
				this.rotate(array, a+c/2, a1, a1+n/2);

				c += n;
			}
		}
	}

	private void redistBuffer(int[] array, int[] tmp, int a, int m, int b) {
		int s = tmp.length;

		while(m-a > s && m < b) {
			int i = this.leftBinSearch(array, m, b, array[a+s]);
			this.rotate(array, a+s, m, i);

			int t = i-m;
			m = i;

			this.mergeFWExt(array, tmp, a, a+s, m);
			a += t+s;
		}
		if(m < b) this.mergeFWExt(array, tmp, a, m, b);
	}

	private void dualMergeBW(int[] array, int a, int m, int b, int p) {
		int i = m-1; b--;

		while(p > b+1 && b >= m) {
			Highlights.markArray(2, i);

			if(Reads.compareValues(array[b], array[i]) >= 0)
				Writes.swap(array, --p, b--, 1, true, false);
			else
				Writes.swap(array, --p, i--, 1, true, false);
		}

		if(b < m) this.shiftBW(array, a, i+1, p);

		else {
			i++; b++; p = m-(i-a);

			while(a < i && m < b) {
				Highlights.markArray(2, m);

				if(Reads.compareValues(array[a], array[m]) <= 0)
					Writes.swap(array, p++, a++, 1, true, false);
				else
					Writes.swap(array, p++, m++, 1, true, false);
			}
			while(a < i) Writes.swap(array, p++, a++, 1, true, false);
		}
	}
	private void dualMergeBWExt(int[] array, int a, int m, int b, int p) {
		int i = m-1; b--;

		while(p > b+1 && b >= m) {
			Highlights.markArray(2, i);

			if(Reads.compareValues(array[b], array[i]) >= 0)
				Writes.write(array, --p, array[b--], 1, true, false);
			else
				Writes.write(array, --p, array[i--], 1, true, false);
		}

		if(b < m) this.shiftBWExt(array, a, i+1, p);

		else {
			i++; b++; p = m-(i-a);

			while(a < i && m < b) {
				Highlights.markArray(2, m);

				if(Reads.compareValues(array[a], array[m]) <= 0)
					Writes.write(array, p++, array[a++], 1, true, false);
				else
					Writes.write(array, p++, array[m++], 1, true, false);
			}
			while(a < i) Writes.write(array, p++, array[a++], 1, true, false);
		}
	}

	private int smartMerge(int[] array, int p, int a, int m, boolean rev) {
		int i = m, cmp = rev ? 0 : 1;

		while(a < m) {
			Highlights.markArray(2, i);

			if(Reads.compareValues(array[a], array[i]) < cmp)
				Writes.write(array, p++, array[a++], 1, true, false);
			else
				Writes.write(array, p++, array[i++], 1, true, false);
		}

		return i;
	}
	private void smartTailMerge(int[] array, int[] tmp, int p, int a, int m, int b) {
		int i = m, bLen = tmp.length;

		while(a < m && i < b) {
			Highlights.markArray(2, i);

			if(Reads.compareValues(array[a], array[i]) <= 0)
				Writes.write(array, p++, array[a++], 1, true, false);
			else
				Writes.write(array, p++, array[i++], 1, true, false);
		}
		if(a < m) {
			if(a > p) this.shiftFWExt(array, p, a, m);
			Writes.arraycopy(tmp, 0, array, b-bLen, bLen, 1, true, false);
		}
		else {
			a = 0;

			while(a < bLen && i < b) {
				Highlights.markArray(2, i);

				if(Reads.compareValues(tmp[a], array[i]) <= 0)
					Writes.write(array, p++, tmp[a++], 1, true, false);
				else
					Writes.write(array, p++, array[i++], 1, true, false);
			}
			while(a < bLen) Writes.write(array, p++, tmp[a++], 1, true, false);
		}
	}
	private void blockCycle(int[] array, int a, int t, int tIdx, int tLen, int bLen) {
		for(int i = 0; i < tLen-1; i++) {
			if(Reads.compareValues(array[t+i], array[tIdx+i]) > 0 ||
			   (i > 0 && Reads.compareValues(array[t+i], array[tIdx+i-1]) < 0)) {

				Writes.arraycopy(array, a+i*bLen, array, a-bLen, bLen, 1, true, false);

				int val = i, next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;

				do {
					Writes.arraycopy(array, a+next*bLen, array, a+val*bLen, bLen, 1, true, false);
					Writes.swap(array, t+i, t+next, 1, true, false);

					val  = next;
					next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;
				}
				while(next != i);

				Writes.arraycopy(array, a-bLen, array, a+val*bLen, bLen, 1, true, false);
			}
		}
	}
	private void blockMerge(int[] array, int[] tmp, int a, int m, int b, int tl, int tLen, int t, int tIdx, int bp1, int bp2, int bLen) {
		if(b-m <= bLen) {
			Highlights.clearMark(2);
			this.mergeBWExt(array, tmp, a, m, b);
			return;
		}

		this.insertTo(array, t+tl-1, t);

		int i = a+bLen-1, j = m+bLen-1, ti = t, tj = t+tl, tp = tIdx;

		while(ti < t+tl && tj < t+tLen) {
			if(Reads.compareValues(array[i], array[j]) <= 0) {
				Writes.swap(array, tp++, ti++, 1, true, false);
				i += bLen;
			}
			else {
				Writes.swap(array, tp++, tj++, 1, true, false);
				Writes.swap(array, bp1, bp2, 1, true, false);
				j += bLen;
			}
			bp1++; bp2++;
		}
		while(ti < t+tl) {
			Writes.swap(array, tp++, ti++, 1, true, false);
			bp1++; bp2++;
		}
		while(tj < t+tLen) {
			Writes.swap(array, tp++, tj++, 1, true, false);
			Writes.swap(array, bp1++, bp2++, 1, true, false);
		}
		t ^= tIdx; tIdx ^= t; t ^= tIdx;

		MaxHeapSort sortHeap = new MaxHeapSort(this.arrayVisualizer);
		sortHeap.customHeapSort(array, tIdx, tIdx+tLen, 1);

		Writes.arraycopy(array, m-bLen, tmp, 0, bLen, 1, true, true);
		Writes.arraycopy(array, a, array, m-bLen, bLen, 1, true, false);

		this.blockCycle(array, a+bLen, t, tIdx, tLen, bLen);
		this.multiSwap(array, t, tIdx, tLen);

		bp1 -= tLen; bp2 -= tLen;

		int f = a+bLen, a1 = f, bp3 = bp2+tLen;

		boolean rev = Reads.compareIndices(array, bp1, bp2, 1, true) > 0;

		while(true) {
			do {
				if(rev) Writes.swap(array, bp1, bp2, 1, true, false);

				bp1++; bp2++;
				a1 += bLen;
			}
			while(bp2 < bp3 && Reads.compareIndices(array, bp1, bp2, 1, true) == (rev ? 1 : -1));

			if(bp2 == bp3) {
				this.smartTailMerge(array, tmp, f-bLen, f, rev ? f : a1, b);
				return;
			}
			f = this.smartMerge(array, f-bLen, f, a1, rev);
			rev = !rev;
		}
	}

	private void blockCycleEasy(int[] array, int a, int t, int tIdx, int tLen, int bLen) {
		for(int i = 0; i < tLen-1; i++) {
			if(Reads.compareValues(array[t+i], array[tIdx+i]) > 0 ||
			   (i > 0 && Reads.compareValues(array[t+i], array[tIdx+i-1]) < 0)) {

				int next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;

				do {
					this.multiSwap(array, a+i*bLen, a+next*bLen, bLen);
					Writes.swap(array, t+i, t+next, 1, true, false);

					next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;
				}
				while(next != i);
			}
		}
	}
	private int inPlaceMergeBW(int[] array, int a, int m, int b, boolean rev) {
		int f = rev ? this.rightBinSearch(array, m, b, array[m-1])
			        : this.leftBinSearch(array, m, b, array[m-1]);
		b = f;

		while(b > m && m > a) {
			int i = rev ? this.leftBinSearch(array, a, m, array[b-1])
			            : this.rightBinSearch(array, a, m, array[b-1]);

			this.rotate(array, i, m, b);

			int t = m-i;
			m = i;
			b -= t+1;

			if(m == a) break;

			b = rev ? this.rightBinSearch(array, m, b, array[m-1])
			        : this.leftBinSearch(array, m, b, array[m-1]);
		}

		return f;
	}
	private void blockMergeEasy(int[] array, int a, int m, int b, int lenA, int lenB, int tl, int tLen, int t, int tIdx, int bp1, int bp2, int bLen) {
		if(b-m <= bLen) {
			this.inPlaceMergeBW(array, a, m, b, false);
			return;
		}

		int a1 = a+lenA, b1 = b-lenB;

		int i = a1+bLen-1, j = m+bLen-1, ti = tIdx, tj = tIdx+tl, tp = t;

		while(ti < tIdx+tl && tj < tIdx+tLen) {
			if(Reads.compareValues(array[i], array[j]) <= 0) {
				Writes.swap(array, ti++, tp++, 1, true, false);
				i += bLen;
			}
			else {
				Writes.swap(array, tj++, tp++, 1, true, false);
				Writes.swap(array, bp1, bp2, 1, true, false);
				j += bLen;
			}
			bp1++; bp2++;
		}
		while(ti < tIdx+tl) {
			Writes.swap(array, ti++, tp++, 1, true, false);
			bp1++; bp2++;
		}
		while(tj < tIdx+tLen) {
			Writes.swap(array, tj++, tp++, 1, true, false);
			Writes.swap(array, bp1++, bp2++, 1, true, false);
		}
		t ^= tIdx; tIdx ^= t; t ^= tIdx;

		MaxHeapSort sortHeap = new MaxHeapSort(this.arrayVisualizer);
		sortHeap.customHeapSort(array, tIdx, tIdx+tLen, 1);

		this.blockCycleEasy(array, a1, t, tIdx, tLen, bLen);
		this.multiSwap(array, t, tIdx, tLen);

		bp1 -= tLen; bp2 -= tLen;

		int f = a1, a2 = f, bp3 = bp2+tLen;

		boolean rev = Reads.compareIndices(array, bp1, bp2, 1, true) > 0;

		while(true) {
			do {
				if(rev) Writes.swap(array, bp1, bp2, 1, true, false);

				bp1++; bp2++;
				a2 += bLen;
			}
			while(bp2 < bp3 && Reads.compareIndices(array, bp1, bp2, 1, true) == (rev ? 1 : -1));

			if(bp2 == bp3) {
				if(!rev) this.inPlaceMergeBW(array, a1, b1, b, false);
				this.inPlaceMerge(array, a, a1, b);

				return;
			}
			f = this.inPlaceMergeBW(array, f, a2, a2+bLen, rev);
			rev = !rev;
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		int a = 0, b = currentLength, n = b-a;

		if(n < 128) {
			if(n < 32) this.binaryInsertion(array, a, b);

			else {
				FifthMergeSort smallSort = new FifthMergeSort(this.arrayVisualizer);
				smallSort.fifthMergeSort(array, n);
			}

			return;
		}

		int cbrt = 2*ceilCbrt(n/4), bLen = 2*cbrt;
		int kLen = this.calcKeys(bLen, n);

		int[] tmp = Writes.createExternalArray(bLen);

		int keys = this.findKeys(array, tmp, a, b, 2*kLen, cbrt);

		if(keys < 8) { //need at least 8 keys to perform a block merge
			for(int j = 1; j < n; j *= 2)
				for(int i = a+j; i < b; i += 2*j)
					this.inPlaceMergeBW(array, i-j, i, Math.min(i+j, b), false);

			return;
		}
		else if(keys < 2*kLen) {
			keys -= keys%4;
			kLen = keys/2;
		}

		//bit buffer length always equal to key buffer length
		int a1 = a+keys, a2 = a1+keys, bSep = this.findBits(array, tmp, a1, b, kLen, cbrt);

		if(bSep == -1) { //if we cant find enough bits we dont have to sort
			this.laziestSortExt(array, tmp, a, a2);
			this.inPlaceMerge(array, a, a2, b);

			return;
		}

		//[a][ keys ][a1][ bits ][a2][extbuf][a3][main sequence][b]
		int a3 = a2+bLen, i, j = 1; n = b-a3;

		//advanced build blocks
		this.binaryInsertion(array, a2, a3);
		Writes.arraycopy(array, a2, tmp, 0, bLen, 1, true, true);

		for(; j < cbrt; j *= 2) {
			int p = Math.max(2, j);

			for(i = a3; i+2*j < b; i += 2*j)
				this.mergeWithBufFWExt(array, i, i+j, i+2*j, i-p);

			if(i+j < b) this.mergeWithBufFWExt(array, i, i+j, b, i-p);
			else		this.shiftFWExt(array, i-p, i, b);

			a3 -= p; b -= p;
		}

		i = b-n%(2*j);

		if(i+j < b) this.mergeWithBufBWExt(array, i, i+j, b, b+j);
		else 		this.shiftBWExt(array, i, b, b+j);

		for(i -= 2*j; i >= a3; i -= 2*j)
			this.mergeWithBufBWExt(array, i, i+j, i+2*j, i+3*j);

		a3 += j; b += j; j *= 2;

		for(i = a3; i+2*j < b; i += 2*j)
			this.mergeWithBufFWExt(array, i, i+j, i+2*j, i-j);

		if(i+j < b) this.mergeWithBufFWExt(array, i, i+j, b, i-j);
		else		this.shiftFWExt(array, i-j, i, b);

		a3 -= j; b -= j; j *= 2;

		i = b-n%(2*j);

		if(i+j < b) this.dualMergeBWExt(array, i, i+j, b, b+j/2);
		else 		this.shiftBWExt(array, i, b, b+j/2);

		for(i -= 2*j; i >= a3; i -= 2*j)
			this.dualMergeBWExt(array, i, i+j, i+2*j, i+2*j+j/2);

		a3 += j/2; b += j/2; j *= 2;

		//advanced build blocks (in-place using keys)
		if(keys >= j) {
			this.rotate(array, a, a1, a3);
			a2 = a1+bLen;

			if(kLen >= j) {
				for(int mLvl = 2*j; j < kLen; j *= 2) {
					int p = Math.max(mLvl, j);

					for(i = a3; i+2*j < b; i += 2*j)
						this.mergeWithBufFW(array, i, i+j, i+2*j, i-p);

					if(i+j < b) this.mergeWithBufFW(array, i, i+j, b, i-p);
					else		this.shiftFW(array, i-p, i, b);

					a3 -= p; b -= p;
				}

				i = b-n%(2*j);

				if(i+j < b) this.mergeWithBufBW(array, i, i+j, b, b+j);
				else 		this.shiftBW(array, i, b, b+j);

				for(i -= 2*j; i >= a3; i -= 2*j)
					this.mergeWithBufBW(array, i, i+j, i+2*j, i+3*j);

				a3 += j; b += j; j *= 2;
			}
			if(keys >= j) {
				for(i = a3; i+2*j < b; i += 2*j)
					this.mergeWithBufFW(array, i, i+j, i+2*j, i-j);

				if(i+j < b) this.mergeWithBufFW(array, i, i+j, b, i-j);
				else		this.shiftFW(array, i-j, i, b);

				a3 -= j; b -= j; j *= 2;

				i = b-n%(2*j);

				if(i+j < b) this.dualMergeBW(array, i, i+j, b, b+j/2);
				else 		this.shiftBW(array, i, b, b+j/2);

				for(i -= 2*j; i >= a3; i -= 2*j)
					this.dualMergeBW(array, i, i+j, i+2*j, i+2*j+j/2);

				a3 += j/2; b += j/2; j *= 2;
			}

			this.rotate(array, a, a2, a3);
			a2 = a1+keys;

			MaxHeapSort sortHeap = new MaxHeapSort(this.arrayVisualizer);
			sortHeap.customHeapSort(array, a, a1, 1);
		}
		Writes.arraycopy(tmp, 0, array, a2, bLen, 1, true, false);

		//main block merge
		this.unshuffle(array, a, a1);
		int limit = bLen*(kLen+2);

		for(int k = j/bLen-1; j < n && Math.min(2*j, n) <= limit; j *= 2, k = 2*k+1) {
			for(i = a3; i+2*j <= b; i += 2*j)
				this.blockMerge(array, tmp, i, i+j, i+2*j, k, 2*k, a, a+kLen, a1, a1+kLen, bLen);

			if(i+j < b)
				this.blockMerge(array, tmp, i, i+j, b, k, (b-i-1)/bLen-1, a, a+kLen, a1, a1+kLen, bLen);
		}

		//in-place block merge
		for(; j < n; j *= 2) {
			bLen = (2*j)/kLen;
			int lenA = j%bLen, lenB = lenA;

			for(i = a3; i+2*j <= b; i += 2*j)
				this.blockMergeEasy(array, i, i+j, i+2*j, lenA, lenB, kLen/2, kLen, a, a+kLen, a1, a1+kLen, bLen);

			if(i+j < b)
				this.blockMergeEasy(array, i, i+j, b, lenA, (b-i-j)%bLen, kLen/2, kLen/2+(b-i-j)/bLen, a, a+kLen, a1, a1+kLen, bLen);
		}

		//cleaning up
		this.multiSwap(array, a1+bSep, a1+kLen+bSep, kLen-bSep); //restore bit buffer initial position
		this.laziestSortExt(array, tmp, a, a3);
		this.redistBuffer(array, tmp, a, a3, b);

		Writes.deleteExternalArray(tmp);
    }
}

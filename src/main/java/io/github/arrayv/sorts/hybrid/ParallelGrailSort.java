package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
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

public final class ParallelGrailSort extends Sort {
    public ParallelGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Parallel Grail");
        this.setRunAllSortsName("Parallel Grail Sort");
        this.setRunSortName("Parallel Grailsort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int[] array;

	private class GrailCommonSort extends Thread {
		private int a, b, nKeys;

		GrailCommonSort(int a, int b, int nKeys) {
			this.a = a;
			this.b = b;
			this.nKeys = nKeys;
		}
		public void run() {
			ParallelGrailSort.this.grailCommonSort(a, b, nKeys);
		}
	}

	private class LazyStableSort extends Thread {
		private int a, b;

		LazyStableSort(int a, int b) {
			this.a = a;
			this.b = b;
		}
		public void run() {
			ParallelGrailSort.this.lazyStableSort(a, b);
		}
	}

	private class RedistFW extends Thread {
		private int a, m, b;

		RedistFW(int a, int m, int b) {
			this.a = a;
			this.m = m;
			this.b = b;
		}
		public void run() {
			ParallelGrailSort.this.redistFW(a, m, b);
		}
	}
	private class RedistBW extends Thread {
		private int a, m, b;

		RedistBW(int a, int m, int b) {
			this.a = a;
			this.m = m;
			this.b = b;
		}
		public void run() {
			ParallelGrailSort.this.redistBW(a, m, b);
		}
	}

	private int sqrt(int n) {
		int a = 0, b = Math.min(46341, n);

		while(a < b) {
			int m = (a+b)/2;

			if(m*m >= n) b = m;
			else         a = m+1;
		}

		return a;
	}

	private void shiftFW(int a, int m, int b) {
		while(m < b) Writes.swap(array, a++, m++, 1, true, false);
	}
	private void shiftBW(int a, int m, int b) {
		while(m > a) Writes.swap(array, --b, --m, 1, true, false);
	}

	private void multiSwap(int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}

    private void rotate(int a, int m, int b) {
        int l = m-a, r = b-m;

        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }

    private void insertTo(int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, temp, 0.5, true, false);
	}

	private int leftBinSearch(int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;

			if(Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}
    private int rightBinSearch(int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;

			if(Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}

	private void binaryInsertion(int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(i, this.rightBinSearch(a, i, array[i]));
    }

	private int mergeFW(int p, int a, int m, int b, boolean fwEq) {
		int i = a, j = m;

		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) < (fwEq ? 1 : 0))
				Writes.swap(array, p++, i++, 1, true, false);

			else Writes.swap(array, p++, j++, 1, true, false);
		}

		int f = i < m ? i : j;
		if(i < m && p < i) this.shiftFW(p, i, m);

		return f;
	}

	private int inPlaceMergeFW(int a, int m, int b, boolean fwEq) {
		int i = a, j = m, k;

		while(i < j && j < b) {
			if(Reads.compareValues(array[i], array[j]) > (fwEq ? 0 : -1)) {
				k = fwEq ? this.leftBinSearch(j+1, b, array[i])
						 : this.rightBinSearch(j+1, b, array[i]);

				this.rotate(i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}

		return i;
	}
	private void inPlaceMergeBW(int a, int m, int b, boolean fwEq) {
		int i = m-1, j = b-1, k;

		while(j > i && i >= a){
			if(Reads.compareValues(array[i], array[j]) > (fwEq ? 0 : -1)) {
				k = fwEq ? this.rightBinSearch(a, i, array[j])
						 : this.leftBinSearch(a, i, array[j]);

				this.rotate(k, i+1, j+1);

				j -= (i+1)-k;
				i = k-1;
			}
			else j--;
		}
	}

	private int findKeys(int a, int b, int n) {
		int p = a, nKeys = 1, pEnd = a+nKeys;

		Highlights.clearMark(2);
		for(int i = pEnd; i < b && nKeys < n; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			int loc = this.leftBinSearch(p, pEnd, array[i]);

			if(pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
				this.rotate(p, pEnd, i);
				int inc = i-pEnd;
				loc  += inc;
				p    += inc;
				pEnd += inc;

				this.insertTo(pEnd, loc);
				nKeys++;
				pEnd++;
			}
		}
		this.rotate(a, p, pEnd);
		return nKeys;
	}

	private void blockSelect(int a, int b, int t, int bLen) {
		for(int j = a; j < b; j += bLen) {
			int min = j;

			for(int i = min+bLen; i < b; i += bLen) {
				int cmp = Reads.compareValues(array[i], array[min]);

				if(cmp < 0 || (cmp == 0 && Reads.compareValues(array[t+(i-a)/bLen], array[t+(min-a)/bLen]) < 0))
					min = i;
			}

			if(min != j) {
				this.multiSwap(j, min, bLen);
				Writes.swap(array, t+(j-a)/bLen, t+(min-a)/bLen, 1, true, false);
			}
		}
	}

	private void blockMerge(int t, int a, int m, int b, int bLen) {
		int a1 = a+(m-a)%bLen;
		int b1 = b-(b-m)%bLen;
		int i = a1, l = i-bLen, r = m;

		int mKey = array[t+(m-i)/bLen];
		int f = a;
		boolean frag = true;

		this.blockSelect(a1, b1, t, bLen);

		while(l < m && r < b1) {
			boolean curr = Reads.compareValues(array[t++], mKey) < 0;

			if(frag != curr) {
				f = this.mergeFW(f-bLen, f, i, i+bLen, frag);

				if(f < i) {
					this.shiftBW(f, i, i+bLen);
					f += bLen;
				}
				else frag = curr;

				if(frag) r += bLen;
				else     l += bLen;
			}
			else {
				this.shiftFW(f-bLen, f, i);
				f = i;

				if(frag) l += bLen;
				else     r += bLen;
			}
			i += bLen;
		}

		if(l < m) {
			f = this.mergeFW(f-bLen, f, b1, b, true);
			if(f >= b1) this.shiftFW(f-bLen, f, b);
		}
		else this.shiftFW(f-bLen, f, b);
	}
	private void blockMergeFewKeys(int t, int a, int m, int b, int bLen) {
		int a1 = a+(m-a)%bLen;
		int b1 = b-(b-m)%bLen;
		int i = a1, l = i-bLen, r = m;

		int mKey = array[t+(m-i)/bLen];
		int f = a;
		boolean frag = true;

		this.blockSelect(a1, b1, t, bLen);

		while(l < m && r < b1) {
			boolean curr = Reads.compareValues(array[t++], mKey) < 0;

			if(frag != curr) {
				boolean tmp = frag;

				if(f == i || Reads.compareValues(array[i-1], array[i+bLen-1]) < (frag ? 1 : 0))
					frag = curr;

				f = this.inPlaceMergeFW(f, i, i+bLen, tmp);

				if(frag) r += bLen;
				else     l += bLen;
			}
			else {
				f = i;

				if(frag) l += bLen;
				else     r += bLen;
			}
			i += bLen;
		}

		if(l < m) this.inPlaceMergeBW(f, b1, b, true);
	}

	private void redistFW(int a, int m, int b) {
		this.binaryInsertion(a, m);
		this.inPlaceMergeFW(a, m, b, true);
	}
	private void redistBW(int a, int m, int b) {
		this.binaryInsertion(m, b);
		this.inPlaceMergeBW(a, m, b, false);
	}

	private void lazyStableSort(int a, int b) {
		if(b-a <= 16) {
			this.binaryInsertion(a, b);
			return;
		}

		int m = (a+b)/2;

		LazyStableSort left  = new LazyStableSort(a, m);
		LazyStableSort right = new LazyStableSort(m, b);
		left.start();
		right.start();

		try {
			left.join();
			right.join();
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		this.inPlaceMergeFW(a, m, b, true);
	}

	private void grailCommonSort(int a, int b, int nKeys) {
		int len  = b-a;

		if(len <= 16) {
			this.binaryInsertion(a, b);
			return;
		}

		int bLen = sqrt(len);
		int tLen = len/bLen;

		int idl  = bLen+tLen;
		boolean strat1 = nKeys >= idl;
		if(!strat1) idl = nKeys;

		int keys = this.findKeys(a, b, idl);
		int a1   = a+keys;
		int m    = (a1+b)/2;

		if(strat1 && keys == idl) {
			GrailCommonSort left  = new GrailCommonSort(a1, m, keys);
			GrailCommonSort right = new GrailCommonSort(m, b, keys);
			left.start();
			right.start();

			try {
				left.join();
				right.join();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			this.blockMerge(a, a1, m, b, bLen);

			m = this.leftBinSearch(a+tLen, b-bLen, array[a+tLen-1]);

			RedistFW kBuf = new RedistFW(a, a+tLen, m);
			RedistBW mBuf = new RedistBW(m, b-bLen, b);
			kBuf.start();
			mBuf.start();

			try {
				kBuf.join();
				mBuf.join();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		else if(keys > 4) {
			bLen = (b-a1-1)/(keys-keys%2)+1;

			GrailCommonSort left  = new GrailCommonSort(a1, m, keys);
			GrailCommonSort right = new GrailCommonSort(m, b, keys);
			left.start();
			right.start();

			try {
				left.join();
				right.join();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			this.blockMergeFewKeys(a, a1, m, b, bLen);
			this.redistFW(a, a1, b);
		}
		else if(keys > 1) this.lazyStableSort(a, b);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
		this.grailCommonSort(0, length, 46341);
    }
}

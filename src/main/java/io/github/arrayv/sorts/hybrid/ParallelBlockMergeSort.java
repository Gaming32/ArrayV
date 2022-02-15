package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
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

public final class ParallelBlockMergeSort extends Sort {
    public ParallelBlockMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Parallel Block Merge");
        this.setRunAllSortsName("Parallel Block Merge Sort");
        this.setRunSortName("Parallel Block Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int[] array;

	private class BlockMergeSort extends Thread {
		private int a, b;

		BlockMergeSort(int a, int b) {
			this.a = a;
			this.b = b;
		}
		public void run() {
			ParallelBlockMergeSort.this.blockMergeSort(a, b);
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

	private void inPlaceMergeFW(int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b) {
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = j;
				while(++k < b && Reads.compareIndices(array, i, k, 0, false) > 0);

				this.rotate(i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}
	private void inPlaceMergeBW(int a, int m, int b) {
		int i = m-1, j = b-1, k;

		while(j > i && i >= a){
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = i;
				while(--k >= a && Reads.compareIndices(array, k, j, 0, false) > 0);

				this.rotate(k+1, i+1, j+1);

				j -= i-k;
				i = k;
			}
			else j--;
		}
	}

	private void mergeFW(int p, int a, int m, int b) {
		int len2 = m-a, pEnd = p+len2;

		this.multiSwap(p, a, len2);

		while(p < pEnd && m < b) {
			if(Reads.compareValues(array[p], array[m]) <= 0)
				Writes.swap(array, a++, p++, 1, true, false);

			else Writes.swap(array, a++, m++, 1, true, false);
		}
		while(p < pEnd)
			Writes.swap(array, a++, p++, 1, true, false);
	}
	private void mergeBW(int p, int a, int m, int b) {
		int len2 = b-m, pEnd = p+len2-1;

		this.multiSwap(p, m, len2);

		m--;
		while(pEnd >= p && m >= a) {
			if(Reads.compareValues(array[pEnd], array[m]) >= 0)
				Writes.swap(array, --b, pEnd--, 1, true, false);

			else Writes.swap(array, --b, m--, 1, true, false);
		}
		while(pEnd >= p)
			Writes.swap(array, --b, pEnd--, 1, true, false);
	}

	private int findKeys(int a, int b, int n) {
		int p = a, found = 1, pEnd = p+found, i = pEnd;

		while(found < n) {
			while(i < b && Reads.compareIndices(array, pEnd-1, i, 1, true) == 0) i++;
			if(i == b) break;

			this.rotate(p, pEnd, i);

			p += i-pEnd;
			pEnd = ++i;

			found++;
		}
		this.rotate(a, p, pEnd);

		return found;
	}

	private int selectMin(int a, int b, int bLen) {
		int min = a;
		for(int i = min+bLen; i < b; i += bLen)
			if(Reads.compareValues(array[i], array[min]) < 0)
				min = i;

		return min;
	}

	private void blockMerge(int a, int m, int b) {
		if(Reads.compareValues(array[m-1], array[m]) <= 0) return;

		else if(Reads.compareValues(array[a], array[b-1]) > 0) {
			this.rotate(a, m, b);
			return;
		}

		int len1 = m-a;

		int bLen = this.sqrt(len1);
		int tLen = len1/bLen;
		int idl  = bLen+tLen;

		int keys = this.findKeys(a, m, idl);
		int a1 = a+keys;
		len1 -= keys;

		if(keys == idl) {
			int b1 = b-(b-m)%bLen;

			int t  = a;
			int p  = a+tLen;

			int i  = a1+(len1-1)%bLen+1;

			for(int j = i, k = t; j < m; j += bLen, k++)
				Writes.swap(array, j, k, 10, true, false);

			while(i < m && m < b1) {
				if(Reads.compareValues(array[i-1], array[m+bLen-1]) > 0) {
					this.multiSwap(i, m, bLen);
					this.mergeBW(p, a1, i, i+bLen);

					m += bLen;
				}
				else {
					int min = this.selectMin(i, m, bLen);

					if(min != i) this.multiSwap(i, min, bLen);
					Writes.swap(array, t++, i, 1, true, false);
				}
				i += bLen;
			}
			if(i < m) {
				do {
					int min = this.selectMin(i, m, bLen);

					this.multiSwap(i, min, bLen);
					Writes.swap(array, t++, i, 1, true, false);
					i += bLen;
				}
				while(i < m);

				this.mergeBW(p, a1, b1, b);
			}
			else {
				while(m < b1 && Reads.compareValues(array[m-bLen], array[m]) > 0) {
					this.mergeBW(p, a1, m, m+bLen);
					m += bLen;
				}
				if(m == b1) this.mergeBW(p, a1, b1, b);
				else        this.mergeFW(p, m-bLen+1, m, b);
			}

			BinaryInsertionSort insertion = new BinaryInsertionSort(this.arrayVisualizer);
			insertion.customBinaryInsert(array, p, a1, 0.5);
			this.inPlaceMergeFW(a, a+keys, b);
		}
		else if(keys > 1) {
			bLen = (len1-1)/keys+1;
			int b1 = b-(b-m)%bLen;

			int t  = a;
			int i  = a1+(len1-1)%bLen+1;

			for(int j = i, k = t; j < m; j += bLen, k++)
				Writes.swap(array, j, k, 10, true, false);

			while(i < m && m < b1) {
				if(Reads.compareValues(array[i-1], array[m+bLen-1]) > 0) {
					this.multiSwap(i, m, bLen);
					this.inPlaceMergeBW(a1, i, i+bLen);

					m += bLen;
				}
				else {
					int min = this.selectMin(i, m, bLen);

					if(min != i) this.multiSwap(i, min, bLen);
					Writes.swap(array, t++, i, 1, true, false);
				}
				i += bLen;
			}
			if(i < m) {
				do {
					int min = this.selectMin(i, m, bLen);

					this.multiSwap(i, min, bLen);
					Writes.swap(array, t++, i, 1, true, false);
					i += bLen;
				}
				while(i < m);

				this.inPlaceMergeBW(a1, b1, b);
			}
			else {
				while(m < b1 && Reads.compareValues(array[m-bLen], array[m]) > 0) {
					this.inPlaceMergeBW(a1, m, m+bLen);
					m += bLen;
				}
				if(m == b1) this.inPlaceMergeBW(a1, b1, b);
				else        this.inPlaceMergeFW(m-bLen+1, m, b);
			}
			this.inPlaceMergeFW(a, a+keys, b);
		}
		else this.inPlaceMergeFW(a, m, b);
	}

	private void blockMergeSort(int a, int b) {
		if(b-a < 32) {
			BinaryInsertionSort insertion = new BinaryInsertionSort(this.arrayVisualizer);
			insertion.customBinaryInsert(array, a, b, 0.5);

			return;
		}

		int m = a+(b-a)/2;

		BlockMergeSort left  = new BlockMergeSort(a, m);
		BlockMergeSort right = new BlockMergeSort(m, b);
		left.start();
		right.start();

		try {
			left.join();
			right.join();
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		this.blockMerge(a, m, b);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
		this.blockMergeSort(0, length);
    }
}

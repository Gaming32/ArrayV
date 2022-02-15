package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

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

public final class LazierestSort extends Sort {
    public LazierestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazierest Stable");
        this.setRunAllSortsName("Lazierest Stable Sort");
        this.setRunSortName("Lazierest Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int ceilCbrt(int n) {
		int a = 0, b = Math.min(1291, n);

		while(a < b) {
			int m = (a+b)/2;

			if(m*m*m >= n) b = m;
			else           a = m+1;
		}

		return a;
	}

	private void insertTo(int[] array, int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, temp, 0.5, true, false);
	}

	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}

    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;

        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(array, m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }

    private int leftBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;

			if(Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}
	private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;

			if(Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+1;
		}

		return a;
	}

    private int leftExpSearch(int[] array, int a, int b, int val) {
    	int i = 1;
    	while(a-1+i < b && Reads.compareValues(val, array[a-1+i]) > 0) i *= 2;

    	return this.leftBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
    }
	private int rightExpSearch(int[] array, int a, int b, int val) {
    	int i = 1;
    	while(b-i >= a && Reads.compareValues(val, array[b-i]) < 0) i *= 2;

    	return this.rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
    }

    private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }

    private void inPlaceMergeFW(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b){
			if(Reads.compareValues(array[i], array[j]) == 1) {
				k = this.leftExpSearch(array, j+1, b, array[i]);
				IndexedRotations.cycleReverse(array, i, j, k, 0.75, true, false);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}
	private void inPlaceMergeBW(int[] array, int a, int m, int b) {
		int i = m-1, j = b-1, k;

		while(j > i && i >= a){
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = this.rightExpSearch(array, a, i, array[j]);
				this.rotate(array, k, i+1, j+1);

				j -= (i+1)-k;
				i = k-1;
			}
			else j--;
		}
	}
	private void inPlaceMerge(int[] array, int a, int m, int b) {
		if(b-m < m-a) this.inPlaceMergeBW(array, a, m, b);
		else          this.inPlaceMergeFW(array, a, m, b);
	}

	private void fragmentedMerge(int[] array, int a, int m, int b, int s) {
		int i = a+(m-a)%s;

		while(i < m) {
			int j = this.leftExpSearch(array, m, b, array[i]);
			IndexedRotations.cycleReverse(array, i, m, j, 0.75, true, false);

			int rLen = j-m;
			j = i;
			i += rLen;
			m += rLen;

			this.inPlaceMerge(array, a, j, i);
			a = i;
			i += s;
		}
		this.inPlaceMerge(array, Math.max(a, i-s), i, b);
	}

	public void lazierestStableSort(int[] array, int a, int b) {
		int len = b-a, s = this.ceilCbrt(len), s1 = s*s;

		for(int i = len%s; i <= b; i += s)
			this.binaryInsertion(array, Math.max(a, i-s), i);

		for(int i = b-s, j = b; i > a; i -= s) {
			if(j-i == s1) {
				j -= s1;
				i -= s;
			}
			this.inPlaceMergeFW(array, Math.max(a, i-s), i, j);
		}

		for(int i = b-s1; i > a; i -= s1)
			this.fragmentedMerge(array, Math.max(a, i-s1), i, b, s);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		if(currentLength <= 16) this.binaryInsertion(array, 0, currentLength);
    	else                    this.lazierestStableSort(array, 0, currentLength);
    }
}

package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 aphitorite

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

public final class ImprovedBlockSelectionSort extends Sort {
    public ImprovedBlockSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Improved Block Selection Merge");
        this.setRunAllSortsName("Improved Block Selection Merge Sort");
        this.setRunSortName("Improved Block Selection Merge Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int sqrt(int n) {
    	int i = 1;
    	for(; i*i < n; i*=2);
    	return i;
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

	private int inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b) {
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = j;
				do k++;
				while(k < b && Reads.compareIndices(array, i, k, 0, false) > 0);

				this.rotate(array, i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}

		return i;
	}

	private void inPlaceMergeBW(int[] array, int a, int m, int b) {
		int i = m-1, j = b-1, k;

		while(j > i && i >= a){
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = i;
				do k--;
				while(k >= a && Reads.compareIndices(array, k, j, 0, false) > 0);

				this.rotate(array, k+1, i+1, j+1);

				j -= i-k;
				i = k;
			}
			else j--;
		}
	}

	private int selectRange(int[] array, int a, int b, int bLen) {
		int min = a;
		a += bLen;

		while(a < b) {
			int comp = Reads.compareIndices(array, a, min, 0, false);

			if(comp == -1 || (comp == 0 && Reads.compareIndices(array, a+bLen-1, min+bLen-1, 0, false) == -1))
				min = a;

			a += bLen;
		}
		return min;
	}

    private void blockSelect(int[] array, int a, int m, int b, int bLen) {
		int k = a, j = m;

		while(k < m && Reads.compareIndices(array, k, m, 0.5, true) <= 0) k += bLen;
		if(k == m) return;

		int i = m;
		this.multiSwap(array, k, j, bLen);
		k += bLen;
		j += bLen;

		while(k < j && j < b) {
			if(Reads.compareIndices(array, i, j, 0.5, true) <= 0) {
				if(k != i) this.multiSwap(array, k, i, bLen);
				k += bLen;
				i = this.selectRange(array, Math.max(m, k), j, bLen);
			}
			else {
				if(i == k) i = j;
				if(k != j) this.multiSwap(array, k, j, bLen);
				k += bLen;
				j += bLen;
			}
		}
		//if(k != m)
			while(k < j) {
				i = this.selectRange(array, k, b, bLen);
				if(k != i) this.multiSwap(array, k, i, bLen);
				k += bLen;
			}
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	for(int i, j = 1; j < length; j *= 2) {
			int bLen = sqrt(j), n = j,
				b = length-length%bLen;

			while(n > 16) {
				for(i = 0; i+j < b; i += 2*j)
					for(int k = i; k+n < Math.min(i+2*j, b); k += n)
						this.blockSelect(array, k, k+n, Math.min(k+2*n, b), bLen);

				n = bLen;
				bLen = sqrt(bLen);
			}

			for(i = 0; i+j < b; i += 2*j)
				for(int k = i, f = i; k+n < Math.min(i+2*j, b); k += n)
					f = this.inPlaceMerge(array, f, k+n, Math.min(k+2*n, b));

			this.inPlaceMergeBW(array, length-length%(2*j), b, length);
    	}
    }
}

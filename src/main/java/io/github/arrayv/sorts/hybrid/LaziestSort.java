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

public final class LaziestSort extends Sort {
    public LaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Laziest Stable");
        this.setRunAllSortsName("Laziest Stable Sort");
        this.setRunSortName("Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }

    private void inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while(i < j && j < b){
			if(Reads.compareValues(array[i], array[j]) == 1) {
				k = this.leftExpSearch(array, j+1, b, array[i]);
				this.rotate(array, i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}

    protected void laziestStableSort(int[] array, int start, int end) {
		int len = end - start;
		if(len <= 16) {
			this.binaryInsertion(array, start, end);
			return;
		}

		int i, blockLen = Math.max(16, (int)Math.sqrt(len));
		for(i = start; i+2*blockLen < end; i+=blockLen) {
			this.binaryInsertion(array, i, i+blockLen);
		}
		this.binaryInsertion(array, i, end);

		while(i-blockLen >= start) {
			this.inPlaceMerge(array, i-blockLen, i, end);
			i-=blockLen;
		}
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.laziestStableSort(array, 0, currentLength);
    }
}

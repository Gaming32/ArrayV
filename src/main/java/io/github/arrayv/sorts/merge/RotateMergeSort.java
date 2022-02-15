package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2020 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

public final class RotateMergeSort extends Sort {
    public RotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rotate Merge");
        this.setRunAllSortsName("Rotate Merge Sort");
        //this.setRunAllID("In-Place Merge Sort with Rotations");
        this.setRunSortName(/*"In-Place */"Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

	private int binarySearch(int[] array, int a, int b, int value, boolean left) {
		while(a < b) {
			int m = a+(b-a)/2;

			boolean comp = left ? Reads.compareValues(value, array[m]) <= 0
								: Reads.compareValues(value, array[m]) < 0;

			if(comp) b = m;
			else     a = m+1;
		}

		return a;
	}

    private void rotateMerge(int[] array, int a, int m, int b) {
		int m1, m2, m3;

		if(m-a >= b-m) {
			m1 = a+(m-a)/2;
			m2 = this.binarySearch(array, m, b, array[m1], true);
			m3 = m1+(m2-m);
		}
		else {
			m2 = m+(b-m)/2;
			m1 = this.binarySearch(array, a, m, array[m2], false);
			m3 = (m2++)-(m-m1);
		}
		this.rotate(array, m1, m, m2);

		if(m2-(m3+1) > 0 && b-m2 > 0) this.rotateMerge(array, m3+1, m2, b);
		if(m1-a > 0 && m3-m1 > 0)     this.rotateMerge(array, a, m1, m3);
	}

	protected void rotateMergeSort(int[] array, int a, int b) {
        int len = b-a, i;

        for(int j = 1; j < len; j *= 2) {
            for(i = a; i + 2*j <= b; i += 2*j)
                this.rotateMerge(array, i, i+j, i+2*j);

            if(i + j < b)
                this.rotateMerge(array, i, i+j, b);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.rotateMergeSort(array, 0, length);
    }
}

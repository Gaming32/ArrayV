package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/*
 *
The MIT License (MIT)

Copyright (c) 2021 aphitorite

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

public final class StacklessRotateMergeSort extends Sort {
    public StacklessRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stackless Rotate Merge");
        this.setRunAllSortsName("Stackless Rotate Merge Sort");
        this.setRunSortName("Stackless Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.griesMills(array, a, m, b, 0.5, true, false);
    }

	//@param c - select c smallest elements
	private void partitionMerge(int[] array, int a, int m, int b, int c) {
		int lenA = m-a, lenB = b-m;

		if(lenA < 1 || lenB < 1) return;

		if(lenB < lenA) {
			c = (lenA+lenB)-c;
			int r1 = 0, r2 = Math.min(c, lenB);

			while(r1 < r2) {
				int ml = (r1+r2)/2;

				if(Reads.compareValues(array[m-(c-ml)], array[b-ml-1]) > 0)
					r2 = ml;
				else
					r1 = ml+1;
			}
			//[lenA-(c-r1)][c-r1][lenB-r1][r1]
			//[lenA-(c-r1)][lenB-r1][c-r1][r1]
			this.rotate(array, m-(c-r1), m, b-r1);
		}
		else {
			int r1 = 0, r2 = Math.min(c, lenA);

			while(r1 < r2) {
				int ml = (r1+r2)/2;

				if(Reads.compareValues(array[a+ml], array[m+(c-ml)-1]) > 0)
					r2 = ml;
				else
					r1 = ml+1;
			}
			//[r1][lenA-r1][c-r1][lenB-(c-r1)]
			//[r1][c-r1][lenA-r1][lenB-(c-r1)]
			this.rotate(array, a+r1, m, m+(c-r1));
		}
	}

	private void rotateMerge(int[] array, int a, int b, int c) {
		int i;
		for(i = a+1; i < b && Reads.compareIndices(array, i-1, i, 0.25, true) <= 0; i++);
		if(i < b) this.partitionMerge(array, a, i, b, c);
	}

	public void rotatePartitionMergeSort(int[] array, int a, int b) {
        int len = b-a;

		for(int i = a+1; i < b; i += 2)
			if(Reads.compareIndices(array, i-1, i, 0.5, true) > 0)
				Writes.swap(array, i-1, i, 0.5, true, false);

        for(int j = 2; j < len; j *= 2) {
			int b1 = 0;

            for(int i = a; i+j < b; i += 2*j) {
				b1 = Math.min(i+2*j, b);
                this.partitionMerge(array, i, i+j, b1, j);
			}

			for(int k = j/2; k > 1; k /= 2)
				for(int i = a; i+k < b1; i += 2*k)
					this.rotateMerge(array, i, Math.min(i+2*k, b), k);

			for(int i = a+1; i < b1; i += 2)
				if(Reads.compareIndices(array, i-1, i, 0.5, true) > 0)
					Writes.swap(array, i-1, i, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.rotatePartitionMergeSort(array, 0, length);
    }
}

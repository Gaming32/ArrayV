package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class OptimizedPancakeSort extends Sort {
	public OptimizedPancakeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Optimized Pancake");
		this.setRunAllSortsName("Optimized Pancake Sort");
		this.setRunSortName("Optimized Pancake Sort");
		this.setCategory("Merge Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	//special thanks to Anonymous0726 !!!
	
	private void flip(int[] array, int idx) {
		Writes.reversal(array, 0, idx, 0.1, true, false);
	}
	
	private void cursedRotate(int[] array, int a, int m, int b) {
		this.flip(array, a-1);
		this.flip(array, m-1);
		this.flip(array, b-1);
		this.flip(array, b-m+a-1);
	}
	
	private int binarySearch(int[] array, int a, int b, int value, boolean left) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			boolean comp = left ? Reads.compareValues(value, array[m]) <= 0
								: Reads.compareValues(value, array[m]) < 0;
			
			if(comp) b = m;
			else	 a = m+1;
		}
		
		return a;
	}
	
	private void pancakeMerge(int[] array, int m, int b) {
		int m1, m2, m3;
		
		if(m >= b-m) {
			m1 = m/2;
			m2 = this.binarySearch(array, m, b, array[m1], false);
			m3 = m1+(m2-m);
		}
		else {
			m2 = m+(b-m)/2;
			m1 = this.binarySearch(array, 0, m, array[m2], true);
			m3 = (m2++)-(m-m1);
		}
		this.cursedRotate(array, m1, m, m2);
		
		if(m1 > 0 && m3 > m1) this.pancakeMerge(array, m1, m3);
		
		m3++;
		if(m2 > m3 && b > m2) {
			this.cursedRotate(array, 0, m3, b);
			this.pancakeMerge(array, m2-m3, b-m3);
			this.cursedRotate(array, 0, b-m3, b);
		}
	}
	
	private void pancakeMergeSort(int[] array, int n) {
		if(n > 1) {
			if(Reads.compareIndices(array, 0, 1, 0, true) <= 0) {
				int i = 2;
				for(; i < n && Reads.compareIndices(array, i-1, i, 0, true) <= 0; i++);

				if(i == n) return;
			}
			else {
				int i = 2;
				for(; i < n && Reads.compareIndices(array, i-1, i, 0, true) > 0; i++);

				if(i == n) {
					this.flip(array, n-1);
					return;
				}
			}
			
			int m = n/2;
			
			this.pancakeMergeSort(array, m);
			this.cursedRotate(array, 0, m, n);
			m = n-m;
			
			this.pancakeMergeSort(array, m);
			this.pancakeMerge(array, m, n);
		}
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.pancakeMergeSort(array, length);
	}
}
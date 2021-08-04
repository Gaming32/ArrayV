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

final public class StacklessRotateMergeSort extends Sort {
    public StacklessRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stackless Rotate Merge");
        this.setRunAllSortsName("Stackless Rotate Merge Sort");
        this.setRunSortName("Stackless Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int val;
	
	private void valSwap(int[] array, int i) {
		int t = array[i];
		Writes.write(array, i, this.val, 1, true, false);
		this.val = t;
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
	
	private int binSearch(int[] array, int a, int b, int value, boolean left) {
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
		int p = m, q = b;
		
		while(a < b) {
			while(p-a > 0 && q-p > 0) {
				int m1, m2, m3;
				
				if(p-a >= q-p) {
					m1 = a+(p-a)/2;
					m2 = this.binSearch(array, p, q, array[m1], true);
					m3 = m1+(m2-p);
				}
				else {
					m2 = p+(q-p)/2;
					m1 = this.binSearch(array, a, p, array[m2], false);
					m3 = (m2++)-(p-m1);
				}
				this.rotate(array, m1, p, m2);
				this.valSwap(array, m3);
				
				p = m1;
				q = m3;
			}
			Highlights.clearMark(2);
			
			this.valSwap(array, q);
			a = q+1;
			for(q = a; q < b && Reads.compareValues(this.val, array[q]) > 0; q++) {
				Highlights.markArray(1, q);
				Delays.sleep(0.5);
			}
			for(p = a+1; p < q && Reads.compareIndices(array, p-1, p, 0.5, true) <= 0; p++);
		}
	}
	
	protected void rotateMergeSort(int[] array, int a, int b) {
		this.val = b; //MAX_VALUE
        int len = b-a, i;
        
        for(int j = 1; j < len; j *= 2) {
            for(i = a; i + 2*j <= b; i += 2*j)
                this.rotateMerge(array, i, i+j, i+2*j);
            
            if(i + j < b)
                this.rotateMerge(array, i, i+j, b);
        }
    }
    
	public void sort(int[] array, int start, int end, double sleep) {
		int min = array[start], max = min;
		for(int i = start+1; i < end; i++) {
            if(array[i] < min) min = array[i];
            else if(array[i] > max) max = array[i];
        }
        
        int size = max - min + 1;
        int[] holes = new int[size];
        
        for(int i = start; i < end; i++)
            Writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, false, true);
        
        for(int i = 0, j = start; i < size; i++) {
            while(holes[i] > 0) {
                Writes.write(holes, i, holes[i] - 1, 0, false, true);
                Writes.write(array, j, i + min, sleep, true, false);
                j++;
            }
        }
	}
	
	private void test(int[] array, int a, int b) {
		int m = (a+b)/2;
		sort(array, a, m, 1);
		sort(array, m, b, 1);
		this.rotateMerge(array, a, m, b);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.val = length; //MAX_VALUE
		this.test(array, 0, length/2);
		this.test(array, length/2, length);
		this.rotateMerge(array, 0, length/2, length);
		//this.rotateMergeSort(array, 0, length);
    }
}
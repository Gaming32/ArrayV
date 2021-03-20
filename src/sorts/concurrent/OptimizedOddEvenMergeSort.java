package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class OptimizedOddEvenMergeSort extends Sort {
	public OptimizedOddEvenMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Odd-Even Merge");
        this.setRunAllSortsName("Optimized Odd-Even Merge Sort");
        this.setRunSortName("Optimized Odd-Even Mergesort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void compSwap(int[] array, int a, int b) {
    	if(Reads.compareIndices(array, a, b, 0.5, true) == 1)
    		Writes.swap(array, a, b, 0.5, true, false);
    }
    
    private void compRange(int[] array, int a, int m, int s) {
    	for(int i = s; a+i < m; i++)
    		this.compSwap(array, a+i, m+i);
    }
    
    private void compRangeExtd(int[] array, int a, int m, int p) {
    	int l = m-a;
    	
    	if(l > p) {
    		int i = a, j, d = l-p;
    		
    		for(j = 0; j < d;   j++, i++) this.compSwap(array, i, i+p);
    		for(j = 0; j < p-d; j++, i++) this.compSwap(array, i, i+l);
    		for(j = 0; j < d;   j++, i++) this.compSwap(array, i+d, i+l);
    	}
    	else this.compRange(array, a, m, 0);
    }
    
    private void merge(int[] array, int a, int b) {
    	int m, s = (b-a)%2;
    	
    	a -= s;
		m = (a+b)/2;
    	this.compRange(array, a, m, s);
    	
    	int l = b-a;
    	if(l < 4) return;
    	
    	int p;
    	for(p = 1; 2*p < l; p *= 2);
    	
    	while(p > 0) {
        	int i = a+p;
        	
        	while(i + 2*p <= m) {
        		this.compRange(array, i, i+p, 0);
        		i += 2*p;
        	}
        	this.compRangeExtd(array, i, m, p);
        	i = 2*m - i;
        			
        	while(i < b-p) {
        		this.compRange(array, i, i+p, 0);
        		i += 2*p;
        	}
    		p /= 2;
    	}
    }
    
    private void mergeSort(int[] array, int a, int b) {
    	int m = (a+b)/2;
    	
    	if(m-a > 1) this.mergeSort(array, a, m);
    	if(b-m > 1) this.mergeSort(array, m, b);
    	
    	this.merge(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
    	this.mergeSort(array, 0, sortLength);
    }
}
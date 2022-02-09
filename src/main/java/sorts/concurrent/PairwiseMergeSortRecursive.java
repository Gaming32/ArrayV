package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class PairwiseMergeSortRecursive extends Sort {
    public PairwiseMergeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pairwise Merge (Recursive)");
        this.setRunAllSortsName("Recursive Pairwise Merge Sort");
        this.setRunSortName("Recursive Pairwise Mergesort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int end;
	
	private void compSwap(int[] array, int a, int b) {
		if(b < this.end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}
	
	private void pairwiseMerge(int[] array, int a, int b) {
		int m = (a+b)/2, m1 = (a+m)/2, g = m-m1;
		
		for(int i = 0; m1+i < m; i++)
			for(int j = m1, k = g; k > 0; k >>= 1, j -= k-(i&k))
				this.compSwap(array, j+i, j+i+k);
				
		if(b-a > 4) this.pairwiseMerge(array, m, b);
	}
	
	private void pairwiseMergeSort(int[] array, int a, int b) {
		int m = (a+b)/2;
		
		for(int i = a, j = m; i < m; i++, j++)
			this.compSwap(array, i, j);
		
		if(b-a > 2) {
			this.pairwiseMergeSort(array, a, m);
			this.pairwiseMergeSort(array, m, b);
			this.pairwiseMerge(array, a, b);
		}
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
    	this.end = length;
    	int n = 1;
    	for(; n < length; n <<= 1);
		
		this.pairwiseMergeSort(array, 0, n);
    }
}
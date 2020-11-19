package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.InsertionSort;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

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

final public class ImprovedWeaveMergeSort extends Sort {
    public ImprovedWeaveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Impr. Weave Merge");
        this.setRunAllSortsName("Improved Weave Merge Sort");
        this.setRunSortName("Improved Weave Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private InsertionSort sort;
    
    public static int getMaxBit(int n) {
    	int i;
    	for(i = 0; (1 << i) <= n; i++);
    	return i - 1;
    }
    
    //will fail for b-a < 2
    private void weaveMerge(int[] array, int a, int b) {
    	int n = b-a, m = (n+1)/2;
    	
    	for(int j = 1<<(getMaxBit(n-1)-1); j >= 1; j >>= 1) {
    		int s = m > j ? 1 : 0;
    		for(int i = a+m+(1-s)*(j<<1); i+j <= b; i += j<<2)
				for(int k = 0; k < j; k++)
					Writes.swap(array, i-j+k, i+k, 1, true, false);
			m -= s*j;
    	}
    	
    	Highlights.clearMark(2);
    	this.sort.customInsertSort(array, a, b, 0.2, false);
    }

	private void weaveMergeSort(int[] array, int a, int b) {
		if(b-a > 2) {
			int m = a+(b-a+1)/2;
			this.weaveMergeSort(array, a, m);
			if(b-a > 3)
				this.weaveMergeSort(array, m, b);
		}
		this.weaveMerge(array, a, b);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.sort = new InsertionSort(this.arrayVisualizer);
        this.weaveMergeSort(array, 0, currentLength);
    }
}
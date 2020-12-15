package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.InsertionSort;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2020 yuji

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

final public class PairwiseCircleSort extends Sort {
    public PairwiseCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pairwise-Circle");
        this.setRunAllSortsName("Pairwise-Circle Sort");
        this.setRunSortName("Pairwise-Circle Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void pairs(int[] array, int left, int right, int gap) {
		if(left+gap >= right) return;
        int a = left;
        while(a + gap <= right) {
        	if(Reads.compareIndices(array, a, a+gap, 1, true) == 1) {
				Writes.swap(array, a, a+gap, 1, true, false);
			}
        	a += gap*2;
        }
        this.pairs(array, left, right, gap*2);
        this.pairs(array, left+gap, right, gap*2);
    }
	
	private void circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        while(a < b) {
        	if(Reads.compareIndices(array, a, b, 1, true) == 1) {
        		Writes.swap(array, a, b, 1, true, false);
        	}
    		a++;
    		b--;
        }
    }
	
	private void pairCircle(int[] array, int left, int right) {
		if(left >= right) return;
		int mid = (left + right) / 2;
		this.pairs(array, left, right, 1);
		this.circle(array, left, right);
		this.pairCircle(array, left, mid);
		this.pairCircle(array, mid+1, right);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	this.pairCircle(array, 0, length-1);
        InsertionSort sort = new InsertionSort(this.arrayVisualizer);
        sort.customInsertSort(array, 0, length, 0.1, false);
    }
}
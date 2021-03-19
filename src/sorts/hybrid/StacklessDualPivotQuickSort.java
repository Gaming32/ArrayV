package sorts.hybrid;

import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;
import main.ArrayVisualizer;

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

final public class StacklessDualPivotQuickSort extends Sort {
    public StacklessDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stackless Dual-Pivot Quick");
        this.setRunAllSortsName("Stackless Dual-Pivot Quicksort");
        this.setRunSortName("Stackless Dual-Pivot Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	public int partition(int[] array, int a, int b, int p) {
		int m1 = (a+a+b)/3, m2 = (a+b+b)/3;
		
		if(Reads.compareIndices(array, m1, m2, 1, true) > 0) {
			Writes.swap(array, m1, a, 1, true, false);
			Writes.swap(array, m2, --b, 1, true, false);
		}
		else {
			Writes.swap(array, m2, a, 1, true, false);
			Writes.swap(array, m1, --b, 1, true, false);
		}
		
		int i = a, j = b;
		
		for(int k = i+1; k < j; k++) {
			if(Reads.compareValues(array[k], array[b]) < 0)
				Writes.swap(array, k, ++i, 1, true, false);
				
			else if(Reads.compareValues(array[k], array[a]) >= 0) {
				do {
					j--;
					Highlights.markArray(3, j);
					Delays.sleep(1);
				}
				while(j > k && Reads.compareValues(array[j], array[a]) >= 0);
				
				Writes.swap(array, k, j, 1, true, false);
				Highlights.clearMark(3);
				
				if(Reads.compareValues(array[k], array[b]) < 0)
					Writes.swap(array, k, ++i, 1, true, false);
			}
		}
		
		Writes.swap(array, a, i, 1, true, false);
		int t = array[b];
		Writes.write(array, b, array[j], 1, true, false);
		Writes.write(array, j, array[p], 1, true, false);
		Writes.write(array, p, t, 1, true, false);
		
		return i;
    }
	
	private int leftBinSearch(int[] array, int a, int b, int p) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			if(Reads.compareIndices(array, p, m, 1, true) <= 0) 
				b = m;
			else     
				a = m+1;
		}
		
		return a;
	}
	
	private void quickSort(int[] array, int a, int b) {
		int max = a;
		for(int i = a+1; i < b; i++)
			if(Reads.compareIndices(array, i, max, 0.5, true) == 1)
				max = i;
		
		Writes.swap(array, max, --b, 1, true, false);
		int t = array[b];
		Writes.write(array, b, array[b]+1, 1, true, false);
		
		int b1 = b;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		
		do {
			while(b1-a > 24) b1 = this.partition(array, a, b1, b);
			smallSort.customBinaryInsert(array, a, b1, 0.25);
				
			a = b1+1;
			if(a >= b) {
				if(a-1 < b) Writes.swap(array, a-1, b, 1, true, false);
				Writes.write(array, b, t, 1, true, false);
				return;
			}
			
			b1 = this.leftBinSearch(array, a, b, a-1);
			Writes.swap(array, a-1, b, 1, true, false);
			
			while(a < b1 && Reads.compareIndices(array, a-1, a, 0.5, true) == 0) a++;
		}
		while(true);
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.quickSort(array, 0, length);
    }
}
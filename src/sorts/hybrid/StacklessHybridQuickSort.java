package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import main.ArrayVisualizer;

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

final public class StacklessHybridQuickSort extends Sort {
    public StacklessHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stackless Hyb. Quick");
        this.setRunAllSortsName("Stackless Hybrid Quicksort");
        this.setRunSortName("Stackless Hybrid Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private BinaryInsertionSort sort;

	private void medianOfThree(int[] array, int a, int b) {
		int m = a+(b-1-a)/2;
		if(Reads.compareIndices(array, a, m, 1, true) == 1)
			Writes.swap(array, a, m, 1, true, false);
		
		if(Reads.compareIndices(array, m, b-1, 1, true) == 1) {
			Writes.swap(array, m, b-1, 1, true, false);
			
			if(Reads.compareIndices(array, a, m, 1, true) == 1)
				return;
		}
		
		Writes.swap(array, a, m, 1, true, false);
	}
	
	private int partition(int[] array, int a, int b) {
        int i = a, j = b;
        this.medianOfThree(array, a, b);
        
		Highlights.markArray(3, a);
        while(true) {
            i++;
            while(i < b && Reads.compareIndices(array, i, a, 0, false) == -1) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }
            j--;
            while(j >= a && Reads.compareIndices(array, j, a, 0, false) == 1) {
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
                j--;
            }
            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else {
            	Writes.swap(array, a, j, 1, true, false);
            	return j;
            }
        }
    }
	
	private int valSwap(int[] array, int i, int val) {
		int temp = array[i];
		Writes.write(array, i, val, 1, true, false);
		return temp;
	}
	
	private int search(int[] array, int a, int b, int val) {
		while(a < b && Reads.compareValues(array[a], val) == -1) {
            Highlights.markArray(1, a);
            Delays.sleep(0.5);
			a++;
		}
		return a;
	}
	
	private void quickSort(int[] array, int a, int b, int t) {
		int i = a, 
			val = Reads.analyzeMax(array, b, 1, false)+1, 
			//^^^ val = MAX_VALUE; NOTE: Reads.analyzeMax() does not account for [a, b)
			end = b;
		
		while(i < b) {
			while(end-i > t) {
				int p = this.partition(array, i, end);
				end = p;
				Writes.write(array, p, array[p]+1, 1, true, false);
				val = this.valSwap(array, p, val);
			}
			this.sort.customBinaryInsert(array, i, end, 0.25);
			
			i = end+1;
			val = this.valSwap(array, end, val);
			Writes.write(array, end, array[end]-1, 1, true, false);
			end = this.search(array, i, b, val);
		}
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.sort = new BinaryInsertionSort(this.arrayVisualizer);
		this.quickSort(array, 0, length, 16);
    }
}
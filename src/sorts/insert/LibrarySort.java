package sorts.insert;

import sorts.templates.Sort;

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

final public class LibrarySort extends Sort {
    public LibrarySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

		this.setSortListName("Library");
        this.setRunAllSortsName("Library Sort");
        this.setRunSortName("Library Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	//implementation of Library Sort using a 2D array
	
	private BinaryInsertionSort binaryInsert;
	private final int GAP_SIZE = 16; //max gap size for insertions
									 //memory: O(n * GAP_SIZE)
	
	private int binarySearch(int[] array, int start, int end, int value) {
		int lo = start, hi = end;
		
		while (lo < hi) {
			int mid = lo + ((hi - lo) / 2);
			Highlights.markArray(1, mid);
			
			Delays.sleep(1);
			if (Reads.compareValues(value, array[mid]) < 0) {
				hi = mid;
			}
			else {
				lo = mid + 1;
			}
		}
		
		Highlights.clearMark(1);
		return lo;
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.binaryInsert = new BinaryInsertionSort(this.arrayVisualizer);
		
		int i = 1;
		while(i < length && Reads.compareValues(array[i-1], array[i]) < 1) { //finds increasing subsequence at start
			Highlights.markArray(1, i);
			Delays.sleep(1);
			i++;
		}
		if(i == length) return;
			
		int size = i+1;
		int[] temp     = Writes.createExternalArray(length);
		int[][] gaps   = new int[length][GAP_SIZE];
		int[] gapCount = Writes.createExternalArray(length);
		Writes.changeAllocAmount(length * GAP_SIZE);
		
		while(i < length) {
			while(i < length) { //insert into gaps
				Highlights.markArray(2, i);
				int idx = this.binarySearch(array, 0, size-1, array[i]);
				if(gapCount[idx] == GAP_SIZE) break;
				
				Writes.multiDimWrite(gaps, idx, gapCount[idx], array[i], 0, false, true);
				Writes.write(gapCount, idx, gapCount[idx]+1, 0, false, true);
				i++;
			}
			Highlights.clearMark(2);
			
			//rebalancing phase
			for(int j = 0; j < size-1; j++)
				Writes.write(temp, j, array[j], 1, true, true);
			
			for(int j = 0, k = 0, m = 0; j < size; j++) {
				for(int g = 0; g < gapCount[j]; g++)
					Writes.write(array, k++, gaps[j][g], 1, true, false);
				Writes.write(gapCount, j, 0, 0, false, true);
				
				binaryInsert.customBinaryInsert(array, m, k, 0.5); //multiple books inserted into a gap
																   //may not be in correct order so we sort
				if(j < size-1) Writes.write(array, k++, temp[j], 1, true, false);
				m = k;
			}
			
			size = i+1;
		}

		Writes.deleteExternalArray(temp);
		Writes.deleteExternalArray(gapCount);
		Writes.changeAllocAmount(-(gaps.length * gaps[0].length));
    }
}
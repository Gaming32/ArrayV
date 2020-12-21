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
	
	//implementation of stable library sort with O(n) extra memory + counter & pointer array (n/2 size each)
	
	private BinaryInsertionSort binaryInsert;
	
	public static int getMinLevel(int n) {
		while(n >= 32) n = (n+1)/2;
		return n;
	}
	
	private int binarySearch(int[] array, int a, int b, int val, double sleep) {
		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(3, m);
			Delays.sleep(sleep);
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else     
				a = m+1;
		}
		Highlights.clearMark(3);
		
		return a;
	}
	
	private void rebalance(int[] array, int[] temp, int[] cnts, int[] locs, int m, int b) {
		Highlights.clearMark(2);
		for(int i = 0; i < m; i++)
			Writes.write(cnts, i+1, cnts[i+1]+cnts[i]+1, 1, true, true);
		
		for(int i = m, j = 0; i < b; i++, j++) {
			Highlights.markArray(2, i);
			Writes.write(temp, cnts[locs[j]], array[i], 1, true, true);
			Writes.write(cnts, locs[j], cnts[locs[j]]+1, 0, false, true);
		}
		for(int i = 0; i < m; i++) {
			Highlights.markArray(2, i);
			Writes.write(temp, cnts[i], array[i], 1, true, true);
			Writes.write(cnts, i, cnts[i]+1, 0, false, true);
		}
		Highlights.clearMark(2);
		
		Writes.arraycopy(temp, 0, array, 0, b, 1, true, false);
		this.binaryInsert.customBinaryInsert(array, 0, cnts[0], 1);
		for(int i = 0; i < m; i++)
			this.binaryInsert.customBinaryInsert(array, cnts[i], cnts[i+1], 1);
		
		for(int i = 0; i < m+2; i++) 
			Writes.write(cnts, i, 0, 0, false, true);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.binaryInsert = new BinaryInsertionSort(this.arrayVisualizer);
		
		int j = getMinLevel(length);
		this.binaryInsert.customBinaryInsert(array, 0, j, 1);
		
		int maxLevel = j;
		for(; maxLevel*2 < length; maxLevel *= 2);
		
		int[] temp = Writes.createExternalArray(length), 
			  cnts = Writes.createExternalArray(maxLevel+2),
			  locs = Writes.createExternalArray(length-maxLevel);
		
		for(int i = j, k = 0; i < length; i++) {
			if(2*j == i) {
				this.rebalance(array, temp, cnts, locs, j, i);
				j = i;
				k = 0;
			}
			
			Highlights.markArray(2, i);
			int loc = this.binarySearch(array, 0, j, array[i], 1);
			
			Writes.write(cnts, loc+1, cnts[loc+1]+1, 0, false, true);
			Writes.write(locs, k++, loc, 0, false, true);
		}
		this.rebalance(array, temp, cnts, locs, j, length);
		
		Writes.deleteExternalArray(temp);
		Writes.deleteExternalArray(cnts);
		Writes.deleteExternalArray(locs);
    }
}
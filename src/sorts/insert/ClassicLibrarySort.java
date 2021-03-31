package sorts.insert;

import sorts.templates.Sort;
import main.ArrayVisualizer;

import java.util.Arrays;

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

final public class ClassicLibrarySort extends Sort {
    public ClassicLibrarySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Classic Library");
        this.setRunAllSortsName("Classic Library Sort");
        this.setRunSortName("Classic Library Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	//possible implementation of the library sort here https://en.wikipedia.org/wiki/Library_sort
	//makes O(1) insertions into gaps of constant size on random data using an extra (1+G)*n space
	
	private final int G = 15;
	private final int R = 4;
	
	private int max;
	
	private void shiftExt(int[] array, int a, int m, int b) {
		while(m > a) Writes.swap(array, --b, --m, 0.5, false, true);
	}
	
	private int leftBlockSearch(int[] array, int a, int b, int val) {
		int s = G+1;
		
		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			Highlights.markArray(2, m/s);
			Delays.sleep(0.5);
			
			if(Reads.compareValues(val, array[m]) <= 0) 
				b = m;
			else     
				a = m+s;
		}
		
		Highlights.clearMark(2);
		return a;
	}
	private int rightBlockSearch(int[] array, int a, int b, int val) {
		int s = G+1;
		
		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			Highlights.markArray(2, m/s);
			Delays.sleep(0.25);
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else     
				a = m+s;
		}
		
		Highlights.clearMark(2);
		return a;
	}
	
	private int locSearch(int[] array, int a, int b) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			if(Reads.compareValues(this.max, array[m]) <= 0) 
				b = m;
			else     
				a = m+1;
		}
		
		return a;
	}
    private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else     
				a = m+1;
		}
		
		return a;
	}
	
	private int eqNextGapSearch(int[] array, int a, int b) {
		int s = G+1;
		
		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			Highlights.markArray(2, m/s);
			Delays.sleep(0.25);
			
			if(this.locSearch(array, m-G, m) < m) 
				b = m;
			else     
				a = m+s;
		}
		
		Highlights.clearMark(2);
		return a;
	}
	
	private void insertTo(int[] array, int a, int b, boolean aux) {
		Highlights.clearMark(2);
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[--a], 0.5, !aux, aux);
		Writes.write(array, b, temp, 0.5, !aux, aux);
	}
	
	private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]), false);
    }
	
	private void retrieve(int[] array, int[] tmp, int i, int pEnd) {
		int loc = i-1; 
		
		for(int k = pEnd-(G+1); k > G;) {
			int m = this.locSearch(tmp, k-G, k)-1;
			k -= G+1;
			
			while(m >= k) {
				Writes.write(array, loc--, tmp[m], 0, true, false);
				Writes.write(tmp, m--, max, 1, false, true);
			}
		}
		
		int m = this.locSearch(tmp, 0, G)-1;
		while(m >= 0) {
			Writes.write(array, loc--, tmp[m], 0, true, false);
			Writes.write(tmp, m--, max, 1, false, true);
		}
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.max = array[0];
		for(int i = 1; i < length; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(0.5);
			
			if(Reads.compareValues(array[i], max) > 0) max = array[i];
		}
		for(int i = length-1; i >= 0; i--) {
			Highlights.markArray(1, i);
			Delays.sleep(0.5);
			
			if(Reads.compareValues(array[i], this.max) == 0)
				Writes.swap(array, i, --length, 1, true, false);
		}
		
		//there is supposed to be a shuffle here between [0, length)
		//but for the sake of demonstrating O(n^2) worst case it has been removed
		
		int[] tmp = Writes.createExternalArray(length*(G+1)-1);
		Arrays.fill(tmp, this.max);
		
		int s = length;
		while(s >= 32) s = (s-1)/R + 1;
		
		int i = s, j = R*i, pEnd = (s+1)*(G+1)+G;
		this.binaryInsertion(array, 0, s);
		
		for(int k = 0; k < s; k++) {
			Highlights.markArray(1, k);
			Writes.write(tmp, k*(G+1)+G, array[k], 1, false, true);
		}
		
		for(; i < length; i++) {
			if(i == j) {
				this.retrieve(array, tmp, i, pEnd);
				
				s = i;
				pEnd = (s+1)*(G+1)+G;
				j *= R;
				
				for(int k = 0; k < s; k++) {
					Highlights.markArray(1, k);
					Writes.write(tmp, k*(G+1)+G, array[k], 1, false, true);
				}
			}
			
			Highlights.markArray(1, i);
			int bLoc = this.leftBlockSearch(tmp, G, pEnd-(G+1), array[i]);
			
			if(Reads.compareValues(array[i], tmp[bLoc]) == 0) {
				int eqEnd = this.rightBlockSearch(tmp, bLoc, pEnd-(G+1), array[i]);
				bLoc = this.eqNextGapSearch(tmp, bLoc, eqEnd);
			}
			int loc  = this.locSearch(tmp, bLoc-G, bLoc);
			
			if(loc == bLoc) {
				do bLoc += G+1;
				while(bLoc < pEnd && this.locSearch(tmp, bLoc-G, bLoc) == bLoc);
				
				if(bLoc == pEnd) {
					this.retrieve(array, tmp, i, pEnd);
					
					s = i;
					pEnd = (s+1)*(G+1)+G;
					j = R*i;
					
					for(int k = 0; k < s; k++) {
						Highlights.markArray(1, k);
						Writes.write(tmp, k*(G+1)+G, array[k], 1, false, true);
					}
				}
				else {
					int rotP = this.locSearch(tmp, bLoc-G, bLoc);
					int rotS = bLoc - Math.max(rotP, bLoc - G/2);
					this.shiftExt(tmp, loc-rotS, bLoc-rotS, bLoc); 
				}
				i--;
			}
			else {
				Writes.write(tmp, loc, array[i], 1, false, true);
				this.insertTo(tmp, loc, this.rightBinSearch(tmp, bLoc-G, loc, tmp[loc]), true);
			}
		}
		this.retrieve(array, tmp, length, pEnd);
		Writes.deleteExternalArray(tmp);
    }
}
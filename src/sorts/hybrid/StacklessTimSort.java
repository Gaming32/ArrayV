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

final public class StacklessTimSort extends Sort {
	public StacklessTimSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Stackless Tim");
		this.setRunAllSortsName("Stackless Tim Sort");
		this.setRunSortName("Stackless Timsort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private final int M = 7;
	
	private int highlight = 0;
	
	private int leftBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(2, this.highlight+m);
			Delays.sleep(0.5);
			
			if(Reads.compareValues(val, array[m]) <= 0) 
				b = m;
			else
				a = m+1;
		}
		
		return a;
	}
	
	private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(2, this.highlight+m);
			Delays.sleep(0.5);
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else
				a = m+1;
		}
		
		return a;
	}
	
	private int leftExpSearch(int[] array, int a, int b, int val) {
		int i = 1;
		while(a-1+i < b && Reads.compareValues(val, array[a-1+i]) > 0) i *= 2;
		
		return this.leftBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
	}
	
	private int rightExpSearch(int[] array, int a, int b, int val) {
		int i = 1;
		while(b-i >= a && Reads.compareValues(val, array[b-i]) < 0) i *= 2;
		
		return this.rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
	}
	
	private int leftBoundSearch(int[] array, int a, int b, int val) {
		int i = 1;
		while(a-1+i < b && Reads.compareValues(val, array[a-1+i]) >= 0) i *= 2;
		
		return this.rightBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
	}
	
	private int rightBoundSearch(int[] array, int a, int b, int val) {
		int i = 1;
		while(b-i >= a && Reads.compareValues(val, array[b-i]) <= 0) i *= 2;
		
		return this.leftBinSearch(array, Math.max(a, b-i+1), b-i/2, val);
	}
	
	private void insertTo(int[] array, int a, int b) {
		Highlights.clearMark(2);
		
		if(a > b) {
			int temp = array[a];
			
			do Writes.write(array, a, array[--a], 0.25, true, false);
			while(a > b);
			
			Writes.write(array, b, temp, 0.25, true, false);
		}
	}
	
	private void buildRuns(int[] array, int a, int b, int mRun) {
		int i = a+1, j = a;
		
		while(i < b) {
			if(Reads.compareIndices(array, i-1, i++, 1, true) == 1) {
				while(i < b && Reads.compareIndices(array, i-1, i, 1, true) == 1) i++;
				Writes.reversal(array, j, i-1, 1, true, false);
			}
			else while(i < b && Reads.compareIndices(array, i-1, i, 1, true) <= 0) i++;
			
			if(i < b) j = i - (i-j-1)%mRun - 1;
			
			while(i-j < mRun && i < b) {
				this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
				i++;
			}
			j = i++;
		}
	}
	
	//galloping mode code refactored from TimSorting.java
	private void mergeFW(int[] array, int[] tmp, int a, int m, int b) {
		int len1 = m-a, t = a;
		Highlights.clearMark(2);
		Writes.arraycopy(array, a, tmp, 0, len1, 1, true, true);
		
		int i = 0, mGallop = M, l = 0, r = 0;
		
		while(true) {
			do {
				if(Reads.compareValues(tmp[i], array[m]) <= 0) {
					Writes.write(array, a++, tmp[i++], 1, true, false);
					l++;
					r = 0;
					
					if(i == len1) return;
				}
				else {
					Highlights.markArray(2, m);
					Writes.write(array, a++, array[m++], 1, true, false);
					r++;
					l = 0;
					
					if(m == b) {
						while(i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
						return;
					}
				}
			}
			while((l | r) < mGallop);
				
			do {
				l = this.leftExpSearch(array, m, b, tmp[i])-m;
				
				for(int j = 0; j < l; j++)
					Writes.write(array, a++, array[m++], 1, true, false);
				Writes.write(array, a++, tmp[i++], 1, true, false);
				
				if(i == len1) return;
				
				else if(m == b) {
					while(i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
					return;
				}
				
				this.highlight = t;
				r = this.leftBoundSearch(tmp, i, len1, array[m])-i;
				this.highlight = 0;
				
				for(int j = 0; j < r; j++)
					Writes.write(array, a++, tmp[i++], 1, true, false);
				Writes.write(array, a++, array[m++], 1, true, false);
				
				if(i == len1) return;
				
				else if(m == b) {
					while(i < len1) Writes.write(array, a++, tmp[i++], 1, true, false);
					return;
				}
				
				mGallop--;
			}
			while((l | r) >= M);
			
			if(mGallop < 0) mGallop = 0;
			mGallop += 2;
		}
	}
	private void mergeBW(int[] array, int[] tmp, int a, int m, int b) {
		int len2 = b-m, t = a;
		Highlights.clearMark(2);
		Writes.arraycopy(array, m, tmp, 0, len2, 1, true, true);
		
		int i = len2-1, mGallop = M, l = 0, r = 0;
		m--;
		
		while(true) {
			do {
				if(Reads.compareValues(tmp[i], array[m]) >= 0) {
					Writes.write(array, --b, tmp[i--], 1, true, false);
					l++;
					r = 0;
					
					if(i < 0) return;
				}
				else {
					Highlights.markArray(2, m);
					Writes.write(array, --b, array[m--], 1, true, false);
					r++;
					l = 0;
					
					if(m < a) {
						while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
						return;
					}
				}
			}
			while((l | r) < mGallop);
			
			do {
				l = (m+1)-this.rightExpSearch(array, a, m+1, tmp[i]);
				
				for(int j = 0; j < l; j++)
					Writes.write(array, --b, array[m--], 1, true, false);
				Writes.write(array, --b, tmp[i--], 1, true, false);
				
				if(i < 0) return;
				
				else if(m < a) {
					while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
					return;
				}
				
				this.highlight = t;
				r = (i+1)-this.rightBoundSearch(tmp, 0, i+1, array[m]);
				this.highlight = 0;
				
				for(int j = 0; j < r; j++)
					Writes.write(array, --b, tmp[i--], 1, true, false);
				Writes.write(array, --b, array[m--], 1, true, false);
				
				if(i < 0) return;
				
				else if(m < a) {
					while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
					return;
				}
			}
			while((l | r) >= M);
			
			if(mGallop < 0) mGallop = 0;
			mGallop += 2;
		}
	}
	
	private void smartMerge(int[] array, int[] tmp, int a, int m, int b) {
		if(Reads.compareValues(array[m-1], array[m]) <= 0) return;
		
		a = this.leftBoundSearch(array, a, m, array[m]);
		b = this.rightBoundSearch(array, m, b, array[m-1]);
		
		if(b-m < m-a) this.mergeBW(array, tmp, a, m, b);
		else		  this.mergeFW(array, tmp, a, m, b);
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int[] tmp = Writes.createExternalArray(length/2);
		
		int mRun = length;
		for(; mRun >= 32; mRun = (mRun+1)/2);
		
		this.buildRuns(array, 0, length, mRun);
		
		for(int i, j = mRun; j < length; j *= 2) {
			for(i = 0; i+2*j <= length; i += 2*j)
				this.smartMerge(array, tmp, i, i+j, i+2*j);
			
			if(i+j < length) this.smartMerge(array, tmp, i, i+j, length);
		}
		Writes.deleteExternalArray(tmp);
	}
}
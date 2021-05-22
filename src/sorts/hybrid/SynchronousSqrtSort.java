package sorts.hybrid;

import sorts.templates.Sort;
import main.ArrayVisualizer;
import sorts.insert.BinaryDoubleInsertionSort;

/*
 * 
MIT License

Copyright (c) 2021 The Holy Grail Sort Project, implemented by aphitorite

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

final public class SynchronousSqrtSort extends Sort {
    public SynchronousSqrtSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Synchronous Sqrt");
        this.setRunAllSortsName("Synchronous Sqrt Sort");
        this.setRunSortName("Synchronous Sqrtsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void shift(int[] array, int a, int m, int b) {
		while(m < b) Writes.write(array, a++, array[m++], 1, true, false);
	}
	private void shiftBW(int[] array, int a, int m, int b) {
		while(m > a) Writes.write(array, --b, array[--m], 1, true, false);
	}
	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}
	
	private void mergeFW(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;
		
		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0)
				Writes.write(array, p++, array[i++], 1, true, false);
			else
				Writes.write(array, p++, array[j++], 1, true, false);
		}
		if(i > p)
			while(i < m) Writes.write(array, p++, array[i++], 1, true, false);
		
		while(j < b) Writes.write(array, p++, array[j++], 1, true, false);
	}
	private int smartMergeBW(int[] array, int a, int m, int b, int p, boolean rev) {
		int i = m-1, j = b-1;
		int cmp = rev ? -1 : 0;
		
		while(i >= a && j >= m) {
			if(Reads.compareValues(array[i], array[j]) > cmp)
				Writes.write(array, --p, array[i--], 1, true, false);
			else
				Writes.write(array, --p, array[j--], 1, true, false);
		}
		return i+1;
	}
	private void mergeBW(int[] array, int a, int m, int b, int p) {
		int bLen = p-b;
		p = this.smartMergeBW(array, a, m, b, p, false);
		this.shiftBW(array, a, p, p+bLen);
	}
	
	private void blockSelection(int[] array, int[] tags, int a, int b, int bLen, int t, int tj) {
		for(int i = 0; i < Math.min(tj+1, tags.length-t); i++)
			Writes.write(tags, t+i, i + (i <= tj/2 ? 0 : tags.length), 1, true, true);
		
		for(int j = a, p = a; j < b-bLen; j+=bLen) {
			int min = p == j ? j+bLen : j;
			
			for(int i = min+bLen; i < b; i+=bLen) {
				if(i != p) {
					int cmp = Reads.compareValues(array[i], array[min]);
					
					if(cmp == -1 || (cmp == 0 && Reads.compareValues(tags[t + (i-a)/bLen], tags[t + (min-a)/bLen]) == -1))
						min = i;
				}
			}
			if(min > j) {
				if(p == j) Writes.arraycopy(array, min, array, j, bLen, 1, true, false);
				else	   this.multiSwap(array, j, min, bLen);
			}
			Writes.swap(tags, t + (j-a)/bLen, t + (min-a)/bLen, 1, true, false);
			
			if(p == j || p == min) p ^= j ^ min;
		}
		Highlights.clearMark(2);
	}
	private void mergeBlocksBW(int[] array, int[] tags, int a, int b, int ti, int tb, int bLen, int bCnt) {
		int tj = tb-1, mkv = tags.length;
		int f = b, a1 = f-bLen;
		boolean rev = Reads.compareValues(tags[tj], mkv) < 0;
		
		while(true) {
			do {
				tj--;
				a1 -= bLen;
			}
			while(tj >= ti && (rev ? Reads.compareValues(tags[tj], mkv) < 0
								   : Reads.compareValues(tags[tj], mkv) >= 0));
			if(tj < ti) {
				this.shiftBW(array, a, f, f+bLen);
				break;
			}
			f = this.smartMergeBW(array, a1, a1+bLen, f, f+bLen, rev);
			rev = !rev;
		}
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		BinaryDoubleInsertionSort smallSort = new BinaryDoubleInsertionSort(this.arrayVisualizer);
		
		if(length <= 16) {
			smallSort.customDoubleInsert(array, 0, length, 0.5);
			return;
		}
		
		int bLen = 1;
		while(bLen*bLen < length) bLen *= 2;
		int mod = length%bLen;
		
		int a = bLen+mod, b = length, len = b-a;
		int i, j = 1;
		
		int[] temp = Writes.createExternalArray(bLen+mod);
		int[] tags = Writes.createExternalArray((length-1)/bLen+1);
		
		smallSort.customDoubleInsert(array, 0, a, 0.25);
		Writes.arraycopy(array, 0, temp, 0, a, 1, true, true);
		
		for(; j < bLen; j *= 2) {
			int p = Math.max(2, j);
			
			for(i = a; i+2*j < b; i += 2*j)
				this.mergeFW(array, i, i+j, i+2*j, i-p);
			
			if(i+j < b) this.mergeFW(array, i, i+j, b, i-p);
			else		this.shift(array, i-p, i, b);
			
			a -= p; b -= p;
		}
		
		int p = len%(2*j);
		i = b-p;
		
		if(i+j < b) this.mergeBW(array, i, i+j, b, b+j);
		else 		this.shiftBW(array, i, b, b+j);
			
		for(i -= 2*j; i >= a; i -= 2*j)
			this.mergeBW(array, i, i+j, i+2*j, i+3*j);
		
		a += j; b += j; j *= 2;
		
		for(int ti, tj = 4; j < len; j *= 2, tj *= 2) {
			for(i = a, ti = 0; i+2*j < b; i += 2*j, ti += tj)
				this.blockSelection(array, tags, i-bLen, i+2*j, bLen, ti, tj);
			
			boolean noFrag = i+j < b;
			p = (b-i)/bLen;
			
			if(noFrag) this.blockSelection(array, tags, i-bLen, b, bLen, ti, tj);
			
			a -= bLen; b -= bLen; i -= bLen;
			
			if(noFrag) this.mergeBlocksBW(array, tags, i, b, ti, ti+p, bLen, tj/2);
				
			for(i -= 2*j, ti -= tj; i >= a; i -= 2*j, ti -= tj)
				this.mergeBlocksBW(array, tags, i, i+2*j, ti, ti+tj, bLen, tj/2);
			
			a += bLen; b += bLen;
		}
		p = 0; i = 0; j = a;
		
		while(i < a && j < b) {
			Highlights.markArray(2, i);
			
			if(Reads.compareValues(temp[i], array[j]) <= 0)
				Writes.write(array, p++, temp[i++], 1, true, false);
			else
				Writes.write(array, p++, array[j++], 1, true, false);
		}
		while(i < a) {
			Highlights.markArray(2, i);
			Writes.write(array, p++, temp[i++], 1, true, false);
		}
		Writes.deleteExternalArray(temp);
		Writes.deleteExternalArray(tags);
    }
}
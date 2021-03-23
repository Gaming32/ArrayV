package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

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

final public class LazierSort extends Sort {
    public LazierSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lazier Stable");
        this.setRunAllSortsName("Lazier Stable Sort");
        this.setRunSortName("Lazier Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
	private static int cbrt(int n) {
		int r = 1;
		for(; r*r*r < n; r++);
		return r;
	}
	
    private void shift(int[] array, int a, int m, int b) {
    	while(m < b) Writes.swap(array, a++, m++, 1, true, false);
	}
    
    private void shiftBW(int[] array, int a, int m, int b) {
    	while(m > a) Writes.swap(array, --b, --m, 1, true, false);
	}
    
    private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}
    
    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;
		
        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(array, m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }
    
    private void insertTo(int[] array, int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[(a--)-1], 0.5, true, false);
		Writes.write(array, b, temp, 0.5, true, false);
	}
    
    private int leftBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			
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
    
    private int findKeys(int[] array, int a, int b, int n) {
		int nKeys = 1, p = a, pEnd = a+nKeys;

		Highlights.clearMark(2);
		for(int i = pEnd; i < b && nKeys < n; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			int loc = this.leftBinSearch(array, p, pEnd, array[i]);
			
			if(pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
				this.rotate(array, p, pEnd, i);
				int inc = i-pEnd;
				loc  += inc;
				p    += inc;
				pEnd += inc;
				
				this.insertTo(array, pEnd, loc);
				nKeys++;
				pEnd++;
			}
		}
		this.rotate(array, a, p, pEnd);
		return nKeys;
	}
	
	private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }
    
    public void inPlaceMerge(int[] array, int a, int m, int b) {
		int i = a, j = m, k;
		
		while(i < j && j < b) {
			if(Reads.compareValues(array[i], array[j]) > 0) {
				k = this.leftExpSearch(array, j+1, b, array[i]);
				this.rotate(array, i, j, k);
				
				i += k-j;
				j = k;
			} 
			else i++;
		}
	}
	
	private void lazierBlockMerge(int[] array, int a, int m, int b, int bLen) {
		while(m-bLen >= a) {
			this.inPlaceMerge(array, m-bLen, m, b);
			m -= bLen;
		}
		this.inPlaceMerge(array, a, m, b);
	}
	
	private void inPlaceMergeSort(int[] array, int a, int b) {
        int len = b-a, i, j;
        
		for(j = len; j >= 32; j = (j+1)/2);
		
		for(i = a; i+j <= b; i += j)
			this.binaryInsertion(array, i, i+j);
		this.binaryInsertion(array, i, b);
		
        for(; j < len; j *= 2) {
            for(i = a; i + 2*j <= b; i += 2*j)
                this.inPlaceMerge(array, i, i+j, i+2*j);
            
            if(i + j < b)
                this.inPlaceMerge(array, i, i+j, b);
        }
	}
	
	private void lazierBlockMergeSort(int[] array, int a, int b, int keys) {
        int a1 = a+keys, b1 = b,
			len = b-a1, i, j;
        
		for(j = len; j >= 32; j = (j+1)/2);
    	
    	//insertion
    	for(i = a1; i+j <= b; i += j)
    		this.binaryInsertion(array, i, i+j);
    	this.binaryInsertion(array, i, b);
    	
    	//build blocks
    	if(keys >= 2*j) {
			for(i = a1; i+2*j <= b; i += 2*j)
				this.mergeTo(array, i, i+j, i+2*j, i-2*j);
			
			if(i+j < b) this.mergeTo(array, i, i+j, b, i-2*j);
			else        this.shift(array, i-2*j, i, b);

			j *= 2;
			a1 -= j;
			b1 -= j;
			
			while(a1-j >= a) {
				for(i = a1; i+2*j <= b1; i += 2*j)
					this.mergeTo(array, i, i+j, i+2*j, i-j);
				
				if(i+j < b1) this.mergeTo(array, i, i+j, b1, i-j);
				else         this.shift(array, i-j, i, b1);

				a1 -= j;
				b1 -= j;
				j *= 2;
			}
			
			for(i = a1; i+2*j <= b1; i += 2*j);
			if(i+j < b1) this.mergeToBW(array, i, i+j, b1, b);
			else         this.shiftBW(array, i, b1, b);
			
			for(; i > a1; i -= 2*j) this.mergeToBW(array, i-2*j, i-j, i, i+j);
			
			a1 += j;
			j *= 2;
		}
		
		int c = 1;
        for(; j < len; j *= 2) {
			while((c*c*c)/(2*j) < (2*j)) c++;
				
            for(i = a1; i + 2*j <= b; i += 2*j)
                this.lazierBlockMerge(array, i, i+j, i+2*j, c);
            
            if(i + j < b)
                this.lazierBlockMerge(array, i, i+j, b, c);
        }
		
		//redist buffer
    	MaxHeapSort heapSort = new MaxHeapSort(this.arrayVisualizer);
    	heapSort.customHeapSort(array, a, a1, 1);
    	this.inPlaceMerge(array, a, a1, b);
	}
    
	private void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;
		
		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0) 
				Writes.swap(array, p++, i++, 1, true, false);
			
			else Writes.swap(array, p++, j++, 1, true, false);
		}
		if(p < i) while(i < m) Writes.swap(array, p++, i++, 1, true, false);
		while(j < b) Writes.swap(array, p++, j++, 1, true, false);
	}
	
	private void mergeToBW(int[] array, int a, int m, int b, int p) {
		int i = m-1, j = b-1; p--;
		
		while(i >= a && j >= m) {
			if(Reads.compareValues(array[i], array[j]) > 0) 
				Writes.swap(array, p--, i--, 1, true, false);
			
			else Writes.swap(array, p--, j--, 1, true, false);
		}
		if(p > j) while(j >= m) Writes.swap(array, p--, j--, 1, true, false);
		while(i >= a) Writes.swap(array, p--, i--, 1, true, false);
	}
	
	private void mergeFW(int[] array, int a, int p, int m, int b) {
		int i = a, j = m, k = p;
		
		while(j < b) {
			while(i < p && Reads.compareValues(array[i], array[j]) <= 0)
				Writes.swap(array, k++, i++, 1, true, false);
			if(i == p) return;
			
			int n = this.leftExpSearch(array, j+1, b, array[i]);
			while(j < n) Writes.swap(array, k++, j++, 1, true, false);
		}
		while(i < p) Writes.swap(array, k++, i++, 1, true, false);
	}
	
    protected void lazierSort(int[] array, int a, int b) {
    	int len = b-a;
    	
		//build blocks dies when length is too small (< 65)
    	if(len <= 64) {
    		this.binaryInsertion(array, a, b);
    		return;
    	}

		int bLen, mRun;
		for(bLen = 1; (bLen*bLen*bLen)/len < len; bLen *= 2);
		for(mRun = 1; (mRun*mRun*mRun)/len < len; mRun++);
		
		mRun = (16*mRun) / bLen;
		bLen = (bLen*mRun) / 16;
		
    	int keys = this.findKeys(array, a, b, bLen);
		
    	if(keys < bLen) {
    		if(keys == 1) return;
			else if(keys <= cbrt(len)) this.inPlaceMergeSort(array, a, b);
			else this.lazierBlockMergeSort(array, a, b, keys);
			return;
    	}
    	
    	int a1 = a+bLen, b1 = b, i, j = mRun;
    	
    	//insertion
    	for(i = a1; i+j <= b; i += j)
    		this.binaryInsertion(array, i, i+j);
    	this.binaryInsertion(array, i, b);
    	
    	//build blocks
    	for(i = a1; i+2*j <= b; i += 2*j)
    		this.mergeTo(array, i, i+j, i+2*j, i-2*j);
    	
    	if(i+j < b) this.mergeTo(array, i, i+j, b, i-2*j);
    	else        this.shift(array, i-2*j, i, b);

        j *= 2;
    	a1 -= j;
    	b1 -= j;
    	
    	while(a1 > a) {
        	for(i = a1; i+2*j <= b1; i += 2*j)
        		this.mergeTo(array, i, i+j, i+2*j, i-j);
        	
        	if(i+j < b1) this.mergeTo(array, i, i+j, b1, i-j);
        	else         this.shift(array, i-j, i, b1);

        	a1 -= j;
        	b1 -= j;
            j *= 2;
    	}
    	
    	//do that merge thing
    	for(i = a; i+bLen < b1; i += bLen);
    	this.shiftBW(array, i, b1, b);
    	
    	b1 = i+bLen;
    	i -= bLen;
    	
    	while(i >= a) {
    		this.mergeFW(array, i, i+bLen, b1, b);
    		
    		i -= bLen;
    		b1 -= bLen;
    	}
    	
    	//redist buffer
    	a1 = a+bLen;
    	MaxHeapSort heapSort = new MaxHeapSort(this.arrayVisualizer);
    	heapSort.customHeapSort(array, a, a1, 1);
    	this.inPlaceMerge(array, a, a1, b);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.lazierSort(array, 0, currentLength);
    }
}
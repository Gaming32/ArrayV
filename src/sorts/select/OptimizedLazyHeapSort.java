package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class OptimizedLazyHeapSort extends Sort {
    public OptimizedLazyHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Lazy Heap");
        this.setRunAllSortsName("Optimized Lazy Heap Sort");
        this.setRunSortName("Optimized Lazy Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int findMin(int[] array, int p, int a, int b, int s) {
		int min = p;
		
		for(int i = a; i < b; i += s)
			if(Reads.compareIndices(array, i, min, 0.1, true) < 0)
				min = i;
			
		return min;
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int s = (int)Math.sqrt(length-1)+1;
		
		int f = (length-1)%s+1;
		int fMin = this.findMin(array, 0, 1, f, 1);
		
		for(int j = f; j < length; j += s) {
			int min = this.findMin(array, j, j+1, j+s, 1);
			
			if(j != min) Writes.swap(array, j, min, 1, true, false);
		}
		
		for(int j = 0; j < length;) {
			int min = this.findMin(array, fMin, f, length, s);
			
			if(min == fMin) {
				if(j != min) Writes.swap(array, j, min, 1, true, false);
				if(++j == f) f += s; //check for bounds if last block is < s
				
				fMin = this.findMin(array, j, j+1, f, 1);
			}
			else {
				if(j == fMin) fMin = this.findMin(array, j+1, j+2, f, 1);
				
				int nMin = this.findMin(array, j, min+1, min+s, 1);
					
				if(nMin == j) Writes.swap(array, j, min, 1, true, false);
				
				else {
					Highlights.clearMark(2);
					
					int t = array[j];
					Writes.write(array, j,    array[min],  0.5, true, false);
					Writes.write(array, min,  array[nMin], 0.5, true, false);
					Writes.write(array, nMin, t,           0.5, true, false);
				}
				
				if(++j == f) f += s;
			}
		}
    }
}
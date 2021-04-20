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

final public class StableCycleSort extends Sort {
    public StableCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Cycle");
        this.setRunAllSortsName("Stable Cycle Sort");
        this.setRunSortName("Stable Cyclesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private final int WLEN = 3;
	
	private boolean getBit(int[] bits, int idx) {
		int b = (bits[idx >> WLEN]) >> (idx & ((1 << WLEN) - 1)) & 1;
		return b == 1;
	}
	
	private void flag(int[] bits, int idx) {
		Highlights.markArray(4, idx >> WLEN);
		Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] | (1 << (idx & ((1 << WLEN) - 1))), 0.02, false, true);
	}
	
	private int destination1(int[] array, int[] bits, int a, int b1, int b) {
		int d = 0, e = 0;
		
		while(d < a && Reads.compareValues(array[d], array[a]) == -1) {
			d++;
			
			Highlights.markArray(3, d);
			Delays.sleep(0.01);
		}
		for(int i = a+1; i < b; i++) {
			Highlights.markArray(2, i);
			if(Reads.compareValues(array[i], array[a]) == -1) d++;
			
			Highlights.markArray(3, d);
			Delays.sleep(0.01);
		}
		for(int i = a+1; i < b1; i++) {
			Highlights.markArray(2, i);
			if(!this.getBit(bits, i) && Reads.compareValues(array[i], array[a]) == 0) e++;
			
			Delays.sleep(0.01);
		}
		while(this.getBit(bits, d) || e-- > 0) {
			d++;
			
			Highlights.markArray(3, d);
			Delays.sleep(0.01);
		}
		
		return d;
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int[] bits = Writes.createExternalArray(((length-1) >> WLEN) + 1);
		
		for(int i = 0; i < length-1; i++) {
			if(!this.getBit(bits, i)) {
				Highlights.markArray(1, i);
				int j = i;
				
				do {
					int k = this.destination1(array, bits, i, j, length);
					Writes.swap(array, i, k, 0.02, true, false);
					this.flag(bits, k);
					j = k;
				}
				while(j != i);
			}
		}
		Writes.deleteExternalArray(bits);
    }
}
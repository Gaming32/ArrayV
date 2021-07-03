package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Random;

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
 
final public class ShuffledTreeSort extends Sort {
	public ShuffledTreeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Shuffled Tree");
		this.setRunAllSortsName("Shuffled Tree Sort");
		this.setRunSortName("Shuffled Treesort");
		this.setCategory("Insertion Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int idx;
	
	private void stableSwap(int[] array, int[] keys, int a, int b) {
		Writes.swap(array, a, b, 0, true, false);
		Writes.swap(keys,  a, b, 1, false, true);
	}
	
	private void traverse(int[] array, int[] keys, int[] lower, int[] upper, int r) {
		Highlights.markArray(2, r);
		Delays.sleep(1);
		
		if(lower[r] != 0) this.traverse(array, keys, lower, upper, lower[r]);
		
		Writes.write(keys, this.idx++, r, 1, true, true);
		
		if(upper[r] != 0) this.traverse(array, keys, lower, upper, upper[r]);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int[] keys  = Writes.createExternalArray(currentLength);
		for(int i = 0; i < currentLength; i++)
			Writes.write(keys, i, i, 1, true, true);
		
		Random random = new Random();
		for(int i = 0; i < currentLength; i++){
			int r = random.nextInt(currentLength - i) + i;
			this.stableSwap(array, keys, i, r);
		}
		
		int[] lower = Writes.createExternalArray(currentLength);
		int[] upper = Writes.createExternalArray(currentLength);
		int[] next;
		
		for(int i = 1; i < currentLength; i++) {
			Highlights.markArray(2, i);
			int c = 0;
			
			while(true) {
				Highlights.markArray(1, c);
				Delays.sleep(0.5);
				
				int cmp = Reads.compareValues(array[i], array[c]);
				next = (cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(keys[i], keys[c]) < 0)) ? lower : upper;
				
				if(next[c] == 0) {
					Writes.write(next, c, i, 0, false, true);
					break;
				}
				else c = next[c];
			}
		}
		Highlights.clearMark(2);
		
		this.idx = 0;
		this.traverse(array, keys, lower, upper, 0);
		
		for(int i = 0; i < currentLength-1; i++) {
			Highlights.markArray(2, i);
			
			if(Reads.compareOriginalValues(i, keys[i]) != 0) {
				int t = array[i];
				int j = i, k = keys[i];
				
				do {
					Writes.write(array, j, array[k], 1, true, false);
					Writes.write(keys, j, j, 1, true, true);
					
					j = k;
					k = keys[k];
				}
				while(Reads.compareOriginalValues(k, i) != 0);
				
				Writes.write(array, j, t, 1, true, false);
				Writes.write(keys, j, j, 1, true, true);
			}
		}
		
		Writes.deleteExternalArray(lower);
		Writes.deleteExternalArray(upper);
		Writes.deleteExternalArray(keys);
	}
}
package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class NapoleonSort extends Sort {
    public NapoleonSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Napoleon");
        this.setRunAllSortsName("Napoleon Sort");
        this.setRunSortName("Napoleon sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }

	private void tilsit(int[] array, int currentLen) {
		for(int i = 0, j = currentLen - 1; i < currentLen/2; i++, j--) {
			Highlights.markArray(1, i);
			Highlights.markArray(2, j);
			Delays.sleep(0.01);
			if(Reads.compareIndices(array, i, j, 0.01, true) > 0)
				Writes.swap(array, i, j, 0.01, true, false);
		}
	}

	private void napoleon(int[] array, int end) {
		int lo = 0;
		int hi = end;
		
		int prev = array[0];
		boolean goSmaller = true;
		boolean noneFound = false;
		
		while(hi > lo) {
			if(goSmaller) {
				int next = lookEast(array, prev, lo+1, hi);
				if(next == lo) { // No smaller elements
					if(noneFound) {
						lo++;
						hi--;
					}
					prev = noneFound ? array[lo] : array[hi];
					goSmaller = noneFound;
					noneFound = !noneFound;
				} else if(next == hi) { // Next smaller element already at end
					prev = array[hi];
					conquer(array, hi, lo);
					noneFound = false;
				} else { // Next smaller element elsewhere
					prev = array[next];
					conquer(array, next, hi);
					noneFound = false;
					goSmaller = false;
				}
			} else {
				int next = lookWest(array, prev, hi-1, lo);
				if(next == hi) { // No larger elements
					if(noneFound) {
						lo++;
						hi--;
					}
					prev = array[lo];
					goSmaller = true;
					noneFound = !noneFound;
				} else if(next == lo) { // Next larger element already at start
					prev = array[lo];
					conquer(array, lo, hi);
					noneFound = false;
				} else { // Next larger element elsewhere
					prev = array[next];
					conquer(array, next, lo);
					noneFound = false;
					goSmaller = true;
				}
			}
		}
		
	}
	
	private int lookEast(int[] array, int prev, int start, int end) {
		for(int i = start; i <= end; i++) {
			if(Reads.compareValues(array[i], prev) < 0)
				return i;
		}
		return start - 1;
	}
	
	private int lookWest(int[] array, int prev, int start, int end) {
		for(int i = start; i >= end; i--) {
			if(Reads.compareValues(array[i], prev) > 0)
				return i;
		}
		return start + 1;
	}
	
	private void conquer(int[] array, int index, int target) {
		int blockSize = 1;
		while(index + blockSize - 1 < target) {
			int marchTo = recruit(array, array[index], index+blockSize, target);
			int spaceBetween = marchTo - index - blockSize + 1;
			march(array, index, blockSize, spaceBetween);
			index += spaceBetween;
			blockSize++;
		}
		while(index - blockSize + 1 > target) {
			int marchTo = recruit(array, array[index], index-blockSize, target);
			int spaceBetween = index - blockSize - marchTo + 1;
			march(array, marchTo, spaceBetween, blockSize);
			index -= spaceBetween;
			blockSize++;
		}
	}
	
	private int recruit(int[] array, int identicalTo, int start, int end) {
		if(start<end) {
			for(int i = start; i <= end; i++) {
				if(Reads.compareValues(array[i], identicalTo) == 0)
					return i - 1;
			}
			return end;
		}
		for(int i = start; i >= end; i--) {
			if(Reads.compareValues(array[i], identicalTo) == 0)
				return i + 1;
		}
		return end;
	}
	
	private void march(int[] array, int index, int len1, int len2) {
		while(len1 != 0 && len2 != 0) {
			if(len1 <= len2) {
				attack(array, index, index+len1, len1);
				index += len1;
				len2 -= len1;
			} else {
				attack(array, index + len1, index + len1 - len2, len2);
				len1 -= len2;
			}
		}
	}
	
	private void attack(int[] array, int startA, int startB, int swapsLeft) {
		while(swapsLeft != 0) {
			Writes.swap(array, startA++, startB++, 1, true, false);
			swapsLeft--;
		}
	}

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
    	tilsit(array, currentLen);
    	Highlights.clearAllMarks();
        napoleon(array, currentLen - 1);
    }
}
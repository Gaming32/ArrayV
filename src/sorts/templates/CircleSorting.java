package sorts.templates;

import main.ArrayVisualizer;

/*
 * 
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

public abstract class CircleSorting extends Sort {
    protected CircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    
    protected int circleSortRoutine(int[] array, int lo, int hi, int swapCount, double sleep) {        
        if (lo == hi)
            return swapCount;
 
        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;
 
        while (lo < hi) {
            if (Reads.compareValues(array[lo], array[hi]) > 0) {
                Writes.swap(array, lo, hi, sleep, true, false);
                swapCount++;
            }
            lo++;
            hi--;
            
            Highlights.markArray(1, lo);
            Highlights.markArray(2, hi);
            Delays.sleep(sleep / 2);
        }
 
        if (lo == hi && Reads.compareValues(array[lo], array[hi + 1]) > 0) {
            Writes.swap(array, lo, hi + 1, sleep, true, false);
            swapCount++;
        }
 
        swapCount = this.circleSortRoutine(array, low, low + mid, swapCount, sleep);
        swapCount = this.circleSortRoutine(array, low + mid + 1, high, swapCount, sleep);
        
        return swapCount;
    }
}
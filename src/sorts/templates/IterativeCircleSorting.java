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

public abstract class IterativeCircleSorting extends Sort {
    protected IterativeCircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    
    protected int circleSortRoutine(int[] array, int length, double sleep) {
        int swapCount = 0;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int start = 0; start + gap < length; start += 2 * gap) {
                int high = start + 2 * gap;
                int low = start;
        
                while (low < high) {
                    if (Reads.compareValues(array[low], array[high]) > 0) {
                        Writes.swap(array, low, high, sleep, true, false);
                        swapCount++;
                    }
                    low++;
                    high--;
                    
                    Highlights.markArray(1, low);
                    Highlights.markArray(2, high);
                    Delays.sleep(sleep / 2);
                }
        
                if (low == high && Reads.compareValues(array[low], array[high + 1]) > 0) {
                    Writes.swap(array, low, high + 1, sleep, true, false);
                    swapCount++;
                }
            }
        }
        if (Reads.compareValues(array[0], array[1]) > 0) {
            Writes.swap(array, 0, 1, sleep, true, false);
            swapCount++;
        }
        return swapCount;
    }
}
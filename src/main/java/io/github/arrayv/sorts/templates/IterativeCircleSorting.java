package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;

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

    protected int end;

    protected int circleSortRoutine(int[] array, int length, double sleep) {
        int swapCount = 0;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int start = 0; start + gap < this.end; start += 2 * gap) {
                int high = start + 2 * gap - 1;
                int low = start;

                while (low < high) {
                    if (high < this.end && Reads.compareIndices(array, low, high, sleep / 2, true) > 0) {
                        Writes.swap(array, low, high, sleep, true, false);
                        swapCount++;
                    }

                    low++;
                    high--;
                }
            }
        }
        return swapCount;
    }
}

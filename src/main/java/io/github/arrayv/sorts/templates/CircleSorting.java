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

public abstract class CircleSorting extends Sort {
    protected CircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected int end;

    protected int circleSortRoutine(int[] array, int lo, int hi, int swapCount, double sleep) {
        if (lo == hi)
            return swapCount;

        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;

        while (lo < hi) {
            if (hi < this.end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0) {
                Writes.swap(array, lo, hi, sleep, true, false);
                swapCount++;
            }

            lo++;
            hi--;
        }

        swapCount = this.circleSortRoutine(array, low, low + mid, swapCount, sleep);
        if (low + mid + 1 < this.end)
            swapCount = this.circleSortRoutine(array, low + mid + 1, high, swapCount, sleep);

        return swapCount;
    }
}

package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.CircleSorting;

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
@SortMeta(listName = "Circle (Recursive)", runName = "Recursive Circle Sort")
public final class CircleSortRecursive extends CircleSorting {
    public CircleSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void singleRoutine(int[] array, int length) {
        this.circleSortRoutine(array, 0, length - 1, 0, 0.1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.end = sortLength;
        int n = 1;
        for (; n < sortLength; n *= 2)
            ;

        int numberOfSwaps = 0;
        do {
            numberOfSwaps = this.circleSortRoutine(array, 0, n - 1, 0, 1);
        } while (numberOfSwaps != 0);
    }
}

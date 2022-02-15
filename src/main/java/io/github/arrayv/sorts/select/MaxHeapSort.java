package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.HeapSorting;

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

public final class MaxHeapSort extends HeapSorting {
    public MaxHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Max Heap");
        this.setRunAllSortsName("Max Heap Sort");
        this.setRunSortName("Heapsort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void makeHeap(int[] array, int start, int length, double sleep) {
        this.heapify(array, start, length, sleep, true);
    }

    public void customHeapSort(int[] array, int start, int length, double sleep) {
        this.heapSort(array, start, length, sleep, true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.heapSort(array, 0, length, 1, true);
    }
}

package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Bozosort randomly swaps any two elements until the array is sorted.
 */
public final class BozoSort extends BogoSorting {
    public BozoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bozo");
        this.setRunAllSortsName("Bozo Sort");
        this.setRunSortName("Bozosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length))
            Writes.swap(array, BogoSorting.randInt(0, length), BogoSorting.randInt(0, length), this.delay, true, false);
    }
}

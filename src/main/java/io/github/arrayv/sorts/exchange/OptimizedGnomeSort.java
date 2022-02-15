package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class OptimizedGnomeSort extends Sort {
    public OptimizedGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort");
        this.setRunSortName("Optimized Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Taken from https://en.wikipedia.org/wiki/Gnome_sort
    private void smartGnomeSort(int[] array, int lowerBound, int upperBound, double sleep) {
        int pos = upperBound;

        while(pos > lowerBound && Reads.compareValues(array[pos - 1], array[pos]) == 1) {
            Writes.swap(array, pos - 1, pos, sleep, true, false);
            pos--;
        }
    }

    public void customSort(int[] array, int low, int high, double sleep) {
        for(int i = low + 1; i < high; i++) {
            smartGnomeSort(array, low, i, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i = 1; i < length; i++) {
            smartGnomeSort(array, 0, i, 0.05);
        }
    }
}

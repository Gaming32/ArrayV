package io.github.arrayv.sorts.exchange;

import java.lang.Math;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public class SnuffleSort extends Sort {
    private static double DELAY = 1;
    public SnuffleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Snuffle");
        this.setRunAllSortsName("Snuffle Sort");
        this.setRunSortName("Snuffle Sort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(100);
        this.setBogoSort(false);
    }

	private void snuffleSort(int[] arr, int start, int stop) {
        if (stop - start + 1 >= 2) {
    	    Highlights.markArray(0, start);
            Highlights.markArray(1, stop);
            if (Reads.compareValues(arr[start], arr[stop]) == 1)
                Writes.swap(arr, start, stop, DELAY, false, false);
            if (stop - start + 1 >= 3) {
                int mid = (stop - start) / 2 + start;
                for (int i = 0; i < (int) Math.ceil((stop - start + 1) / 2); i++) {
                    this.snuffleSort(arr, start, mid);
                    this.snuffleSort(arr, mid, stop);
                }
            }
        }
	}

    @Override
    public void runSort(int[] array, int length, int buckets) {
        this.snuffleSort(array, 0, length - 1);
    }
}

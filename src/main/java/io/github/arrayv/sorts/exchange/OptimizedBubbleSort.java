package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class OptimizedBubbleSort extends Sort {
    public OptimizedBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Bubble");
        this.setRunAllSortsName("Optimized Bubble Sort");
        this.setRunSortName("Optimized Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	int consecSorted;
        for(int i = length - 1; i > 0; i -= consecSorted) {
            consecSorted = 1;
            for(int j = 0; j < i; j++) {
                if(Reads.compareIndices(array, j, j + 1, 0.025, true) > 0){
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                    consecSorted = 1;
                } else consecSorted++;
            }
        }
    }
}

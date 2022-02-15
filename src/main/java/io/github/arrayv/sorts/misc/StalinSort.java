package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class StalinSort extends Sort {
    public StalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stalin");
        this.setRunAllSortsName("Stalin Sort");
        this.setRunSortName("Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                Writes.write(array, i, array[i - 1], 0.5, true, false);
            }
        }
    }
}

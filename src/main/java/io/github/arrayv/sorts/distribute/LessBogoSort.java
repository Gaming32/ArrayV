package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Less Bogosort repeatedly shuffles the array,
 * dropping the first remaining element when it is in the correct place.
 */
public final class LessBogoSort extends BogoSorting {
    public LessBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Less Bogo");
        this.setRunAllSortsName("Less Bogo Sort");
        this.setRunSortName("Less Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length; ++i) {
            while (!this.isMinSorted(array, i, length))
                this.bogoSwap(array, i, length, false);
            Highlights.markArray(3, i);
        }
    }
}

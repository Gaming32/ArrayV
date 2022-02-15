package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Exchange Bogosort randomly sorts any two elements until the array is sorted.
 */
public final class ExchangeBogoSort extends BogoSorting {
    public ExchangeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Exchange Bogo");
        this.setRunAllSortsName("Exchange Bogo Sort");
        this.setRunSortName("Exchange Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isRangeSorted(array, 0, length, false, true)) {
            int index1 = BogoSorting.randInt(0, length),
                index2 = BogoSorting.randInt(0, length);

            int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
            if (index1 < index2 ? comp > 0 : comp < 0)
                Writes.swap(array, index1, index2, this.delay, true, false);
        }
    }
}

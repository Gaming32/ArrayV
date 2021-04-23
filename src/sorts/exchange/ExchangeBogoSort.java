package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

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
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length)) {
            int index1 = BogoSorting.randInt(0, length),
                index2 = BogoSorting.randInt(0, length);

            int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
            if (index1 < index2 ? comp > 0 : comp < 0)
                Writes.swap(array, index1, index2, this.delay, true, false);
        }
    }
}

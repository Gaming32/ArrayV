package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Slice Bogosort repeatedly shuffles random ranges of the array until it is sorted.
 */
public final class SliceBogoSort extends BogoSorting {
    public SliceBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slice Bogo");
        this.setRunAllSortsName("Slice Bogo Sort");
        this.setRunSortName("Slice Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length)) {
            int index1 = BogoSorting.randInt(0, length),
                index2 = BogoSorting.randInt(0, length);

            Highlights.markArray(3, index1);
            Highlights.markArray(4, index2);

            if (index1 < index2)
                this.bogoSwap(array, index1, index2+1, false);
            else
                this.bogoSwap(array, index2, index1+1, false);
        }
    }
}

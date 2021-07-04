package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author mingyue12
 *
 */
public final class ReverseBubbleSort extends Sort {

    /**
     * @param arrayVisualizer
     */
    public ReverseBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Bubble");
        this.setRunAllSortsName("Reverse Bubble Sort");
        this.setRunSortName("Reverse Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {

        for (int i = 0; i < sortLength - 1; i++) {
            boolean sorted = true;
            for (int j = sortLength - 1; j > i; j--) {
                if (Reads.compareIndices(array, j - 1, j, 0.05, true) == 1) {
                    Writes.swap(array, j - 1, j, 0.075, true, false);
                    sorted = false;
                }
            }
            if (sorted)
                break;
        }

    }

}

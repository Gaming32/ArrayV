package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class PopSort extends Sort {

    public PopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop");
        this.setRunAllSortsName("Pop Sort");
        this.setRunSortName("Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * Sorts the range {@code [start, end)} of {@code array} using Bubble Sort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     * @param fw    whether to sort ascending
     */
    public void bubbleSort(int[] array, int start, int end, boolean fw) {
        int cmp = fw ? 1 : -1;
        for (int i = end - 1; i > start; i--) {
            boolean sorted = true;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, 0.125, true) == cmp) {
                    Writes.swap(array, j, j + 1, 0.25, true, false);
                    sorted = false;
                }
            }
            if (sorted)
                break;
        }
    }

    /**
     * Sorts the range {@code [start, end)} of {@code array} using Popsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     */
    public void popSort(int[] array, int start, int end) {
        bubbleSort(array, start, start + ((end - start) / 4), false);
        bubbleSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        bubbleSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        bubbleSort(array, start + (3 * (end - start) / 4), end, true);
        bubbleSort(array, start, start + ((end - start) / 2), false);
        bubbleSort(array, start + ((end - start) / 2), end, true);
        bubbleSort(array, start, end, true);

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}

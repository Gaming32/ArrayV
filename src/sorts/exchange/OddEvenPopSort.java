package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 *
 */
public final class OddEvenPopSort extends Sort {

    public OddEvenPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Odd-Even Pop");
        this.setRunAllSortsName("Odd-Even Pop Sort");
        this.setRunSortName("Odd-Even Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);

    }

    /**
     * Sorts the range {@code [start, end)} of {@code array} using Odd-Even Sort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     * @param fw    whether to sort ascending
     */
    public void oddEvenSort(int[] array, int start, int end, boolean fw) {
        boolean sorted = false;
        int cmp = fw ? 1 : -1;
        while (!sorted) {
            sorted = true;
            for (int i = start + 1; i < end - 1; i += 2) {
                if (Reads.compareIndices(array, i, i + 1, 0.125, true) == cmp) {
                    Writes.swap(array, i, i + 1, 0.25, true, false);
                    sorted = false;
                }
            }
            for (int i = start; i < end - 1; i += 2) {
                if (Reads.compareIndices(array, i, i + 1, 0.125, true) == cmp) {
                    Writes.swap(array, i, i + 1, 0.25, true, false);
                    sorted = false;
                }
            }
        }
    }

    /**
     * Sorts the range {@code [start, end)} of {@code array} using Odd-Even Popsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     */
    public void popSort(int[] array, int start, int end) {
        oddEvenSort(array, start, start + ((end - start) / 4), false);
        oddEvenSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        oddEvenSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        oddEvenSort(array, start + (3 * (end - start) / 4), end, true);
        oddEvenSort(array, start, start + ((end - start) / 2), false);
        oddEvenSort(array, start + ((end - start) / 2), end, true);
        oddEvenSort(array, start, end, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}

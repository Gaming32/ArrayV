package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReverseClamberSort extends Sort {
    public ReverseClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Clamber");
        this.setRunAllSortsName("Reverse Clamber Sort");
        this.setRunSortName("Reverse Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = currentLength - 2;
        int right = currentLength - 1;
        while (left >= 0) {
            right = currentLength - 1;
            while (right > left) {
                Highlights.markArray(1, left);
                Highlights.markArray(2, right);
                Delays.sleep(0.1);
                if (Reads.compareValues(array[left], array[right]) > 0) {
                    Writes.swap(array, left, right, 0.1, true, false);
                }
                right--;
            }
            left--;
        }
    }
}
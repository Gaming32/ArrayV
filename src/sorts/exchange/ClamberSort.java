package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ClamberSort extends Sort {
    public ClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Clamber");
        this.setRunAllSortsName("Clamber Sort");
        this.setRunSortName("Clambersort");
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
        int left = 0;
        int right = 1;
        while (right < currentLength) {
            left = 0;
            while (left < right) {
                Highlights.markArray(1, left);
                Highlights.markArray(2, right);
                Delays.sleep(0.1);
                if (Reads.compareValues(array[left], array[right]) > 0) {
                    Writes.swap(array, left, right, 0.1, true, false);
                }
                left++;
            }
            right++;
        } 
    }
}
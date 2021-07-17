package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class InOrderShoveSort extends Sort {
    public InOrderShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Order Shove");
        this.setRunAllSortsName("In-Order Shove Sort");
        this.setRunSortName("In-Order Shove Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        int right = 1;
        int pull = 0;
        while (left < currentLength) {
            right = left + 1;
            while (right < currentLength) {
                Highlights.markArray(1, left);
                Highlights.markArray(2, right);
                Delays.sleep(0.125);
                if (Reads.compareValues(array[left], array[right]) > 0) {
                    pull = left;
                    while (pull + 1 < currentLength) {
                        Writes.swap(array, pull, pull + 1, 0.125, true, false);
                        pull++;
                    }
                    right = left + 1;
                } else {
                    right++;
                }
            }
            left++;
        } 
    }
}
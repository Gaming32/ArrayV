package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class FallSort extends Sort {
    public FallSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Falling");
        this.setRunAllSortsName("Falling Sort");
        this.setRunSortName("Fallsort");
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
        int left = 1;
        int right = 2;
        int highestlow = 0;
        while (left <= currentLength) {
            right = left + 1;
            highestlow = 0;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.001);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    if (highestlow == 0) {
                        highestlow = right;
                    } else {
                        Highlights.markArray(1, highestlow - 1);
                        Highlights.markArray(2, right - 1);
                        Delays.sleep(0.001);
                        if (Reads.compareValues(array[highestlow - 1], array[right - 1]) < 0) {
                            highestlow = right;
                        }
                    }
                }
                right++;
            }
            if (highestlow == 0) {
                left++;
            } else {
                Writes.swap(array, left - 1, highestlow - 1, 0.001, true, false);
            }
        }
    }
}
package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class TumbleweedSort extends Sort {
    public TumbleweedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tumbleweed");
        this.setRunAllSortsName("Tumbleweed Sort");
        this.setRunSortName("Tumbleweed Sort");
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
        int lowesthigh = 1;
        int right = 2;
        int pull = 1;
        boolean anyless = false;
        while (left != currentLength) {
            lowesthigh = left;
            right = left + 1;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.005);
                if (Reads.compareValues(array[left - 1], array[right - 1]) < 1) {
                    if (lowesthigh == left) {
                        lowesthigh = right;
                    } else {
                        Highlights.markArray(1, lowesthigh - 1);
                        Highlights.markArray(2, right - 1);
                        Delays.sleep(0.005);
                        if (Reads.compareValues(array[lowesthigh - 1], array[right - 1]) > 0) {
                            lowesthigh = right;
                        }
                    }
                }
                right++;
            }
            pull = left;
            if (lowesthigh == left) {
                while (pull + 1 <= currentLength) {
                    Writes.swap(array, pull - 1, pull, 0.005, true, false);
                    pull++;
                }
            } else {
                if (lowesthigh == left + 1) {
                    right = left + 1;
                    anyless = false;
                    while (right <= currentLength && !anyless) {
                        Highlights.markArray(1, left - 1);
                        Highlights.markArray(2, right - 1);
                        Delays.sleep(0.005);
                        if (Reads.compareValues(array[left - 1], array[right - 1]) == 1) {
                            anyless = true;
                        } else {
                            right++;
                        }
                    }
                    if (!anyless) {
                        left++;
                    } else {
                        while (pull + 1 <= currentLength) {
                            Writes.swap(array, pull - 1, pull, 0.005, true, false);
                            pull++;
                        } 
                    }
                } else {
                    while (pull + 1 != lowesthigh) {
                        Writes.swap(array, pull - 1, pull, 0.005, true, false);
                        pull++;
                    }
                }
            }
        }
    }
}
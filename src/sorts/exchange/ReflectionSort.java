package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Reflection Sort is an algorithm developed with unwritten impracticalities.

Reflection Sort starts running a Cocktail Shaker. When a swap is found, the
target position is then set to a reflection across the length of the list.
Upon reaching either bound of the list, exactly like Cocktail Shaker Sort,
the search direction is turned opposite, and the swap hunt restarts. This
continues until a sweep in descending order can be made without any swaps.

*/
final public class ReflectionSort extends Sort {
    public ReflectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reflection");
        this.setRunAllSortsName("Reflection Sort");
        this.setRunSortName("Reflection Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswap = true;
        int i = 0;
        while (anyswap) {
            i = 0;
            anyswap = false;
            while (i < currentLength) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.125);
                if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                    Writes.swap(array, i, i + 1, 0.125, true, false);
                    i = (currentLength - 1) - i;
                    anyswap = true;
                } else {
                    i++;
                }
            }
            i = currentLength;
            anyswap = false;
            while (i > 1) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.125);
                if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                    Writes.swap(array, i - 1, i, 0.125, true, false);
                    i = (currentLength - 1) - i;
                    anyswap = true;
                } else {
                    i--;
                }
            }
        }
    }
}
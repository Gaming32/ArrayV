package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AsteraceaeSort extends Sort {
    public AsteraceaeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Asteraceae");
        this.setRunAllSortsName("Asteraceae Sort");
        this.setRunSortName("Asteraceae Sort");
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
        int i = 1;
        int firstswap = 2;
        boolean anyswaps = true;
        boolean lastswap = false;
        while (anyswaps) {
            if (firstswap - 1 == 0) {
                i = 1;
            } else {
                i = firstswap - 1;
            }
            anyswaps = false;
            lastswap = false;
            while (i + 1 <= currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.1);
                if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                    Writes.swap(array, i - 1, i, 0.1, true, false);
                    i++;
                    if (!anyswaps) {
                        firstswap = i - 1;
                    }
                    anyswaps = true;
                    lastswap = true;
                } else {
                    if (lastswap) {
                        i += (int) Math.floor(Math.sqrt(currentLength));
                    } else {
                        i++;
                    }
                    lastswap = false;
                }
            }
        }
    }
}
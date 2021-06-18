package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReverseNaturalSort extends Sort {
    public ReverseNaturalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Natural");
        this.setRunAllSortsName("Reverse Natural Sort");
        this.setRunSortName("Reverse Naturalsort");
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
        int verifyi = currentLength;
        boolean anyswaps = true;
        boolean verifypass = false;
        while (!verifypass) {
            i = verifyi - 1;
            anyswaps = true;
            while (i > 0 && anyswaps) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                    Writes.swap(array, i - 1, i, 0.05, true, false);
                } else {
                    anyswaps = false;
                }
                i--;
            }
            if (verifyi < currentLength - 1) {
                verifyi++;
            } else {
                verifyi = currentLength - 1;
            }
            verifypass = true;
            while (verifyi > 0 && verifypass) {
                Highlights.markArray(1, verifyi - 1);
                Highlights.markArray(2, verifyi);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[verifyi - 1], array[verifyi]) <= 0) {
                    verifyi--;
                } else {
                    verifypass = false;
                    Writes.swap(array, verifyi - 1, verifyi, 0.05, true, false);
                }
            }
        }
    }
}
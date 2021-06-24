package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CocktailPushSort extends Sort {
    public CocktailPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Push");
        this.setRunAllSortsName("Cocktail Push Sort");
        this.setRunSortName("Cocktail Pushsort");
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
        boolean anyswaps = true;
        int i = 1;
        int gap = 1;
        while (anyswaps) {
            anyswaps = false;
            i = 1;
            gap = 1;
            while (i + gap <= currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i - 1], array[(i - 1) + gap]) > 0) {
                    for (int j = 1; j <= gap; j++) {
                        Writes.swap(array, i - 1, (i - 1) + j, 0.01, true, false);
                    }
                    anyswaps = true;
                    gap++;
                } else {
                    i++;
                }
            }
            i = currentLength;
            gap = 1;
            while (i - gap > 0) {
                Highlights.markArray(1, (i - 1) - gap);
                Highlights.markArray(2, i - 1);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[(i - 1) - gap], array[i - 1]) > 0) {
                    for (int j = 1; j <= gap; j++) {
                        Writes.swap(array, i - 1, (i - 1) - j, 0.01, true, false);
                    }
                    anyswaps = true;
                    gap++;
                } else {
                    i--;
                }
            }
        }
    }
}
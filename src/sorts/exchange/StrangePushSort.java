package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StrangePushSort extends Sort {
    public StrangePushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strange Push");
        this.setRunAllSortsName("Strange Push Sort");
        this.setRunSortName("Strange Pushsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2)
            return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswaps = true;
        int i = 1;
        int gap = 1;
        int base = bucketCount - 1;
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
                    gap += base * gap;
                } else {
                    i++;
                }
            }
        }
    }
}
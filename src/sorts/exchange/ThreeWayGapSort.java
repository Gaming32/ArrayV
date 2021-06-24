package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author thatsOven
 *
 */
public final class ThreeWayGapSort extends Sort {

    public ThreeWayGapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("3-Way Gap");
        setRunAllSortsName("3-Way Gap Sort");
        setRunSortName("3-Way Gap Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    public int[] backCheckSorted(int[] arr, int end, int start) {
        int[] returnarr = new int[2];
        for (int i = end - 1; i > start + 1; i--) {
            if (this.Reads.compareValues(arr[i], arr[i - 1]) < 0) {
                returnarr[0] = 0;
                returnarr[1] = i + 3;
                return returnarr;
            }
        }
        returnarr[0] = 1;
        returnarr[1] = end;
        return returnarr;
    }

    public void threeWayCompareAndSwap(int[] arr, int a, int m, int b) {
        if (this.Reads.compareValues(arr[a], arr[m]) > 0) {
            this.Writes.swap(arr, a, m, 1.0D, true, false);
        }
        if (this.Reads.compareValues(arr[m], arr[b]) > 0) {
            this.Writes.swap(arr, m, b, 1.0D, true, false);
        }
        if (this.Reads.compareValues(arr[a], arr[m]) > 0) {
            this.Writes.swap(arr, a, m, 1.0D, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] condition = new int[2];

        condition[0] = 0;
        condition[1] = currentLength;
        while (condition[0] == 0) {
            int gap = condition[1] / 3;
            while (gap > 0) {
                int start = 0;
                while (start + gap * 2 < condition[1]) {
                    threeWayCompareAndSwap(array, start, start + gap, start + gap * 2);
                    start += gap;
                }
                gap = gap * 10 / 13;
            }
            condition = backCheckSorted(array, condition[1], 0);
        }

    }

}

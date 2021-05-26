package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author fungamer2
 *
 */
public final class TernarySlowSort extends Sort {

    public TernarySlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Ternary Slow");
        setRunAllSortsName("Ternary Slow Sort");
        setRunSortName("Ternary Slowsort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(512);
        setBogoSort(false);

    }

    private void compareSwap(int[] array, int start, int end, int sleep) {
        if (this.Reads.compareValues(array[start], array[end]) == 1) {
            this.Writes.swap(array, start, end, sleep, true, false);
        }
        this.Highlights.markArray(1, start);
        this.Highlights.markArray(2, end);
        this.Delays.sleep(sleep);
    }

    public void ternarySlowSort(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            compareSwap(array, start, end, 1);
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            int mid1 = start + third;
            int mid2 = start + 2 * third;

            ternarySlowSort(array, start, mid1 - 1);
            ternarySlowSort(array, mid1, mid2 - 1);
            ternarySlowSort(array, mid2 - 1, end);

            compareSwap(array, mid1 - 1, end, 1);
            compareSwap(array, mid1 - 1, mid2 - 1, 1);
            compareSwap(array, mid2 - 1, end, 1);

            ternarySlowSort(array, start, end - 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        ternarySlowSort(array, 0, sortLength - 1);

    }

}

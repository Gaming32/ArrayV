package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author yuji
 * @author McDude_73
 *
 */
public final class OrangeSort extends Sort {

    public OrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Orange");
        setRunAllSortsName("Orange Sort");
        setRunSortName("Orange Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            int i, gap;
            for (i = 0, gap = 1; i + gap <= length; i++) {
                if (this.Reads.compareValues(array[i], array[i + gap]) > 0) {
                    this.Writes.swap(array, i, i + gap, 0.5D, true, false);
                    sorted = false;
                    gap++;
                } else if (gap >= 2) {
                    gap--;
                }
            }

            while (i - gap > 1) {
                if (this.Reads.compareValues(array[i - gap], array[i]) > 0) {
                    this.Writes.swap(array, i, i - gap, 0.5D, true, false);
                    sorted = false;
                } else if (gap >= 2) {
                    gap--;
                }

                i--;
            }
        }

    }

}

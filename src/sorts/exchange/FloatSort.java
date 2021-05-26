package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Lancewer
 * @author McDude_73
 *
 */
public final class FloatSort extends Sort {

    public FloatSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Float");
        setRunAllSortsName("Float Sort");
        setRunSortName("Float Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(8192);
        setBogoSort(false);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;
        while (!sorted) {
            int h = 0;
            sorted = true;
            for (int g = length - 1; g > 0; g--) {
                int i = h;
                int j = h + 1;

                this.Highlights.markArray(1, i);
                this.Highlights.markArray(2, j);

                while (i >= 0 && this.Reads.compareValues(array[i], array[j]) > 0) {
                    this.Writes.swap(array, i, j, 0.5D, true, false);
                    sorted = false;
                    i--;
                    j--;
                }

                this.Delays.sleep(0.25D);

                if (i >= 0) {
                    i++;
                    j++;
                    while (j < length && this.Reads.compareValues(array[i], array[j]) > 0) {
                        this.Writes.swap(array, i, j, 0.5D, true, false);
                        sorted = false;
                        i++;
                        j++;
                    }

                    this.Delays.sleep(0.25D);
                }

                h++;
            }
        }

    }

}

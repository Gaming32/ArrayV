package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author frankblob
 * @author PiotrGrochowski
 *
 */
public final class ShoveSort extends Sort {

    public ShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Shove");
        setRunAllSortsName("Shove Sort");
        setRunSortName("Shove Sort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(512);
        setBogoSort(false);

    }

    private void shovesort(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < end - 1) {
            this.Highlights.markArray(1, i);
            this.Highlights.markArray(2, i + 1);
            this.Delays.sleep(sleep);
            if (this.Reads.compareValues(array[i], array[i + 1]) == 1) {
                for (int f = i; f < end - 1; f++) {
                    this.Writes.swap(array, f, f + 1, sleep, true, false);
                }
                if (i > start) {
                    i--;
                }
                continue;
            }
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        shovesort(array, 0, length, 0.125D);

    }

}

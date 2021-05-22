package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 *
 */
public final class AwkwardSort extends Sort {

    public AwkwardSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Awkward");
        setRunAllSortsName("Awkward Sort");
        setRunSortName("Awkward Sort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(4096);
        setBogoSort(false);

    }

    private void awkward(int[] arr, int l, int pos) {
        if (l == 1) {
            return;
        }
        awkward(arr, l / 2, pos);
        awkward(arr, l / 2 + l % 2, pos + l / 2);

        for (int i = 0; i < l / 2; i++) {
            int a = pos + i;
            int b = pos + l / 2 + l % 2 + i;

            this.Highlights.markArray(1, a);
            this.Highlights.markArray(2, b);
            this.Delays.sleep(0.5D);
            if (this.Reads.compareValues(arr[a], arr[b]) == 1) {
                this.Writes.swap(arr, a, b, 1.0D, true, false);
            }
        }
        awkward(arr, l / 2 + l % 2, pos + l / 4);

        awkward(arr, l / 2, pos);
        awkward(arr, l / 2 + l % 2, pos + l / 2);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        awkward(array, sortLength, 0);

    }

}

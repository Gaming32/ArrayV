package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

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
			
            if (this.Reads.compareIndices(arr, a, b, 0.02D, true) == 1) {
                this.Writes.swap(arr, a, b, 0.02D, true, false);
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

package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Binary Gnome", runName = "Optimized Gnome Sort + Binary Search")
public final class BinaryGnomeSort extends Sort {
    public BinaryGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = 1; i < sortLength; i++) {
            int num = array[i];

            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2);

                Highlights.markArray(1, lo);
                Highlights.markArray(3, mid);
                Highlights.markArray(2, hi);

                Delays.sleep(1);

                if (Reads.compareValues(num, array[mid]) < 0) { // do NOT shift equal elements past each other; this
                                                                // maintains stability!
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            Highlights.clearMark(1);
            Highlights.clearMark(2);

            int j = i;
            while (j > lo) {
                Writes.swap(array, j, j - 1, 0.05, true, false);
                j--;
            }
        }
    }
}

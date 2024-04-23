package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Merge-Exchange", runName = "Batcher's Merge-Exchange Sorting Network")
public final class MergeExchangeSortIterative extends Sort {

    public MergeExchangeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int t = (int) (Math.log(length - 1) / Math.log(2)) + 1;
        int p0 = 1 << (t - 1);
        for (int p = p0; p > 0; p >>= 1) {
            int q = p0;
            int r = 0;
            int d = p;
            while (true) {
                for (int i = 0; i < length - d; i++) {
                    if ((i & p) == r && Reads.compareIndices(array, i, i + d, 0.5, true) == 1) {
                        Writes.swap(array, i, i + d, 0.5, true, false);
                    }
                }
                if (q == p)
                    break;
                d = q - p;
                q >>= 1;
                r = p;
            }
        }
    }
}

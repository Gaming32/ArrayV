package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class MergeExchangeSortIterative extends Sort {

    public MergeExchangeSortIterative(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);

        this.setSortListName("Merge-Exchange (Iterative)");
        this.setRunAllSortsName("Batcher's Merge-Exchange Sort");
        this.setRunSortName("Iterative Merge-Exchange Sort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int t = (int)(Math.log(length-1) / Math.log(2))+1;
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
                if (q == p) break;
                d = q - p;
                q >>= 1;
                r = p;
            }
        }
    }
}

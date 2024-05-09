package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This version of Bitonic Sort was taken from here, written by Nikos Pitsianis:
 * https://www2.cs.duke.edu/courses/fall08/cps196.1/Pthreads/bitonic.c
 *
 * Thanks to Piotr Grochowski for rewriting code to allow this implementation to
 * work for array lengths other than powers of two!
 */

@SortMeta(listName = "Bitonic (Iterative)", runName = "Iterative Bitonic Sorting Network")
public final class BitonicSortIterative extends Sort {
    public BitonicSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int i, j, k;

        for (k = 2; k < sortLength * 2; k = 2 * k) {
            boolean m = (((sortLength + (k - 1)) / k) % 2) != 0;

            for (j = k >> 1; j > 0; j = j >> 1) {
                for (i = 0; i < sortLength; i++) {
                    int ij = i ^ j;

                    if ((ij) > i && ij < sortLength) {
                        if ((((i & k) == 0) == m) && Reads.compareIndices(array, i, ij, 0.5, true) > 0)
                            Writes.swap(array, i, ij, 0.5, true, false);
                        if ((((i & k) != 0) == m) && Reads.compareIndices(array, i, ij, 0.5, true) < 0)
                            Writes.swap(array, i, ij, 0.5, true, false);
                    }
                }
            }
        }
    }
}

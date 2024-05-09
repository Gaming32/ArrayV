package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This version of Bitonic Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/bitonic/oddn.htm
 */

@SortMeta(listName = "Bitonic (Recursive)", runName = "Recursive Bitonic Sorting Network")
public final class BitonicSortRecursive extends Sort {
    private boolean direction = true;

    public BitonicSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private static int greatestPowerOfTwoLessThan(int n) {
        int k = 1;
        while (k < n) {
            k = k << 1;
        }
        return k >> 1;
    }

    private void compare(int[] A, int i, int j, boolean dir) {
        int cmp = Reads.compareIndices(A, i, j, 0.5, true);

        if (dir == (cmp == 1))
            Writes.swap(A, i, j, 0.5, true, false);
    }

    private void bitonicMerge(int[] A, int lo, int n, boolean dir) {
        if (n > 1) {
            int m = greatestPowerOfTwoLessThan(n);

            for (int i = lo; i < lo + n - m; i++) {
                this.compare(A, i, i + m, dir);
            }

            this.bitonicMerge(A, lo, m, dir);
            this.bitonicMerge(A, lo + m, n - m, dir);
        }
    }

    private void bitonicSort(int[] A, int lo, int n, boolean dir) {
        if (n > 1) {
            int m = n / 2;
            this.bitonicSort(A, lo, m, !dir);
            this.bitonicSort(A, lo + m, n - m, dir);
            this.bitonicMerge(A, lo, n, dir);
        }
    }

    public void changeDirection(String choice) throws Exception {
        if (choice.equals("forward"))
            this.direction = true;
        else if (choice.equals("backward"))
            this.direction = false;
        else
            throw new Exception("Invalid direction for Bitonic Sort!");
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.bitonicSort(array, 0, sortLength, this.direction);
    }
}

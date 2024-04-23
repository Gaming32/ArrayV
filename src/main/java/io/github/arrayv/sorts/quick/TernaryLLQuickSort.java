package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author Timo Bingmann
 *         Implemented in ArrayV by Gaming32
 *         https://github.com/bingmann/sound-of-sorting/blob/master/src/SortAlgo.cpp#L536-L595
 */
@SortMeta(listName = "Ternary LL Quick", runName = "Quicksort (ternary, LL ptrs)")
public final class TernaryLLQuickSort extends Sort {
    class PivotPair {
        int first, second;

        public PivotPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    public TernaryLLQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int compare(int[] A, int lo, int hi) {
        return Reads.compareIndices(A, lo, hi, 0.5, true);
    }

    // I'll just be using median-of-3 here
    private int selectPivot(int[] A, int lo, int hi) {
        int mid = (lo + hi) / 2;

        if (compare(A, lo, mid) == 0)
            return lo;
        if (compare(A, lo, hi - 1) == 0 || compare(A, mid, hi - 1) == 0)
            return hi - 1;

        return compare(A, lo, mid) < 0
                ? (compare(A, mid, hi - 1) < 0 ? mid : (compare(A, lo, hi - 1) < 0 ? hi - 1 : lo))
                : (compare(A, mid, hi - 1) > 0 ? mid : (compare(A, lo, hi - 1) < 0 ? lo : hi - 1));
    }

    private PivotPair partitionTernaryLL(int[] A, int lo, int hi) {
        int p = selectPivot(A, lo, hi);

        int pivot = A[p];
        Writes.swap(A, p, hi - 1, 1, true, false);
        Highlights.markArray(1, hi - 1);

        int i = lo, k = hi - 1;

        for (int j = lo; j < k; ++j) {
            int cmp = Reads.compareIndexValue(A, j, pivot, 0.5, true);
            if (cmp == 0) {
                Writes.swap(A, --k, j, 1, true, false);
                --j;
                Highlights.markArray(4, k);
            } else if (cmp < 0) {
                Writes.swap(A, i++, j, 1, true, false);
                Highlights.markArray(3, i);
            }
        }

        int j = i + (hi - k);

        for (int s = 0; s < hi - k; ++s) {
            Writes.swap(A, i + s, hi - 1 - s, 1, true, false);
        }
        Highlights.clearAllMarks();

        return new PivotPair(i, j);
    }

    private void quickSortTernaryLL(int[] A, int lo, int hi) {
        if (lo + 1 < hi) {
            PivotPair mid = partitionTernaryLL(A, lo, hi);

            quickSortTernaryLL(A, lo, mid.first);
            quickSortTernaryLL(A, mid.second, hi);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        quickSortTernaryLL(array, 0, currentLength);
    }
}

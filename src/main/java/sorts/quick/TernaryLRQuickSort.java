package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Timo Bingmann
 * Implemented in ArrayV by Gaming32
 * https://github.com/bingmann/sound-of-sorting/blob/master/src/SortAlgo.cpp#L449-L534
 */
final public class TernaryLRQuickSort extends Sort {
    public TernaryLRQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Ternary LR Quick");
        this.setRunAllSortsName("Quick Sort (ternary, LR ptrs)");
        this.setRunSortName("Quicksort (ternary, LR ptrs)");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    private void quickSortTernaryLR(int[] A, int lo, int hi) {
        if (hi <= lo) return;

        int cmp;

        int piv = selectPivot(A, lo, hi + 1);
        Writes.swap(A, piv, hi, 1, true, false);

        int pivot = A[hi];

        int i = lo, j = hi - 1;
        int p = lo, q = hi - 1;

        Highlights.markArray(3, j);

        for (;;) {
            while (i <= j && (cmp = Reads.compareIndexValue(A, i, pivot, 0.5, true)) <= 0) {
                if (cmp == 0) {
                    Highlights.markArray(4, p);
                    Writes.swap(A, i, p++, 1, true, false);
                }
                ++i;
                Highlights.markArray(3, i);
            }

            while (i <= j && (cmp = Reads.compareIndexValue(A, j, pivot, 0.5, true)) >= 0) {
                if (cmp == 0) {
                    Highlights.markArray(4, q);
                    Writes.swap(A, j, q--, 1, true, false);
                }
                --j;
                Highlights.markArray(3, j);
            }

            if (i > j) break;

            Writes.swap(A, i++, j--, 1, true, false);
            Highlights.markArray(3, j);
        }

        Writes.swap(A, i, hi, 1, true, false);

        int num_less = i - p;
        int num_greater = q - j;

        j = i - 1; i = i + 1;
        Highlights.markArray(3, i);

        int pe = lo + Math.min(p - lo, num_less);
        for (int k = lo; k < pe; k++, j--) {
            Writes.swap(A, k, j, 1, true, false);
            Highlights.markArray(3, j);
        }

        int qe = hi - 1 - Math.min(hi - 1 - q, num_greater - 1);
        for (int k = hi - 1; k > qe; k--, i++) {
            Writes.swap(A, i, k, 1, true, false);
            Highlights.markArray(3, i);
        }

        Highlights.clearAllMarks();

        quickSortTernaryLR(A, lo, lo + num_less - 1);
        quickSortTernaryLR(A, hi - num_greater + 1, hi);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        quickSortTernaryLR(array, 0, currentLength - 1);
    }
}

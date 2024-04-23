package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;

// code retrieved from https://codeblab.com/wp-content/uploads/2009/09/DualPivotQuicksort.pdf
// written by Vladimir Yaroslavskiy
@SortMeta(name = "Optimized Dual-Pivot Quick")
public final class OptimizedDualPivotQuickSort extends Sort {
    private InsertionSort insertSorter;

    public OptimizedDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void dualPivot(int[] array, int left, int right, int divisor) {
        int length = right - left;

        // insertion sort for tiny array
        if (length < 27) {
            Highlights.clearMark(2);
            insertSorter.customInsertSort(array, left, right + 1, 0.333, false);
            return;
        }

        int third = length / divisor;

        // "medians"
        int med1 = left + third;
        int med2 = right - third;

        if (med1 <= left) {
            med1 = left + 1;
        }
        if (med2 >= right) {
            med2 = right - 1;
        }
        if (Reads.compareIndices(array, med1, med2, 1, true) == -1) {
            Writes.swap(array, med1, left, 1, true, false);
            Writes.swap(array, med2, right, 1, true, false);
        } else {
            Writes.swap(array, med1, right, 1, true, false);
            Writes.swap(array, med2, left, 1, true, false);
        }

        // pivots
        int pivot1 = array[left];
        int pivot2 = array[right];

        // pointers
        int less = left + 1;
        int great = right - 1;

        Highlights.markArray(2, less);
        Highlights.markArray(3, great);

        // sorting
        for (int k = less; k <= great; k++) {
            if (Reads.compareIndexValue(array, k, pivot1, 0.5, true) == -1) {
                Writes.swap(array, k, less++, 0.5, false, false);
                Highlights.markArray(2, less);
            } else if (Reads.compareIndexValue(array, k, pivot2, 0.5, true) == 1) {
                while (k < great && Reads.compareIndexValue(array, great, pivot2, 0.5, false) == 1) {
                    great--;
                    Highlights.markArray(3, great);
                    Delays.sleep(0.5);
                }
                Writes.swap(array, k, great--, 0.5, false, false);
                Highlights.markArray(3, great);

                if (Reads.compareIndexValue(array, k, pivot1, 0.5, true) == -1) {
                    Writes.swap(array, k, less++, 0.5, false, false);
                    Highlights.markArray(2, less);
                }
            }
        }
        Highlights.clearAllMarks();

        // swaps
        int dist = great - less;

        if (dist < 13) {
            divisor++;
        }
        Writes.swap(array, less - 1, left, 1, true, false);
        Writes.swap(array, great + 1, right, 1, true, false);

        // subarrays
        this.dualPivot(array, left, less - 2, divisor);
        this.dualPivot(array, great + 2, right, divisor);

        Highlights.markArray(2, less);
        Highlights.markArray(3, great);

        // equal elements
        if (dist > length - 13 && pivot1 != pivot2) {
            for (int k = less; k <= great; k++) {
                if (Reads.compareIndexValue(array, k, pivot1, 0.5, true) == 0) {
                    Writes.swap(array, k, less++, 0.5, false, false);
                    Highlights.markArray(2, less);
                } else if (Reads.compareIndexValue(array, k, pivot2, 0.5, true) == 0) {
                    Writes.swap(array, k, great--, 0.5, false, false);
                    Highlights.markArray(3, great);

                    if (Reads.compareIndexValue(array, k, pivot1, 0.5, true) == 0) {
                        Writes.swap(array, k, less++, 0.5, false, false);
                        Highlights.markArray(2, less);
                    }
                }
            }
        }
        Highlights.clearAllMarks();

        // subarray
        if (pivot1 < pivot2) {
            this.dualPivot(array, less, great, divisor);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.insertSorter = new InsertionSort(this.arrayVisualizer);
        this.dualPivot(array, 0, currentLength - 1, 3);
    }
}

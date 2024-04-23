package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Quick (Left/Left)", runName = "Quick Sort, Left/Left Pointers")
public final class LLQuickSort extends Sort {
    public LLQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int i = lo;

        for (int j = lo; j < hi; j++) {
            Highlights.markArray(1, j);
            if (Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                i++;
            }
            Delays.sleep(1);
        }
        Writes.swap(array, i, hi, 1, true, false);
        return i;
    }

    private void quickSort(int[] array, int lo, int hi) {
        if (lo < hi) {
            int p = this.partition(array, lo, hi);
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p + 1, hi);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}

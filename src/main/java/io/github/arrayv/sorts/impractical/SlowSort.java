package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

// Code refactored from Python: http://wiki.c2.com/?SlowSort
@SortMeta(name = "Slow", slowSort = true, unreasonableLimit = 150)
public final class SlowSort extends Sort {
    public SlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void slowSort(int[] A, int i, int j) {
        if (i >= j) {
            return;
        }

        int m = i + ((j - i) / 2);

        this.slowSort(A, i, m);
        this.slowSort(A, m + 1, j);

        if (Reads.compareValues(A[m], A[j]) == 1) {
            Writes.swap(A, m, j, 1, true, false);
        }

        Highlights.markArray(1, j);
        Highlights.markArray(2, m);

        this.slowSort(A, i, j - 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.slowSort(array, 0, currentLength - 1);
    }
}

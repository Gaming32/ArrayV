package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Optimized Bubble")
public final class OptimizedBubbleSort extends Sort {
    public OptimizedBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int consecSorted;
        for (int i = length - 1; i > 0; i -= consecSorted) {
            consecSorted = 1;
            for (int j = 0; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, 0.025, true) > 0) {
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }
}

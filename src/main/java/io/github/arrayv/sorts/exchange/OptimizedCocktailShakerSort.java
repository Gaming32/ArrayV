package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Optimized Cocktail Shaker")
public final class OptimizedCocktailShakerSort extends Sort {
    public OptimizedCocktailShakerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int start = 0, end = length - 1; start < end;) {
            int consecSorted = 1;
            for (int i = start; i < end; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
            end -= consecSorted;

            consecSorted = 1;
            for (int i = end; i > start; i--) {
                if (Reads.compareIndices(array, i - 1, i, 0.025, true) > 0) {
                    Writes.swap(array, i - 1, i, 0.075, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
            start += consecSorted;
        }
    }
}

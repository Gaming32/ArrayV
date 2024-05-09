package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Stalin")
public final class StalinSort extends Sort {
    public StalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                Writes.write(array, i, array[i - 1], 0.5, true, false);
            }
        }
    }
}

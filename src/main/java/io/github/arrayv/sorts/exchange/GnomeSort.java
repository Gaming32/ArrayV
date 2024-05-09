package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Gnome")
public final class GnomeSort extends Sort {
    public GnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    // Code retrieved from http://www.algostructure.com/sorting/gnomesort.php

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length;) {
            if (Reads.compareIndices(array, i, i - 1, 0.04, true) >= 0) {
                i++;
            } else {
                Writes.swap(array, i, i - 1, 0.02, true, false);

                if (i > 1) {
                    i--;
                }
            }
        }
    }
}

package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This version of Odd-Even Sort was taken from here, written by Rachit Belwariar:
 * https://www.geeksforgeeks.org/odd-even-sort-brick-sort/
 */
@SortMeta(name = "Odd-Even")
public final class OddEvenSort extends Sort {
    public OddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            for (int i = 1; i < length - 1; i += 2) {
                if (Reads.compareIndices(array, i, i + 1, 0.05, true) == 1) {
                    Writes.swap(array, i, i + 1, 0, true, false);
                    sorted = false;
                }
            }

            for (int i = 0; i < length - 1; i += 2) {
                if (Reads.compareIndices(array, i, i + 1, 0.05, true) == 1) {
                    Writes.swap(array, i, i + 1, 0, true, false);
                    sorted = false;
                }
            }
        }
    }
}

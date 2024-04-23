package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Bozosort randomly swaps any two elements until the array is sorted.
 */
@SortMeta(name = "Bozo", slowSort = true, bogoSort = true, unreasonableLimit = 11)
public final class BozoSort extends BogoSorting {
    public BozoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length))
            Writes.swap(array, BogoSorting.randInt(0, length), BogoSorting.randInt(0, length), this.delay, true, false);
    }
}

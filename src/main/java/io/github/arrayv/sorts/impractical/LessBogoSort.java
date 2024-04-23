package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Less Bogosort repeatedly shuffles the array,
 * dropping the first remaining element when it is in the correct place.
 */
@SortMeta(name = "Less Bogo", slowSort = true, bogoSort = true, unreasonableLimit = 512)
public final class LessBogoSort extends BogoSorting {
    public LessBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length; ++i) {
            while (!this.isMinSorted(array, i, length))
                this.bogoSwap(array, i, length, false);
            Highlights.markArray(3, i);
        }
    }
}

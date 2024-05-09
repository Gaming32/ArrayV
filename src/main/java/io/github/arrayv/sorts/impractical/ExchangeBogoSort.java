package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Exchange Bogosort randomly sorts any two elements until the array is sorted.
 */
@SortMeta(name = "Exchange Bogo", slowSort = true, bogoSort = true, unreasonableLimit = 512)
public final class ExchangeBogoSort extends BogoSorting {
    public ExchangeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isRangeSorted(array, 0, length, false, true)) {
            int index1 = BogoSorting.randInt(0, length),
                    index2 = BogoSorting.randInt(0, length);

            int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
            if (index1 < index2 ? comp > 0 : comp < 0)
                Writes.swap(array, index1, index2, this.delay, true, false);
        }
    }
}

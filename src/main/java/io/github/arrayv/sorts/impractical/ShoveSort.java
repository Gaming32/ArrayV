package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author frankblob
 * @author PiotrGrochowski
 *
 */
@SortMeta(name = "Shove", slowSort = true, unreasonableLimit = 512)
public final class ShoveSort extends Sort {

    public ShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

    }

    private void shovesort(int[] array, int start, int end, double sleep) {
        int i = start;
        while (i < end - 1) {
            this.Highlights.markArray(1, i);
            this.Highlights.markArray(2, i + 1);
            this.Delays.sleep(sleep);
            if (this.Reads.compareValues(array[i], array[i + 1]) == 1) {
                for (int f = i; f < end - 1; f++) {
                    this.Writes.swap(array, f, f + 1, sleep, true, false);
                }
                if (i > start) {
                    i--;
                }
                continue;
            }
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        shovesort(array, 0, length, 0.125D);

    }

}

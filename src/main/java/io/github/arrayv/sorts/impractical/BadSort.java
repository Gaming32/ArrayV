package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This example of an O(n^3) sorting algorithm may be found here, written by James Jensen (StriplingWarrayior on StackOverflow):
 * https://stackoverflow.com/questions/27389344/is-there-a-sorting-algorithm-with-a-worst-case-time-complexity-of-n3
 */
@SortMeta(name = "Bad", slowSort = true, unreasonableLimit = 2048)
public final class BadSort extends Sort {
    public BadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        for (int i = 0; i < currentLen; i++) {
            int shortest = i;
            Delays.sleep(0.05);

            for (int j = i; j < currentLen; j++) {
                Highlights.markArray(1, j);
                Delays.sleep(0.05);

                boolean isShortest = true;
                for (int k = j + 1; k < currentLen; k++) {

                    if (Reads.compareIndices(array, j, k, 0.05, true) == 1) {
                        isShortest = false;
                        break;
                    }
                }
                if (isShortest) {
                    shortest = j;
                    break;
                }
            }
            Writes.swap(array, i, shortest, 0.05, true, false);
        }
    }
}

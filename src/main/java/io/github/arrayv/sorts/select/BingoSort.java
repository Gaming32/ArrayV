package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * Bingo Sort is a variant of Selection Sort which looks through all elements, using the
 * element with the maximum VALUE instead of the item to be swapped instead of the item.
 *
 * This is best suited to use when there are duplicate values in the array because the
 * sort will run quicker (similar to Counting Sort) - running on O(n+m^2) best case scenario,
 * otherwise it will run at O(n*m) time complexity,
 * where "m" is the amount of unique values in the array.
 *
 * >> Original source(s):
 * >> https://en.wikipedia.org/wiki/Selection_sort#Variants
 * >> https://xlinux.nist.gov/dads/HTML/bingosort.html
 *
 * >> Imported and Translated (from Pascal) by Joel "McDude73" Zaleschuk
 */
@SortMeta(name = "Bingo")
public final class BingoSort extends Sort {
    public BingoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        double sleep = Math.min(Math.max(10d / this.arrayVisualizer.getUniqueItems(), 0.001), 1);

        int maximum = length - 1;
        int next = array[maximum];

        for (int i = maximum - 1; i >= 0; i--) {
            if (array[i] > next) {
                next = array[i];
            }
        }
        while (maximum > 0 && array[maximum] == next) {
            maximum--;
        }
        while (maximum > 0) {
            int val = next;
            next = array[maximum];

            for (int j = maximum - 1; j >= 0; j--) {

                Highlights.markArray(1, array[j]);
                Highlights.markArray(2, val);

                if (Reads.compareValues(array[j], val) == 0) {
                    Writes.swap(array, j, maximum, 2 * sleep, true, false);
                    maximum--;

                } else {
                    if (array[j] > next) {
                        next = array[j];
                    }
                }
                Delays.sleep(sleep);
            }
            while (maximum > 0 && array[maximum] == next) {
                maximum--;
            }
        }
    }
}

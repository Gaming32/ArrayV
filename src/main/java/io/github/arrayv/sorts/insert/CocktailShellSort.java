package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author fungamer2
 *
 */
public final class CocktailShellSort extends Sort {

    /**
     * @param arrayVisualizer
     */
    public CocktailShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Shell");
        this.setRunAllSortsName("Cocktail Shell Sort");
        this.setRunSortName("Cocktail Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int gap = sortLength / 2;
        boolean dir = true;
        while (gap >= 1) {
            if (dir) {
                for (int i = gap; i < sortLength; i++) {
                    int tmp = array[i];
                    int j = i;
                    while (j >= gap && Reads.compareValues(array[j - gap], tmp) == 1) {
                        Highlights.markArray(2, j - gap);
                        Writes.write(array, j, array[j - gap], 0.7, true, false);
                        j -= gap;
                    }

                    if (j - gap >= 0) {
                        Highlights.markArray(2, j - gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            } else {
                for (int i = sortLength - gap; i >= 0; i--) {
                    int tmp = array[i];
                    int j = i;
                    while (j < sortLength - gap && this.Reads.compareValues(array[j + gap], tmp) == -1) {
                        Highlights.markArray(2, j + gap);
                        Writes.write(array, j, array[j + gap], 0.7, true, false);
                        j += gap;
                    }

                    if (j + gap < sortLength) {
                        Highlights.markArray(2, j + gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            }
            gap /= 2;
            dir = !dir;
        }

    }

}

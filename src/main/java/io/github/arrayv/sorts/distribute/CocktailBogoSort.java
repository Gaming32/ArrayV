package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Cocktail Bogosort is a bidirectional variation of Less Bogosort.
 * It repeatedly shuffles the array,
 * dropping first and last remaining elements when they are in the correct place.
 */
public final class CocktailBogoSort extends BogoSorting {
    public CocktailBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cocktail Bogo");
        this.setRunAllSortsName("Cocktail Bogo Sort");
        this.setRunSortName("Cocktail Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int min = 0;
        int max = length;

        while (min < max-1) {
            if (this.isMinSorted(array, min, max)) {
                Highlights.markArray(3, min);
                ++min;
                continue;
            }
            if (this.isMaxSorted(array, min, max)) {
                Highlights.markArray(4, max-1);
                --max;
                continue;
            }

            this.bogoSwap(array, min, max, false);
        }
    }
}

package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Cocktail Bogosort is a bidirectional variation of Less Bogosort.
 * It repeatedly shuffles the array,
 * dropping first and last remaining elements when they are in the correct
 * place.
 */
@SortMeta(name = "Cocktail Bogo", slowSort = true, bogoSort = true, unreasonableLimit = 1024)
public final class CocktailBogoSort extends BogoSorting {
    public CocktailBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int min = 0;
        int max = length;

        while (min < max - 1) {
            if (this.isMinSorted(array, min, max)) {
                Highlights.markArray(3, min);
                ++min;
                continue;
            }
            if (this.isMaxSorted(array, min, max)) {
                Highlights.markArray(4, max - 1);
                --max;
                continue;
            }

            this.bogoSwap(array, min, max, false);
        }
    }
}

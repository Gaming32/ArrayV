package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * Stable Bogo is a stable variant of Bogo Sort. It picks a random item and a direction (either left or right) and swaps
 it a random number of times, and stops when it has swapped enough, reached the edge or encounters an item with the same
 value.
 */
public final class StableBogoSort extends BogoSorting {
    public StableBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Bogo");
        this.setRunAllSortsName("Stable Bogo Sort");
        this.setRunSortName("Stable Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length)) {
        var i = BogoSorting.randInt(0, length);
        var distance = BogoSorting.randInt(0, length);
        var location = i + distance
        var dir = BogoSorting.randInt(0, 1);
        dir = (dir - 0.5)*2;
        var spot = i + dir;
        
        while (array[i] != array[spot] && i < (length + 1) && i > -1 && i != location){
          Writes.swap(array, i, spot, this.delay, true, false);
          i = i + dir;
            };
}
    }
}

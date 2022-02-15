package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

public final class OptimizedGuessSort extends BogoSorting {
    public OptimizedGuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Guess");
        this.setRunAllSortsName("Optimized Guess Sort");
        this.setRunSortName("Optimized Guess Sort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }

    // PROGRAMMER'S NOTE: This sort is intentionally bad, it is purposefully un-optimized.
    // OTHER PROGRAMMER'S NOTE: haha too bad not any more
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // "nested for-loop" with depth `length` to iterate over all n-tuples of indices
        int[] loops = Writes.createExternalArray(length);
        // we don't care if we've been through every n-tuple, we go down in flames!
        while (true) {
            // check if the array is stably sorted (doubles as duplicate-detection)
            boolean sorted = true;
            for (int i = 0; i < length-1; ++i) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i+1);
                Delays.sleep(this.delay);
                int comp = Reads.compareIndices(array, loops[i], loops[i+1], this.delay, true);
                if (comp < 0 || comp == 0 && loops[i] < loops[i+1])
                    continue;
                sorted = false;
                break;
            }
            Highlights.clearAllMarks();
            if (sorted)
                break;
            // progress the loops
            for (int pos = 0; pos < length; ++pos)
                if (loops[pos] < length - 1) {
                    Writes.write(loops, pos, loops[pos] + 1, this.delay, true, true);
                    break;
                } else
                    Writes.write(loops, pos, 0, this.delay, true, true);
        }
        // write the indexes to the array
        for (int i = 0; i < length; i++)
            Writes.write(loops, i, array[loops[i]], this.delay, true, true);
        for (int i = 0; i < length; i++)
            Writes.write(array, i, loops[i], this.delay, true, false);
        Writes.deleteExternalArray(loops);
    }
}

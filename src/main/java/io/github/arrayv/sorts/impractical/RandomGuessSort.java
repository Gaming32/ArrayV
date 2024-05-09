package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

@SortMeta(name = "Random Guess", slowSort = true, bogoSort = true, unreasonableLimit = 8)
public final class RandomGuessSort extends BogoSorting {
    public RandomGuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    // PROGRAMMER'S NOTE: This sort is intentionally bad, it is purposefully
    // un-optimized.
    // OTHER PROGRAMMER'S NOTE: haha too bad this isn't even the same sort anymore
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // just guessing the indices
        int[] loops = Writes.createExternalArray(length);
        // hello, randomness!
        while (true) {
            // check if the array is stably sorted (doubles as duplicate-detection)
            boolean sorted = true;
            for (int i = 0; i < length - 1; ++i) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(this.delay);
                int comp = Reads.compareIndices(array, loops[i], loops[i + 1], this.delay, true);
                if (comp < 0 || comp == 0 && loops[i] < loops[i + 1])
                    continue;
                sorted = false;
                break;
            }
            Highlights.clearAllMarks();
            if (sorted)
                break;
            // guess
            for (int pos = 0; pos < length; ++pos)
                Writes.write(loops, pos, BogoSort.randInt(0, length), this.delay, true, true);
        }
        // write the indexes to the array
        for (int i = 0; i < length; i++)
            Writes.write(loops, i, array[loops[i]], this.delay, true, true);
        for (int i = 0; i < length; i++)
            Writes.write(array, i, loops[i], this.delay, true, false);
        Writes.deleteExternalArray(loops);
    }
}

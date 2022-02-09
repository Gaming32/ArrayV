package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class SmartGuessSort extends BogoSorting {
    public SmartGuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Smart Guess");
        this.setRunAllSortsName("Smart Guess Sort");
        this.setRunSortName("Smart Guess Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(19);
        this.setBogoSort(false);
    }

    // PROGRAMMER'S NOTE: This sort is intentionally bad, it is purposefully un-optimized.
    // OTHER PROGRAMMER'S NOTE: haha too bad not any more at all
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // "nested for-loop" with depth `length` to iterate over all n-tuples of indices
        int[] loops = Writes.createExternalArray(length);
        // we don't care if we've "been through" every n-tuple, we go down in flames!
        while (true) {
            // check if the array is stably sorted (doubles as duplicate-detection)
            boolean sorted = true;
            int i = length-2;
            for (; i >= 0; --i) {
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
            // progress the loops (we skip ahead to progress the index where the out-of-order was detected)
            for (int pos = 0; pos < length; ++pos)
                if (pos >= i && loops[pos] < length - 1) {
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

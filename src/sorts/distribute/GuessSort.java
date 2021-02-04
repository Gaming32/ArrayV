package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class GuessSort extends Sort {
    private final double DELAY = 0.0001;   // trust me, this shouldn't be greater than tiny!
    public GuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Guess");
        this.setRunAllSortsName("Guess Sort");
        this.setRunSortName("Guess Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }
    
    // PROGRAMMER'S NOTE: This sort is intentionally bad, it is purposefully un-optimized.
    @Override
    public void runSort(int[] arr, int length, int buckets) {
        // nested for-loop with depth `length`
        int[] loops = Writes.createExternalArray(length);
        int[] indexes = Writes.createExternalArray(length);
        int pos = length;
        while (pos >= 0) {
            // check to make sure the indexes are unique
            int total = 0;
            for (int i = 0; i < length; i++)
                for (int j = 0; j < length; j++)
                    if (Reads.compareIndices(loops, i, j, DELAY, true) == 0)
                        total++;
            // check to make sure the resulting array is sorted
            for (int i = 0; i < length; i++)
                for (int j = 0; j < length; j++)
                    if (Reads.compareIndices(arr, loops[i], loops[j], DELAY, true) == (i < j ? 1 : -1))
                        total++;
            if (total == length)   // both tests have passed
                // save the loop state to the index array
                for (int i = 0; i < length; i++)
                    Writes.write(indexes, i, loops[i], DELAY, true, true);
            // progress the loops
            pos = length - 1;
            while (pos >= 0)
                if (loops[pos] < length - 1) {
                    Writes.write(loops, pos, loops[pos] + 1, DELAY, true, true);
                    break;
                } else
                    Writes.write(loops, pos--, 0, DELAY, true, true);
        }
        Writes.deleteExternalArray(loops);
        // write the indexes to the array
        int[] aux = Writes.createExternalArray(length);
        for (int i = 0; i < length; i++)
            Writes.write(aux, i, arr[i], DELAY, true, true);
        for (int i = 0; i < length; i++)
            Writes.write(arr, i, aux[indexes[i]], DELAY, true, false);
        Writes.deleteExternalArray(indexes);
        Writes.deleteExternalArray(aux);
    }
}

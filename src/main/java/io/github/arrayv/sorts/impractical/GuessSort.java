package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

@SortMeta(name = "Guess", slowSort = true, unreasonableLimit = 7)
public final class GuessSort extends BogoSorting {
    public GuessSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    // PROGRAMMER'S NOTE: This sort is intentionally bad, it is purposefully
    // un-optimized.
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // "nested for-loop" with depth `length` to iterate over all n-tuples of indices
        int[] loops = Writes.createExternalArray(length);
        int[] indexes = Writes.createExternalArray(length);
        while (true) {
            // check to make sure the indexes are unique
            int total = 0;
            for (int i = 0; i < length; ++i)
                for (int j = 0; j < length; ++j)
                    if (Reads.compareIndices(loops, i, j, this.delay, true) == 0)
                        ++total;
            // check to make sure the resulting array is sorted
            for (int i = 0; i < length; ++i)
                for (int j = 0; j < length; ++j)
                    if (Reads.compareIndices(array, loops[i], loops[j], this.delay, true) == (i < j ? 1 : -1))
                        ++total;
            // both tests have passed
            if (total == length)
                // save the loop state to the index array
                Writes.arraycopy(loops, 0, indexes, 0, length, this.delay, true, true);
            // progress the loops
            int pos = 0;
            while (pos < length)
                if (loops[pos] < length - 1) {
                    Writes.write(loops, pos, loops[pos] + 1, this.delay, true, true);
                    break;
                } else {
                    Writes.write(loops, pos, 0, this.delay, true, true);
                    ++pos;
                }
            if (pos == length)
                break;
        }
        Writes.deleteExternalArray(loops);
        // write the indexes to the array
        int[] aux = Writes.createExternalArray(length);
        for (int i = 0; i < length; ++i)
            Writes.write(aux, i, array[i], this.delay, true, true);
        for (int i = 0; i < length; ++i)
            Writes.write(array, i, aux[indexes[i]], this.delay, true, false);
        Writes.deleteExternalArray(indexes);
        Writes.deleteExternalArray(aux);
    }
}

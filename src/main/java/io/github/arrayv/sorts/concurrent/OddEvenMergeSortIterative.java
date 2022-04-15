package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 * This version of Odd-Even Merge Sort was taken from here, written by wkpark on StackOverflow:
 * https://stackoverflow.com/questions/34426337/how-to-fix-this-non-recursive-odd-even-merge-sort-algorithm
 *
 * Thanks to Piotr Grochowski for rewriting code to allow this implementation to
 * work for array lengths other than powers of two!
 */

@SortMeta(
    listName = "Odd-Even Merge (Iterative)",
    showcaseName = "Iterative Odd-Even Merge Sort",
    runName = "Iterative Odd-Even Mergesort"
)
public final class OddEvenMergeSortIterative extends Sort {
    public OddEvenMergeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int p = 1; p < sortLength; p += p)
            for (int k = p; k > 0; k /= 2)
                for (int j = k % p; j + k < sortLength; j += k + k)
                    for (int i = 0; i < k; i++)
                        if ((i + j)/(p + p) == (i + j + k)/(p + p))
                            if(i + j + k < sortLength)
                                if(Reads.compareIndices(array, i + j, i + j + k, 0.5, true) > 0)
                                    Writes.swap(array, i + j, i + j + k, 0.5, true, false);
    }
}

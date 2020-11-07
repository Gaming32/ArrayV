package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * This version of Odd-Even Merge Sort was taken from here, written by wkpark on StackOverflow:
 * https://stackoverflow.com/questions/34426337/how-to-fix-this-non-recursive-odd-even-merge-sort-algorithm
 * 
 * Thanks to Piotr Grochowski for rewriting code to allow this implementation to
 * work for array lengths other than powers of two!
 */

final public class IterativeOddEvenMergeSort extends Sort {
    public IterativeOddEvenMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Iter. Odd-Even Merge");
        this.setRunAllSortsName("Iterative Odd-Even Merge Sort");
        this.setRunSortName("Iterative Odd-Even Mergesort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int p = 1; p < sortLength; p += p)
            for (int k = p; k > 0; k /= 2)
                for (int j = k % p; j + k < sortLength; j += k + k)
                    for (int i = 0; i < k; i++)
                        if ((i + j)/(p + p) == (i + j + k)/(p + p)) {
                            if(i + j + k < sortLength) {
                                Highlights.markArray(1, i + j);
                                Highlights.markArray(2, i + j + k);
                                Delays.sleep(1);
                                if(Reads.compareValues(array[i + j], array[i + j + k]) > 0)
                                    Writes.swap(array, i + j, i + j + k, 1, true, false);
                            }
                        }
    }
}
package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BinaryQuickSorting;

/**
 * Binary MSD Radix Sort / Binary Quicksort.
 *
 * Implemented as recursive decent, and via task queue, see:
 * * binaryQuickSortRecursive, and
 * * binaryQuickSort respectively.
 *
 * Both of which are in-place sorting algorithms, with the recursive utilizing
 * the stack for divide-and-conquer, while the non-recursive utilizes a queue.
 *
 * Can be extended to support unsigned integers, by sorting the first bit rin
 * reverse. Can be made stable at the cost of O(n) memory. Can be parallalized
 * to O(log2(n)) subtasks / threads.
 *
 * @author Skeen
 */

public final class BinaryQuickSortRecursive extends BinaryQuickSorting {
    public BinaryQuickSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binary Quick (Recursive)");
        this.setRunAllSortsName("Recursive Binary Quick Sort");
        this.setRunSortName("Recursive Binary Quicksort");
        this.setCategory("Distribution Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int mostSignificantBit = Reads.analyzeBit(array, sortLength);
        this.binaryQuickSortRecursive(array, 0, sortLength - 1, mostSignificantBit);
    }
}

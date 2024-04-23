package io.github.arrayv.sorts.hybrid;

import javax.xml.crypto.Data;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Optimized Bottom-Up Merge")
public final class OptimizedBottomUpMergeSort extends Sort {
    public OptimizedBottomUpMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    /**
     * Iterative merge sort algorithm --- as a static method
     * 
     * @author: Sartaj Sahni, ported to Java by Timothy Rolfe
     * 
     * @see Data Structures, Algorithms, and Applications in C++,
     *      pp. 680-81 (the original in C++ has a memory leak, but
     *      that is not a problem in Java due to garbage collection)
     * 
     *      Minor revision by Timothy Rolfe (tjr) --- saves a comparison
     */

    // retrieved from
    // http://penguin.ewu.edu/cscd300/Topic/AdvSorting/MergeSorts/MergeIter.html
    private void merge(int[] c, int[] d, int lt, int md, int rt, boolean activeSound) {
        // Merge c[lt:md] and c[md+1:rt] to d[lt:rt]
        int i = lt, // cursor for first segment
                j = md + 1, // cursor for second
                k = lt; // cursor for result

        // merge until i or j exits its segment
        while ((i <= md) && (j <= rt)) {
            if (Reads.compareValues(c[i], c[j]) <= 0) {
                if (activeSound) {
                    Writes.write(d, k++, c[i++], 0.5, false, true);
                    Highlights.markArray(1, i);
                    Highlights.markArray(2, j);
                } else {
                    Writes.write(d, k++, c[i++], 0.5, false, false);
                    Highlights.markArray(1, k - 1);
                    Highlights.clearMark(2);
                }
            } else {
                if (activeSound) {
                    Writes.write(d, k++, c[j++], 0.5, false, true);
                    Highlights.markArray(1, j);
                    Highlights.markArray(2, i);
                } else {
                    Writes.write(d, k++, c[j++], 0.5, false, false);
                    Highlights.markArray(1, k - 1);
                    Highlights.clearMark(2);
                }
            }
            Delays.sleep(0.5);
        }
        // take care of left overs --- tjr code: only one while loop actually runs
        while (i <= md) {
            Writes.write(d, k++, c[i++], 1, true, activeSound);
        }
        while (j <= rt) {
            Writes.write(d, k++, c[j++], 1, true, activeSound);
        }

    } // end merge()

    /**
     * Perform one pass through the two arrays, invoking Merge() above
     */
    private void mergePass(int[] x, int[] y, int s, int n, boolean activeSound) {// Merge adjacent segments of size s.
        int i = 0;

        while (i <= n - 2 * s) {// Merge two adjacent segments of size s
            this.merge(x, y, i, i + s - 1, i + 2 * s - 1, activeSound);
            i = i + 2 * s;
        }
        // fewer than 2s elements remain
        if (i + s < n) {
            this.merge(x, y, i, i + s - 1, n - 1, activeSound);
        } else
            for (int j = i; j <= n - 1; j++) {
                Writes.write(y, j, x[j], 1, false, activeSound); // copy last segment to y
                Highlights.markArray(1, j);
                Highlights.clearMark(2);
            }

    }// end mergePass()

    /**
     * Entry point for merge sort
     */
    private void stableSort(int[] a, int n) {
        BinaryInsertionSort binaryInserter = new BinaryInsertionSort(this.arrayVisualizer);

        if (n < 16) {
            binaryInserter.customBinaryInsert(a, 0, 16, 0.35);
            return;
        }

        // Sort a[0:n-1] using merge sort.
        int s = 16; // segment size
        int[] b = Writes.createExternalArray(n);
        int i;

        for (i = 0; i <= n - 16; i += 16) {
            binaryInserter.customBinaryInsert(a, i, i + 16, 0.35);
        }
        binaryInserter.customBinaryInsert(a, i, n, 0.35);

        while (s < n) {
            this.mergePass(a, b, s, n, true); // merge from a to b
            s += s; // double the segment size
            this.mergePass(b, a, s, n, false); // merge from b to a
            s += s; // again, double the segment size
        } // end while
          // in C/C++, return the scratch array b by free/delete --- tjr
          // end mergeSort

        Writes.deleteExternalArray(b);
    }// end MergeArray class

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.stableSort(array, length);
    }
}

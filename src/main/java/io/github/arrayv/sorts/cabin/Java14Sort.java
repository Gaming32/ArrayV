package io.github.arrayv.sorts.cabin;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

import java.util.Arrays;
import java.util.Optional;

/**
 * JDK 14's dual-pivot Quicksort, which I've painstakingly adapted line by line from DualPivotQuicksort.java to call
 * into the ArrayV hooks. Although I've gutted all that parallel merge stuff, because it essentially calls into the main
 * algorithm on smaller segments in parallel, and merges them back in parallel. I'd rather just watch the main algorithm.
 * While this parallel merge piece is ostensibly what sets JDK 14's sort apart from its predecessors, there are still a
 * few little optimizations compared to JDK 11, including pivot selection and a new heapsort fallback (which I haven't
 * been able to trigger in ArrayV without changing the tuning constants).
 * <p/>
 * Unfortunately, this janked out copypasta is the only way to observe the standard sort. I thought of adding hooks into
 * the ArrayV code in a List implementor, but the collections framework actually dumps the List into an array and calls
 * Arrays::sort when you call List::sort. Plus, it uses a whole different algorithm for Comparables as opposed
 * to primitives.
 * <p/>
 * <b>Overview:</b>
 * <p/>
 * The algorithm is an Introsort variant at heart, but with so many safeguards, it's basically invincible.
 * <p/>
 * The core algorithm is a dual-pivot Quicksort. It selects the pivots using a weird median of 5 thing which is based
 * on the golden ratio, because of course it is. <b>Safeguard #1:</b> If any two of them are equal, it switches to a
 * single-pivot Quicksort for that range.
 * <p/>
 * <b>Safeguard #2:</b> Before trying Quicksort, it tries to find and merge runs. A run is defined as an ascending,
 * descending, or constant sequence of values (a constant sequence could technically be considered ascending <i>and</i>
 * descending, but it is handled slightly differently here). A descending sequence is reversed on the spot. Then, if
 * all the sequences are long enough, it will attempt to do an N-way merge on them. Otherwise, it will leave them alone
 * and defer to the core Quicksort loop.
 * <p/>
 * <b>Safeguard #3:</b> If the recursion delves too greedily and too deep, it will call Heapsort on that range. This is,
 * of course, a classic Introsort optimization. Interestingly, they set a <i>huge</i> depth on it, likely impossible to
 * reach without millions of elements.
 * <p/>
 * <b>Safeguard #4:</b> If called on a small enough range, it will call insertion sort. Another classic Introsort
 * optimization. But there are two versions of it... one is a regular insertion sort like you're used to. The other is a
 * so-called "mixed" insertion sort, which uses the pivot to do some sort of double-ended thing that helps cut down on
 * swaps. I find this fascinating.
 * <p/>
 * Suggested settings:
 * <ul>
 *     <li>Shape = modulo function, Shuffle = no shuffle, N = 32768, Style = Bar Graph</li>
 *     <li>Shape = modulo function, Shuffle = sawtooth, N = 32768, Style = Bar Graph</li>
 *     <li>Shape = modulo function, Shuffle = pipe organ, N = 32768, Style = Bar Graph</li>
 *     <li>Shape = square root, Shuffle = SMB3 (fixed), N = 32768, Style = Bar Graph</li>
 *     <li>Shape = sine wave, Shuffle = None, N = 32768, Style = Bar Graph</li>
 * </ul>
 */
@SuppressWarnings("StatementWithEmptyBody")
@SortMeta(
    name = "Java 14",
    runName = "Java 14",
    category = "Custom"
)
public class Java14Sort extends Sort {

    private static final double INSERTION_SORT_SLEEP = 0.5;
    private static final double QUICK_SORT_SLEEP = 0.5;
    private static final double RUN_MERGE_SLEEP = 0.5;
    private static final boolean includeRunIndicesInVisuals = false;

    private int sortLength;

    public Java14Sort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.sortLength = sortLength;
        sort(array, 0, sortLength);
    }

    // For mixedInsertionSort, which often receives awkward bounds.
    private int clamp(int i) {
        if (i < 0) {
            (new IndexOutOfBoundsException("" + i)).printStackTrace();
            return 0;
        } else if (i >= sortLength) {
            (new IndexOutOfBoundsException("" + i)).printStackTrace();
            return sortLength - 1;
        }
        // return Math.min(Math.max(i, 0), sortLength - 1);
        return i;
    }

// ============ ADAPTED FROM DualPivotQuicksort.java (jdk14), minus all the parallel crap. ============ \\
// ================= It doesn't use any fancy language features, so no problems here. ================= \\
// ================================= Original comments have been left intact. ========================= \\

    /**
     * Max array size to use insertion sort.
     */
    private static final int MAX_INSERTION_SORT_SIZE = 44;

    /**
     * Max array size to use mixed insertion sort.
     */
    private static final int MAX_MIXED_INSERTION_SORT_SIZE = 65;

    /**
     * Min array size to try merging of runs.
     */
    private static final int MIN_TRY_MERGE_SIZE = 4 << 10;

    /**
     * Min size of the first run to continue with scanning.
     */
    private static final int MIN_FIRST_RUN_SIZE = 16;

    /**
     * Min factor for the first runs to continue scanning.
     */
    private static final int MIN_FIRST_RUNS_FACTOR = 7;

    /**
     * Max capacity of the index array for tracking runs.
     */
    private static final int MAX_RUN_CAPACITY = 5 << 10;

    /**
     * Threshold of mixed insertion sort is incremented by this value.
     */
    private static final int DELTA = 3 << 1;

    /**
     * Max recursive partitioning depth before using heap sort.
     */
    private static final int MAX_RECURSION_DEPTH = 64 * DELTA;

    /**
     * Sorts the specified range of the array using <s>parallel merge
     * sort and/or</s> Dual-Pivot Quicksort.
     *
     * <s>To balance the faster splitting and parallelism of merge sort
     * with the faster element partitioning of Quicksort, ranges are
     * subdivided in tiers such that, if there is enough parallelism,
     * the four-way parallel merge is started, still ensuring enough
     * parallelism to process the partitions.</s>
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private void sort(int[] a, int low, int high) {
        sort(a, 0, low, high);
    }

    /**
     * Sorts the specified array using the Dual-Pivot Quicksort and/or
     * other sorts in special-cases, <s>possibly with parallel partitions</s>.
     *
     * @param a the array to be sorted
     * @param bits the combination of recursion depth and bit flag, where
     *        the right bit "0" indicates that array is the leftmost part
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private void sort(int[] a, int bits, int low, int high) {
        while (true) {
            int end = high - 1, size = high - low;

            /*
             * Run mixed insertion sort on small non-leftmost parts.
             */
            if (size < MAX_MIXED_INSERTION_SORT_SIZE + bits && (bits & 1) > 0) {
                mixedInsertionSort(a, low, high - 3 * ((size >> 5) << 3), high);
                return;
            }

            /*
             * Invoke insertion sort on small leftmost part.
             */
            if (size < MAX_INSERTION_SORT_SIZE) {
                insertionSort(a, low, high);
                return;
            }

            /*
             * Check if the whole array or large non-leftmost
             * parts are nearly sorted and then merge runs.
             */
            if ((bits == 0 || size > MIN_TRY_MERGE_SIZE && (bits & 1) > 0)
                && tryMergeRuns(a, low, size)) {
                return;
            }

            /*
             * Switch to heap sort if execution
             * time is becoming quadratic.
             */
            if ((bits += DELTA) > MAX_RECURSION_DEPTH) {
                heapSort(a, low, high);
                return;
            }

            /*
             * Use an inexpensive approximation of the golden ratio
             * to select five sample elements and determine pivots.
             */
            int step = (size >> 3) * 3 + 3;

            /*
             * Five elements around (and including) the central element
             * will be used for pivot selection as described below. The
             * unequal choice of spacing these elements was empirically
             * determined to work well on a wide variety of inputs.
             */
            int e1 = low + step;
            int e5 = end - step;
            int e3 = (e1 + e5) >>> 1;
            int e2 = (e1 + e3) >>> 1;
            int e4 = (e3 + e5) >>> 1;
            int a3 = a[e3];

            // Markers can have sparse IDs, and the positions can be unordered.
            // With that in mind, it's best to leave 1 and 2 for reads/writes/swaps and start indexing at 3.

            // Mark endpoints
            Highlights.markArray(3, low);
            Highlights.markArray(4, high - 1);

            // Mark pivot considerations
            Highlights.markArray(5, e1);
            Highlights.markArray(6, e2);
            Highlights.markArray(7, e3);
            Highlights.markArray(8, e4);
            Highlights.markArray(9, e5);

            /*
             * Sort these elements in place by the combination
             * of 4-element sorting network and insertion sort.
             *
             *    5 ------o-----------o------------
             *            |           |
             *    4 ------|-----o-----o-----o------
             *            |     |           |
             *    2 ------o-----|-----o-----o------
             *                  |     |
             *    1 ------------o-----o------------
             */
            if (Reads.compareIndices(a, e5, e2, QUICK_SORT_SLEEP, true) < 0)
                Writes.swap(a, e5, e2, QUICK_SORT_SLEEP, true, false);
            if (Reads.compareIndices(a, e4, e1, QUICK_SORT_SLEEP, true) < 0)
                Writes.swap(a, e4, e1, QUICK_SORT_SLEEP, true, false);
            if (Reads.compareIndices(a, e5, e4, QUICK_SORT_SLEEP, true) < 0)
                Writes.swap(a, e5, e4, QUICK_SORT_SLEEP, true, false);
            if (Reads.compareIndices(a, e2, e1, QUICK_SORT_SLEEP, true) < 0)
                Writes.swap(a, e2, e1, QUICK_SORT_SLEEP, true, false);
            if (Reads.compareIndices(a, e4, e2, QUICK_SORT_SLEEP, true) < 0)
                Writes.swap(a, e4, e2, QUICK_SORT_SLEEP, true, false);

            // TODO: compareIndices(e3, e2)?
            if (Reads.compareValueIndex(a, a3, e2, QUICK_SORT_SLEEP, true) < 0) {
                if (Reads.compareValueIndex(a, a3, e1, QUICK_SORT_SLEEP, true) < 0) {
                    Writes.write(a, e3, a[e2], QUICK_SORT_SLEEP, true, false);
                    Writes.write(a, e2, a[e1], QUICK_SORT_SLEEP, true, false);
                    Writes.write(a, e1, a3, QUICK_SORT_SLEEP, true, false);
                } else {
                    Writes.swap(a, e2, e3, QUICK_SORT_SLEEP, true, false);
                }
            } else if (Reads.compareValueIndex(a, a3, e4, QUICK_SORT_SLEEP, true) > 0) {
                if (Reads.compareValueIndex(a, a3, e5, QUICK_SORT_SLEEP, true) > 0) {
                    Writes.write(a, e3, a[e4], QUICK_SORT_SLEEP, true, false);
                    Writes.write(a, e4, a[e5], QUICK_SORT_SLEEP, true, false);
                    Writes.write(a, e5, a3, QUICK_SORT_SLEEP, true, false);
                } else {
                    Writes.swap(a, e3, e4, QUICK_SORT_SLEEP, true, false);
                }
            }

            // Unmark pivot considerations
            Highlights.clearMark(5);
            Highlights.clearMark(6);
            Highlights.clearMark(7);
            Highlights.clearMark(8);
            Highlights.clearMark(9);

            // Pointers
            int lower = low; // The index of the last element of the left part
            int upper = end; // The index of the first element of the right part

            /*
             * Partitioning with 2 pivots in case of different elements.
             */
            if (   Reads.compareIndices(a, e1, e2, QUICK_SORT_SLEEP, true) < 0
                && Reads.compareIndices(a, e2, e3, QUICK_SORT_SLEEP, true) < 0
                && Reads.compareIndices(a, e3, e4, QUICK_SORT_SLEEP, true) < 0
                && Reads.compareIndices(a, e4, e5, QUICK_SORT_SLEEP, true) < 0
            ) {


                /*
                 * Use the first and fifth of the five sorted elements as
                 * the pivots. These values are inexpensive approximation
                 * of tertiles. Note, that pivot1 < pivot2.
                 */
                int pivot1 = a[e1];
                int pivot2 = a[e5];

                /*
                 * The first and the last elements to be sorted are moved
                 * to the locations formerly occupied by the pivots. When
                 * partitioning is completed, the pivots are swapped back
                 * into their final positions, and excluded from the next
                 * subsequent sorting.
                 */
                Writes.write(a, e1, a[lower], QUICK_SORT_SLEEP, true, false);
                Writes.write(a, e5, a[upper], QUICK_SORT_SLEEP, true, false);

                /*
                 * Skip elements, which are less or greater than the pivots.
                 */
                while (Reads.compareIndexValue(a, ++lower, pivot1, QUICK_SORT_SLEEP, true) < 0);
                while (Reads.compareIndexValue(a, --upper, pivot2, QUICK_SORT_SLEEP, true) > 0);

                Highlights.markArray(3, lower);
                Highlights.markArray(4, upper - 1);

                /*
                 * Backward 3-interval partitioning
                 *
                 *   left part                 central part          right part
                 * +------------------------------------------------------------+
                 * |  < pivot1  |   ?   |  pivot1 <= && <= pivot2  |  > pivot2  |
                 * +------------------------------------------------------------+
                 *             ^       ^                            ^
                 *             |       |                            |
                 *           lower     k                          upper
                 *
                 * Invariants:
                 *
                 *              all in (low, lower] < pivot1
                 *    pivot1 <= all in (k, upper)  <= pivot2
                 *              all in [upper, end) > pivot2
                 *
                 * Pointer k is the last index of ?-part
                 */
                for (int unused = --lower, k = ++upper; --k > lower; ) {
                    int ak = a[k];

                    if (Reads.compareIndexValue(a, k, pivot1, QUICK_SORT_SLEEP, true) < 0) { // Move a[k] to the left side
                        while (lower < k) {
                            if (Reads.compareIndexValue(a, ++lower, pivot1, QUICK_SORT_SLEEP, true) >= 0) {
                                if (Reads.compareIndexValue(a, lower, pivot2, QUICK_SORT_SLEEP, true) > 0) {
                                    Writes.write(a, k, a[--upper], QUICK_SORT_SLEEP, true, false);
                                    Writes.write(a, upper, a[lower], QUICK_SORT_SLEEP, true, false);
                                } else {
                                    Writes.write(a, k, a[lower], QUICK_SORT_SLEEP, true, false);
                                }
                                Writes.write(a, lower, ak, QUICK_SORT_SLEEP, true, false);
                                break;
                            }
                        }
                    } else if (Reads.compareIndexValue(a, k, pivot2, QUICK_SORT_SLEEP, true) > 0) { // Move a[k] to the right side
                        Writes.write(a, k, a[--upper], QUICK_SORT_SLEEP, true, false);
                        Writes.write(a, upper, ak, QUICK_SORT_SLEEP, true, false);
                    }
                }

                /*
                 * Swap the pivots into their final positions.
                 */
                // TODO: swap(low, lower)?
                Writes.write(a, low, a[lower], QUICK_SORT_SLEEP, true, false);
                Writes.write(a, lower, pivot1, QUICK_SORT_SLEEP, true, false);

                // TODO: swap(end, upper)?
                Writes.write(a, end, a[upper], QUICK_SORT_SLEEP, true, false);
                Writes.write(a, upper, pivot2, QUICK_SORT_SLEEP, true, false);

                Highlights.clearAllMarks();

                /*
                 * Sort non-left parts recursively, excluding known pivots.
                 */
                sort(a, bits | 1, lower + 1, upper);
                sort(a, bits | 1, upper + 1, high);

            } else { // Use single pivot in case of many equal elements
                /*
                 * Use the third of the five sorted elements as the pivot.
                 * This value is inexpensive approximation of the median.
                 */
                int pivot = a[e3];

                /*
                 * The first element to be sorted is moved to the
                 * location formerly occupied by the pivot. After
                 * completion of partitioning the pivot is swapped
                 * back into its final position, and excluded from
                 * the next subsequent sorting.
                 */
                a[e3] = a[lower];

                /*
                 * Traditional 3-way (Dutch National Flag) partitioning
                 *
                 *   left part                 central part    right part
                 * +------------------------------------------------------+
                 * |   < pivot   |     ?     |   == pivot   |   > pivot   |
                 * +------------------------------------------------------+
                 *              ^           ^                ^
                 *              |           |                |
                 *            lower         k              upper
                 *
                 * Invariants:
                 *
                 *   all in (low, lower] < pivot
                 *   all in (k, upper)  == pivot
                 *   all in [upper, end] > pivot
                 *
                 * Pointer k is the last index of ?-part
                 */
                for (int k = ++upper; --k > lower; ) {
                    int ak = a[k];

                    if (Reads.compareIndexValue(a, k, pivot, QUICK_SORT_SLEEP, true) != 0) {
                        a[k] = pivot;

                        if (Reads.compareValues(ak, pivot) < 0) { // Move a[k] to the left side
                            while (Reads.compareIndexValue(a, ++lower, pivot, QUICK_SORT_SLEEP, true) < 0);

                            if (Reads.compareIndexValue(a, lower, pivot, QUICK_SORT_SLEEP, true) > 0) {
                                Writes.write(a, --upper, a[lower], QUICK_SORT_SLEEP, true, false);
                            }
                            Writes.write(a, lower, ak, QUICK_SORT_SLEEP, true, false);
                        } else { // ak > pivot - Move a[k] to the right side
                            Writes.write(a, --upper, ak, QUICK_SORT_SLEEP, true, false);
                        }
                    }
                }

                /*
                 * Swap the pivot into its final position.
                 */
                // TODO: swap(low, lower)?
                Writes.write(a, low, a[lower], QUICK_SORT_SLEEP, true, false);
                Writes.write(a, lower, pivot, QUICK_SORT_SLEEP, true, false);

                Highlights.clearAllMarks();

                /*
                 * Sort the right part, excluding known pivot.
                 * All elements from the central part are
                 * equal and therefore already sorted.
                 */
                sort(a, bits | 1, upper, high);
            }
            high = lower; // Iterate along the left part
        }
    }

    /**
     * Sorts the specified range of the array using mixed insertion sort.
     *
     * Mixed insertion sort is combination of simple insertion sort,
     * pin insertion sort and pair insertion sort.
     *
     * In the context of Dual-Pivot Quicksort, the pivot element
     * from the left part plays the role of sentinel, because it
     * is less than any elements from the given part. Therefore,
     * expensive check of the left range can be skipped on each
     * iteration unless it is the leftmost call.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param end the index of the last element for simple insertion sort
     * @param high the index of the last element, exclusive, to be sorted
     */
    private void mixedInsertionSort(int[] a, int low, int end, int high) {

        Highlights.markArray(3, clamp(low));
        Highlights.markArray(4, clamp(end - 1));
        Highlights.markArray(5, clamp(high - 1));

        if (end == high) {

            /*
             * Invoke simple insertion sort on tiny array.
             */
            for (int i; ++low < end; ) {
                int ai = a[i = low];

                // TODO: Reads.compareIndices(low, --i)? Would make a more intuitive marker.
                while (Reads.compareValueIndex(a, ai, --i, INSERTION_SORT_SLEEP, true) < 0) {
                    Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                }
                Writes.write(a, i + 1, ai, INSERTION_SORT_SLEEP, true, false);
            }
        } else {

            /*
             * Start with pin insertion sort on small part.
             *
             * Pin insertion sort is extended simple insertion sort.
             * The main idea of this sort is to put elements larger
             * than an element called pin to the end of array (the
             * proper area for such elements). It avoids expensive
             * movements of these elements through the whole array.
             */
            int pin = a[end];

            for (int i, p = high; ++low < end; ) {
                int ai = a[i = low];

                // TODO: Reads.compareIndices(low, i - 1)? Would make a more intuitive marker.
                if (Reads.compareValueIndex(a, ai, i - 1, INSERTION_SORT_SLEEP, true) < 0) { // Small element

                    /*
                     * Insert small element into sorted part.
                     */
                    Writes.write(a, i, a[--i], INSERTION_SORT_SLEEP, true, false);

                    // TODO: Reads.compareIndices(low, i - 1)? Would make a more intuitive marker.
                    while (Reads.compareValueIndex(a, ai, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, i + 1, ai, INSERTION_SORT_SLEEP, true, false);

                } else if (p > i && Reads.compareValues(ai, pin) > 0) { // Large element
                    // TODO: Reads.compareIndices(low, end)?

                    /*
                     * Find element smaller than pin.
                     */
                    while (Reads.compareIndexValue(a, --p, pin, INSERTION_SORT_SLEEP, true) > 0);
                    // TODO: Reads.compareIndices(--p, end)?

                    /*
                     * Swap it with large element.
                     */
                    if (p > i) {
                        ai = a[p];
                        Writes.write(a, p, a[i], INSERTION_SORT_SLEEP, true, false);
                    }

                    /*
                     * Insert small element into sorted part.
                     */
                    while (Reads.compareValueIndex(a, ai, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        // NOTE: Can't do Reads.compareIndices because ai may have been reassigned (can't tell if
                        // it would be p or low).
                        Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, i + 1, ai, INSERTION_SORT_SLEEP, true, false);
                }
            }

            /*
             * Continue with pair insertion sort on remain part.
             */
            for (int i; low < high; ++low) {
                final int a1 = a[i = low], a2 = a[++low];

                /*
                 * Insert two elements per iteration: at first, insert the
                 * larger element and then insert the smaller element, but
                 * from the position where the larger element was inserted.
                 */
                if (Reads.compareValues(a1, a2) > 0) {
                    // TODO: Reads.compareIndices(low - 1, low)?

                    while (Reads.compareValueIndex(a, a1, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        // TODO: Reads.compareIndices(low - 1, --i)?
                        Writes.write(a, i + 2, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, ++i + 1, a1, INSERTION_SORT_SLEEP, true, false);

                    while (Reads.compareValueIndex(a, a2, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        // TODO: Reads.compareIndices(low, --i)?
                        Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, i + 1, a2, INSERTION_SORT_SLEEP, true, false);

                } else if (Reads.compareValueIndex(a, a1, i - 1, INSERTION_SORT_SLEEP, true) < 0) {
                    // TODO: Reads.compareIndices(low - 1, i - 1)?

                    while (Reads.compareValueIndex(a, a2, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        Writes.write(a, i + 2, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, ++i + 1, a2, INSERTION_SORT_SLEEP, true, false);

                    while (Reads.compareValueIndex(a, a1, --i, INSERTION_SORT_SLEEP, true) < 0) {
                        Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                    }
                    Writes.write(a, i + 1, a1, INSERTION_SORT_SLEEP, true, false);
                }
            }
        }

        Highlights.clearMark(3);
        Highlights.clearMark(4);
        Highlights.clearMark(5);
    }

    /**
     * Sorts the specified range of the array using insertion sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private void insertionSort(int[] a, int low, int high) {
        Highlights.markArray(3, low);
        Highlights.markArray(4, Math.max(low, high - 1));

        for (int i, k = low; ++k < high; ) {
            int ai = a[i = k];

            if (Reads.compareValueIndex(a, ai, i - 1, INSERTION_SORT_SLEEP, true) < 0) {
                while (--i >= low && Reads.compareValueIndex(a, ai, i, INSERTION_SORT_SLEEP, true) < 0) {
                    Writes.write(a, i + 1, a[i], INSERTION_SORT_SLEEP, true, false);
                }
                Writes.write(a, i + 1, ai, INSERTION_SORT_SLEEP, true, false);
            }
        }

        Highlights.clearMark(3);
        Highlights.clearMark(4);
    }

    /**
     * Tries to sort the specified range of the array.
     *
     * @param a the array to be sorted
     * @param low the index of the first element to be sorted
     * @param size the array size
     * @return true if finally sorted, false otherwise
     */
    private boolean tryMergeRuns(int[] a, int low, int size) {

        /*
         * The run array is constructed only if initial runs are
         * long enough to continue, run[i] then holds start index
         * of the i-th sequence of elements in non-descending order.
         */
        int[] run = null;
        int high = low + size;
        int count = 1, last = low;

        /*
         * Identify all possible runs.
         */
        for (int k = low + 1; k < high; ) {

            /*
             * Find the end index of the current run.
             */
            if (Reads.compareIndices(a, k - 1, k, RUN_MERGE_SLEEP, true) < 0) {

                // Identify ascending sequence
                while (++k < high && Reads.compareIndices(a, k - 1, k, RUN_MERGE_SLEEP, true) <= 0);

            } else if (Reads.compareIndices(a, k - 1, k, RUN_MERGE_SLEEP, true) > 0) {

                // Identify descending sequence
                while (++k < high && Reads.compareIndices(a, k - 1, k, RUN_MERGE_SLEEP, true) >= 0);

                // Reverse into ascending order
                Writes.changeReversals(1);
                for (int i = last - 1, j = k; ++i < --j && Reads.compareIndices(a, i, j, RUN_MERGE_SLEEP, true) > 0; ) {
                    Writes.swap(a, i, j, RUN_MERGE_SLEEP, true, false);
                }
            } else { // Identify constant sequence
                for (int ak = a[k]; ++k < high && Reads.compareValueIndex(a, ak, k, RUN_MERGE_SLEEP, true) == 0; );

                if (k < high) {
                    continue;
                }
            }

            /*
             * Check special cases.
             */
            if (run == null) {
                if (k == high) {

                    /*
                     * The array is monotonous sequence,
                     * and therefore already sorted.
                     */
                    return true;
                }

                if (k - low < MIN_FIRST_RUN_SIZE) {

                    /*
                     * The first run is too small
                     * to proceed with scanning.
                     */
                    return false;
                }

                // Some weird bitwise rounding and clamping magic for the size here.
                // Conditionally reserving the array through bootleg malloc because I wasn't quite
                // sure if I wanted the indices showing up in the visualization.
                run = includeRunIndicesInVisuals
                    ? Writes.createExternalArray(((size >> 10) | 0x7F) & 0x3FF)
                    : new int[((size >> 10) | 0x7F) & 0x3FF];
                Writes.write(run, 0, low, RUN_MERGE_SLEEP, true, true);

            } else if (Reads.compareIndices(a, last - 1, last, RUN_MERGE_SLEEP, true) > 0) {

                if (count > (k - low) >> MIN_FIRST_RUNS_FACTOR) {

                    /*
                     * The first runs are not long
                     * enough to continue scanning.
                     */
                    if (includeRunIndicesInVisuals) Writes.deleteExternalArray(run);
                    return false;
                }

                if (++count == MAX_RUN_CAPACITY) {

                    /*
                     * Array is not highly structured.
                     */
                    if (includeRunIndicesInVisuals) Writes.deleteExternalArray(run);
                    return false;
                }

                if (count == run.length) {

                    /*
                     * Increase capacity of index array.
                     */
                    if (includeRunIndicesInVisuals) {
                        int[] copy = Writes.copyOfArray(run, count << 1);
                        Writes.deleteExternalArray(run);
                        run = copy;
                    } else {
                        run = Arrays.copyOf(run, count << 1);
                    }
                }
            }
            Writes.write(run, count, (last = k), RUN_MERGE_SLEEP, true, true);
        }

        /*
         * Merge runs of highly structured array.
         */
        if (count > 1) {
            int[] b = Writes.createExternalArray(size);

            mergeRuns(a, b, low, 1, run, 0, count);
            Writes.deleteExternalArray(b);
        }

        if (includeRunIndicesInVisuals) Optional.ofNullable(run).ifPresent(Writes::deleteExternalArray);
        return true;
    }

    /**
     * Merges the specified runs.
     *
     * @param a the source array
     * @param b the temporary buffer used in merging
     * @param offset the start index in the source, inclusive
     * @param aim specifies merging: to source ( > 0), buffer ( < 0) or any ( == 0)
     * @param run the start indexes of the runs, inclusive
     * @param lo the start index of the first run, inclusive
     * @param hi the start index of the last run, inclusive
     * @return the destination where runs are merged
     */
    private int[] mergeRuns(int[] a, int[] b, int offset,
            int aim, int[] run, int lo, int hi) {

        if (hi - lo == 1) {
            if (aim >= 0) {
                return a;
            }
            for (int i = run[hi], j = i - offset, low = run[lo]; Reads.compareValues(i, low) > 0;
                 Writes.write(b, --j, a[--i], RUN_MERGE_SLEEP, true, true)
            );
            return b;
        }

        /*
         * Split into approximately equal parts.
         */
        int mi = lo, rmi = (run[lo] + run[hi]) >>> 1;
        while (Reads.compareIndexValue(run, ++mi + 1, rmi, RUN_MERGE_SLEEP, false) <= 0);

        /*
         * Merge the left and right parts.
         */
        int[] a1, a2;

        a1 = mergeRuns(a, b, offset, -aim, run, lo, mi);
        a2 = mergeRuns(a, b, offset,    0, run, mi, hi);

        int[] dst = a1 == a ? b : a;

        int k   = a1 == a ? run[lo] - offset : run[lo];
        int lo1 = a1 == b ? run[lo] - offset : run[lo];
        int hi1 = a1 == b ? run[mi] - offset : run[mi];
        int lo2 = a2 == b ? run[mi] - offset : run[mi];
        int hi2 = a2 == b ? run[hi] - offset : run[hi];

        mergeParts(dst, k, a1, lo1, hi1, a2, lo2, hi2);
        return dst;
    }

    /**
     * Merges the sorted parts.
     *
     * @param dst the destination where parts are merged
     * @param k the start index of the destination, inclusive
     * @param a1 the first part
     * @param lo1 the start index of the first part, inclusive
     * @param hi1 the end index of the first part, exclusive
     * @param a2 the second part
     * @param lo2 the start index of the second part, inclusive
     * @param hi2 the end index of the second part, exclusive
     */
    private void mergeParts(int[] dst, int k,
                                   int[] a1, int lo1, int hi1, int[] a2, int lo2, int hi2) {

        /*
         * Merge small parts sequentially.
         * TODO: Not sure if the highlighting will make sense, here, since it always marks them in the main array.
         *    Also need to do some sort of dst == original for the auxwrite flag.
         */
        while (lo1 < hi1 && lo2 < hi2) {
            Writes.write(dst, k++, Reads.compareValues(a1[lo1], a2[lo2]) < 0 ? a1[lo1++] : a2[lo2++], RUN_MERGE_SLEEP, true, true);
        }
        if (dst != a1 || k < lo1) {
            while (lo1 < hi1) {
                Writes.write(dst, k++, a1[lo1++], RUN_MERGE_SLEEP, true, true);
            }
        }
        if (dst != a2 || k < lo2) {
            while (lo2 < hi2) {
                Writes.write(dst, k++, a2[lo2++], RUN_MERGE_SLEEP, true, true);
            }
        }
    }

    /**
     * Sorts the specified range of the array using heap sort.
     *
     * @param a the array to be sorted
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void heapSort(int[] a, int low, int high) {

        if (true) throw new UnsupportedOperationException("Degeneracy detected. Heapsort not implemented.");

        for (int k = (low + high) >>> 1; k > low; ) {
            pushDown(a, --k, a[k], low, high);
        }
        while (--high > low) {
            int max = a[low];
            pushDown(a, low, a[high], low, high);
            a[high] = max;
        }
    }

    /**
     * Pushes specified element down during heap sort.
     *
     * @param a the given array
     * @param p the start index
     * @param value the given element
     * @param low the index of the first element, inclusive, to be sorted
     * @param high the index of the last element, exclusive, to be sorted
     */
    private static void pushDown(int[] a, int p, int value, int low, int high) {
        for (int k ;; a[p] = a[p = k]) {
            k = (p << 1) - low + 2; // Index of the right child

            if (k > high) {
                break;
            }
            if (k == high || a[k] < a[k - 1]) {
                --k;
            }
            if (a[k] <= value) {
                break;
            }
        }
        a[p] = value;
    }
}

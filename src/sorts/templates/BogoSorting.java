package sorts.templates;

import java.util.concurrent.ThreadLocalRandom;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021 EmeraldBlock

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 * Abstract class containing methods commonly used in bogosorts.
 * <p>
 * Bogosorts are a subset of impractical sorts
 * that rely on guess-and-check to sort the array.
 * This is typically done through randomness.
 */
public abstract class BogoSorting extends Sort {
    protected BogoSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    /**
     * Returns a random {@code int} value in the range {@code [start, end)}.
     *
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @return a random {@code int} value within the range
     */
    protected static int randInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    /**
     * Returns a random {@code boolean} value.
     *
     * @return a random {@code boolean} value
     */
    protected static boolean randBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * Randomly shuffles the range {@code [start, end)} of {@code array}.
     * <p>
     * This implementation uses the Fisher–Yates shuffle.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @param aux whether the array is auxililary
     */
    protected void bogoSwap(int[] array, int start, int end, boolean aux){
        for(int i = start; i < end; ++i)
            Writes.swap(array, i, BogoSorting.randInt(i, end), 0.0, true, aux);
    }

    /**
     * Sets a random combination of {@code size} elements
     * in the range {@code [start, end)} of {@code array} to {@code 1}, and the rest to {@code 0}.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @param size the number of elements to set to 1
     * @param aux whether the array is auxililary
     */
    protected void bogoCombo(int[] array, int start, int end, int size, boolean aux) {
        for (int i = start; i < end; ++i)
            Writes.write(array, i, 0, 0.0, true, aux);

        for (int i = end-size; i < end; ++i) {
            int j = BogoSorting.randInt(start, i+1);
            Highlights.markArray(1, j);
            Writes.write(array, Reads.compareValues(array[j], 0) == 0 ? j : i, 1, 0.0, true, aux);
        }
    }

    /**
     * Checks if the range {@code [start, end)} of {@code array} is sorted.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @return whether the range is sorted
     */
    protected boolean isRangeSorted(int[] array, int start, int end) {
        for (int i = start; i < end - 1; ++i) {
            if (Reads.compareIndices(array, i, i + 1, 0.0, true) > 0)
                return false;
        }
        return true;
    }

    /**
     * Checks if {@code array} is sorted.
     * This can also be used to check if a prefix of length {@code length} of {@code array} is sorted.
     *
     * @param array the array
     * @param length the length of the array or prefix
     * @return whether the array or prefix is sorted
     */
    protected boolean isArraySorted(int[] array, int length) {
        return isRangeSorted(array, 0, length);
    }

    /**
     * Checks if the range {@code [start, end)} of {@code array} is partitioned around index {@code pivot}.
     * <p>
     * <i>Partitioned</i> means that every element before {@code pivot} is no greater than the value of {@code pivot},
     * and every element after is no less.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param pivot the index of the pivot
     * @param end the end of the range, exclusive
     * @return whether the range is partitioned
     */
    protected boolean isRangePartitioned(int[] array, int start, int pivot, int end) {
        for (int i = start; i < pivot; i++) {
            if (Reads.compareIndices(array, i, pivot, 0.0, true) > 0)
                return false;
        }
        for (int i = pivot + 1; i < end; i++) {
            if (Reads.compareIndices(array, pivot, i, 0.0, true) > 0)
                return false;
        }
        return true;
    }

    /**
     * Checks if the first element in the range {@code [start, end)} of {@code array} is a minimum.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @return whether the first element is a minimum
     */
    protected boolean isMinSorted(int[] array, int start, int end) {
        return isRangePartitioned(array, start, start, end);
    }

    /**
     * Checks if the last element in the range {@code [start, end)} of {@code array} is a maximum.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end the end of the range, exclusive
     * @return whether the last element is a maximum
     */
    protected boolean isMaxSorted(int[] array, int start, int end) {
        return isRangePartitioned(array, start, end-1, end);
    }

    /**
     * Checks if the elements in the range {@code [start, end)} of {@code array} are split by {@code mid},
     * where the {@code mid} itself belongs to the ending side of the array.
     * <p>
     * <i>Split</i> means that all elements in range {@code [start, mid)} are no greater than those in range {@code [mid, end)}.
     *
     * @param array the array
     * @param start the start of the range, inclusive
     * @param mid the index where range is split
     * @param end the end of the range, exclusive
     * @return whether the range is split
     */
    protected boolean isRangeSplit(int[] array, int start, int mid, int end) {
        Highlights.markArray(1, start);
        int lowMax = array[start];
        for (int i = start+1; i < mid; ++i) {
            Highlights.markArray(1, i);
            if (Reads.compareValues(lowMax, array[i]) < 0)
                lowMax = array[i];
        }

        for (int i = mid; i < end; ++i) {
            Highlights.markArray(1, i);
            if (Reads.compareValues(lowMax, array[i]) > 0)
                return false;
        }
        return true;
    }
}

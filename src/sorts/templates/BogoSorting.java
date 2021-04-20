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

public abstract class BogoSorting extends Sort {
    protected BogoSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected static int randInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    protected static boolean randBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    protected void bogoSwap(int[] array, int start, int end, boolean aux){
        for(int i = start; i < end; ++i)
            Writes.swap(array, i, BogoSorting.randInt(i, end), 0.0, true, aux);
    }

    protected void bogoCombo(int[] array, int start, int end, int size, boolean aux) {
        for (int i = start; i < end; ++i)
            Writes.write(array, i, 1, 0.0, true, aux);

        for (int i = end-size; i < end; ++i) {
            int j = BogoSorting.randInt(start, i+1);
            Highlights.markArray(1, j);
            Writes.write(array, Reads.compareValues(array[j], 1) == 0 ? j : i, 0, 0.0, true, aux);
        }
    }

    protected boolean isRangeSorted(int[] array, int start, int end) {
        for (int i = start; i < end - 1; ++i) {
            if (Reads.compareIndices(array, i, i + 1, 0.0, true) > 0)
                return false;
        }
        return true;
    }

    protected boolean bogoIsSorted(int[] array, int length) {
        return isRangeSorted(array, 0, length);
    }

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

    protected boolean isMinSorted(int[] array, int start, int end) {
        return isRangePartitioned(array, start, start, end);
    }

    protected boolean isMaxSorted(int[] array, int start, int end) {
        return isRangePartitioned(array, start, end-1, end);
    }

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

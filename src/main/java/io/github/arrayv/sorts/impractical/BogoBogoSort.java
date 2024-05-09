package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/*
 *
MIT License

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
 * Implements https://www.dangermouse.net/esoteric/bogobogosort.html.
 * <p>
 * Bogobogosort is like Bogosort, but in order to check whether the array is
 * sorted, it performs the following procedure:
 * <ul>
 * <li>It makes a copy, then shuffles it and sorts all but the last element
 * using Bogobogosort.
 * <li>If the last element of the sorted section is no greater than the last
 * element of the copy,
 * then the copy is sorted.
 * <li>Otherwise, the shuffling-sorting process is repeated.
 * <li>The original array is then compared to the copy to determine whether the
 * array is sorted.
 * </ul>
 * Like in Bogosort, if the array is not sorted, it shuffles it and repeats the
 * entire process.
 */
@SortMeta(name = "BogoBogo", slowSort = true, bogoSort = true, unreasonableLimit = 5)
public final class BogoBogoSort extends BogoSorting {
    public BogoBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int[][] tmp; // fix seizure aux using 2d array

    private boolean bogoBogoIsSorted(int[] array, int length) {
        if (length == 1)
            return true;

        int idx = length - 2;
        Writes.arraycopy(array, 0, tmp[idx], 0, length, this.delay, true, true);

        bogoBogo(tmp[idx], length - 1, true);
        while (Reads.compareValues(tmp[idx][length - 2], tmp[idx][length - 1]) > 0) {
            this.bogoSwap(tmp[idx], 0, length, true);
            bogoBogo(tmp[idx], length - 1, true);
        }

        for (int i = 0; i < length; ++i) {
            Highlights.markArray(1, i);
            Delays.sleep(this.delay);
            if (Reads.compareValues(array[i], tmp[idx][i]) != 0) {
                return false;
            }
        }
        return true;
    }

    private void bogoBogo(int[] array, int length, boolean aux) {
        while (!bogoBogoIsSorted(array, length))
            this.bogoSwap(array, 0, length, aux);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        tmp = new int[length - 1][];

        for (int i = length; i > 1; i--)
            tmp[i - 2] = Writes.createExternalArray(i);

        bogoBogo(array, length, false);

        for (int i = length; i > 1; i--)
            Writes.deleteExternalArray(tmp[i - 2]);
    }
}

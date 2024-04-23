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
 * Smart Bogobogosort is like bogosort, but it makes the observation that
 * the sorted copy produced to check if the array is sorted, is sorted.
 * This is then simplified so that no copy of the array is needed.
 * <ul>
 * <li>All but the last element of the array are sorted using Bogobogosort.
 * <li>If the last element of the sorted section is no greater than the last
 * element of the array,
 * then the copy is sorted. Otherwise, the array is shuffled and the process is
 * repeated.
 * </ul>
 */
@SortMeta(name = "Smart BogoBogo", slowSort = true, bogoSort = true, unreasonableLimit = 11)
public final class SmartBogoBogoSort extends BogoSorting {
    public SmartBogoBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void smartBogoBogo(int[] array, int length) {
        if (length == 1)
            return;

        smartBogoBogo(array, length - 1);
        while (Reads.compareIndices(array, length - 2, length - 1, this.delay, true) > 0) {
            this.bogoSwap(array, 0, length, false);
            smartBogoBogo(array, length - 1);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        smartBogoBogo(array, length);
    }
}

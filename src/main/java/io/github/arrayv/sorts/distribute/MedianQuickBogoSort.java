package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
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
 * Median Quick Bogosort repeatedly shuffles the array until the left and right halves are split.
 * It then recursively sorts each half.
 */
public final class MedianQuickBogoSort extends BogoSorting {
    public MedianQuickBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Median Quick Bogo");
        this.setRunAllSortsName("Median Quick Bogo Sort");
        this.setRunSortName("Median Quick Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(23);
        this.setBogoSort(true);
    }

    private void medianQuickBogo(int[] array, int start, int end) {
        if (start >= end-1)
            return;

        int mid = (start+end)/2;
        while (!isRangeSplit(array, start, mid, end))
            this.bogoSwap(array, start, end, false);

        medianQuickBogo(array, start, mid);
        medianQuickBogo(array, mid, end);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        medianQuickBogo(array, 0, sortLength);
    }
}

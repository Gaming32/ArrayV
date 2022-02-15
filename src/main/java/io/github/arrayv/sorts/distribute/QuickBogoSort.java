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
 * Quick Bogosort is like Quicksort, but after selecting a pivot,
 * it randomly shuffles the array until the pivot partitions the array.
 * The pivot is tracked as the array is shuffled.
 */
public final class QuickBogoSort extends BogoSorting {
    public QuickBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Quick Bogo");
        this.setRunAllSortsName("Quick Bogo Sort");
        this.setRunSortName("Quick Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(22);
        this.setBogoSort(true);
    }

    private int quickBogoSwap(int[] array, int start, int pivot, int end){
        for(int i = start; i < end; i++) {
            int j = BogoSorting.randInt(i, end);
            if (pivot == i)
                pivot = j;
            else if (pivot == j)
                pivot = i;
            Writes.swap(array, i, j, this.delay, true, false);
        }
        return pivot;
    }

    private void quickBogo(int[] array, int start, int end) {
        if (start >= end-1)
            return;

        int pivot = start;
        // worst-case pivot (linear distribution)
        // for (; pivot < end; ++pivot)
        //     if (array[pivot] == (start+end)/2) break;
        while (!isRangePartitioned(array, start, pivot, end))
            pivot = quickBogoSwap(array, start, pivot, end);

        quickBogo(array, start, pivot);
        quickBogo(array, pivot+1, end);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickBogo(array, 0, sortLength);
    }
}

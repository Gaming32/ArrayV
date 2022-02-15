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

/*
 * The original Selection Bogosort algorithm was created and implemented by fluffyyboii.
 * However, the original implementation seems to never have been added,
 * and this version makes it more concise with some helper methods.
 */

/**
 * Selection Bogosort is like Selection Sort,
 * but it randomly swaps an element out of the remaining unsorted elements
 * to the front of them until it is the smallest.
 * <p>
 * Selection Bogosort can also be viewed as an optimized variation of Less Bogosort.
 */
public final class SelectionBogoSort extends BogoSorting {
    public SelectionBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Selection Bogo");
        this.setRunAllSortsName("Selection Bogo Sort");
        this.setRunSortName("Selection Bogosort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length; ++i) {
            while (!this.isMinSorted(array, i, length))
                Writes.swap(array, i, BogoSorting.randInt(i, length), this.delay, true, false);
            Highlights.markArray(3, i);
        }
    }
}

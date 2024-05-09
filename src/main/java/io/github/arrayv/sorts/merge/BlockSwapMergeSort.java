package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2019 Piotr Grochowski
Copyright (c) 2020 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

//refactored version of original implementation by @Piotr Grochowski (in place merge 2)
@SortMeta(name = "Block-Swap Merge")
public final class BlockSwapMergeSort extends Sort {
    public BlockSwapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void multiSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 1, true, false);
    }

    private int binarySearchMid(int[] array, int start, int mid, int end) {
        int a = 0, b = Math.min(mid - start, end - mid), m = a + (b - a) / 2;

        while (b > a) {
            if (Reads.compareValues(array[mid - m - 1], array[mid + m]) == 1)
                a = m + 1;
            else
                b = m;

            m = a + (b - a) / 2;
        }

        return m;
    }

    public void multiSwapMerge(int[] array, int start, int mid, int end) {
        int m = this.binarySearchMid(array, start, mid, end);

        while (m > 0) {
            this.multiSwap(array, mid - m, mid, m);
            this.multiSwapMerge(array, mid, mid + m, end);

            end = mid;
            mid -= m;

            m = this.binarySearchMid(array, start, mid, end);
        }
    }

    public void multiSwapMergeSort(int[] array, int a, int b) {
        int len = b - a, i;

        for (int j = 1; j < len; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                this.multiSwapMerge(array, i, i + j, i + 2 * j);

            if (i + j < b)
                this.multiSwapMerge(array, i, i + j, b);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.multiSwapMergeSort(array, 0, length);
    }
}

package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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
@SortMeta(name = "Improved In-Place Merge")
public final class ImprovedInPlaceMergeSort extends Sort {
    public ImprovedInPlaceMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void push(int[] array, int p, int a, int b, double sleep) {
        if (a == b)
            return;

        int temp = array[p];
        Writes.write(array, p, array[a], sleep, true, false);

        for (int i = a + 1; i < b; i++)
            Writes.write(array, i - 1, array[i], sleep, true, false);

        Writes.write(array, b - 1, temp, sleep, true, false);
    }

    private void merge(int[] array, int a, int m, int b, double sleep) {
        int i = a, j = m;

        Highlights.clearMark(1);
        while (i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            Delays.sleep(1);

            if (Reads.compareValues(array[i], array[j]) == 1)
                j++;
            else
                this.push(array, i++, m, j, sleep);
        }

        Highlights.clearAllMarks();
        while (i < m)
            this.push(array, i++, m, b, sleep);
    }

    private void mergeSort(int[] array, int a, int b, double sleep) {
        int m = a + (b - a) / 2;

        if (b - a > 2) {
            if (b - a > 3)
                this.mergeSort(array, a, m, 2 * sleep);
            this.mergeSort(array, m, b, 2 * sleep);
        }

        this.merge(array, a, m, b, sleep);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.mergeSort(array, 0, currentLength, Math.max(1.0 / currentLength, 0.001));
    }
}

package io.github.arrayv.sorts.select;

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
@SortMeta(name = "Double Selection")
public final class DoubleSelectionSort extends Sort {
    public DoubleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int left = 0;
        int right = length - 1;
        int smallest = 0;
        int biggest = 0;

        while (left <= right) {
            for (int i = left; i <= right; i++) {
                Highlights.markArray(3, i);

                if (Reads.compareIndices(array, i, biggest, 0.01, true) == 1)
                    biggest = i;

                if (Reads.compareIndices(array, i, smallest, 0.01, true) == -1) {
                    smallest = i;
                    Highlights.markArray(2, smallest);
                }

                Delays.sleep(0.01);
            }
            if (biggest == left)
                biggest = smallest;

            Writes.swap(array, left, smallest, 0.02, true, false);
            Writes.swap(array, right, biggest, 0.02, true, false);

            left++;
            right--;

            smallest = left;
            biggest = right;
        }
    }
}

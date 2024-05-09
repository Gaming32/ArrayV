package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;

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

public abstract class MergeSorting extends Sort {
    private BinaryInsertionSort binaryInserter;

    protected MergeSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void merge(int[] array, int[] tmp, int start, int mid, int end, boolean binary) {
        if (start == mid)
            return;

        if (end - start < 32 && binary) {
            return;
        } else if (end - start < 64 && binary) {
            binaryInserter.customBinaryInsert(array, start, end, 0.333);
        } else {
            merge(array, tmp, start, (mid + start) / 2, mid, binary);
            merge(array, tmp, mid, (mid + end) / 2, end, binary);

            int low = start;
            int high = mid;

            for (int nxt = 0; nxt < end - start; nxt++) {
                if (low >= mid && high >= end)
                    break;

                Highlights.markArray(1, low);
                Highlights.markArray(2, high);

                if (low < mid && high >= end) {
                    Highlights.clearMark(2);
                    Writes.write(tmp, nxt, array[low++], 1, false, true);
                } else if (low >= mid && high < end) {
                    Highlights.clearMark(1);
                    Writes.write(tmp, nxt, array[high++], 1, false, true);
                } else if (Reads.compareValues(array[low], array[high]) <= 0) {
                    Writes.write(tmp, nxt, array[low++], 1, false, true);
                } else {
                    Writes.write(tmp, nxt, array[high++], 1, false, true);
                }
            }
            Highlights.clearMark(2);

            for (int i = 0; i < end - start; i++) {
                Writes.write(array, start + i, tmp[i], 1, true, false);
            }
        }
    }

    protected void mergeSort(int[] array, int length, boolean binary) {
        binaryInserter = new BinaryInsertionSort(this.arrayVisualizer);

        if (length < 32 && binary) {
            binaryInserter.customBinaryInsert(array, 0, length, 0.333);
            return;
        }

        int[] tmp = Writes.createExternalArray(length);

        int start = 0;
        int end = length;
        int mid = start + ((end - start) / 2);

        merge(array, tmp, start, mid, end, binary);

        Writes.deleteExternalArray(tmp);
    }
}

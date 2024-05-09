package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 Gaming32 and Morewenn

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
@SortMeta(name = "Double Insertion")
public final class DoubleInsertionSort extends Sort {
    public DoubleInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void insertionSort(int[] array, int start, int end, double sleep, boolean auxwrite) {
        int left = start + (end - start) / 2 - 1, right = left + 1;
        if (Reads.compareIndices(array, left, right, sleep, true) > 0) {
            Writes.swap(array, left, right, sleep, true, auxwrite);
        }
        left--;
        right++;

        int leftItem, rightItem;
        while (left >= start && right < end) {
            if (Reads.compareIndices(array, left, right, sleep, true) > 0) {
                leftItem = array[right];
                rightItem = array[left];

                int pos = left + 1;
                while (pos <= right && Reads.compareValues(array[pos], leftItem) <= 0) {
                    Writes.write(array, pos - 1, array[pos], sleep, true, auxwrite);
                    pos++;
                }
                Writes.write(array, pos - 1, leftItem, sleep, true, auxwrite);

                pos = right - 1;
                while (pos >= left && Reads.compareValues(array[pos], rightItem) >= 0) {
                    Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                    pos--;
                }
                Writes.write(array, pos + 1, rightItem, sleep, true, auxwrite);
            } else {
                leftItem = array[left];
                rightItem = array[right];

                int pos = left + 1;
                while (Reads.compareValues(array[pos], leftItem) < 0) {
                    Writes.write(array, pos - 1, array[pos], sleep, true, auxwrite);
                    pos++;
                }
                Writes.write(array, pos - 1, leftItem, sleep, true, auxwrite);

                pos = right - 1;
                while (Reads.compareValues(array[pos], rightItem) > 0) {
                    Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                    pos--;
                }
                Writes.write(array, pos + 1, rightItem, sleep, true, auxwrite);
            }

            left--;
            right++;
        }

        if (right < end) {
            int pos = right - 1;
            int current = array[right];
            while (Reads.compareValues(array[pos], current) > 0) {
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
            }
            Writes.write(array, pos + 1, current, sleep, true, auxwrite);
        }
    }

    public void customInsertSort(int[] array, int start, int end, double sleep, boolean auxwrite) {
        this.insertionSort(array, start, end, sleep, auxwrite);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.insertionSort(array, 0, currentLength, 0.015, false);
    }
}

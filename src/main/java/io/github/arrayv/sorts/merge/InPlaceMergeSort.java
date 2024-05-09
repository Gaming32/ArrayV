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
@SortMeta(name = "In-Place Merge")
public final class InPlaceMergeSort extends Sort {
    public InPlaceMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void push(int[] array, int low, int high) {
        for (int i = low; i < high; i++) {
            if (Reads.compareValues(array[i], array[i + 1]) == 1) {
                Writes.swap(array, i, i + 1, 0.035, true, false);
            }
        }
    }

    private void merge(int[] array, int min, int max, int mid) {
        int i = min;
        while (i <= mid) {
            if (Reads.compareValues(array[i], array[mid + 1]) == 1) {
                Writes.swap(array, i, mid + 1, 0.035, true, false);
                push(array, mid + 1, max);
            }
            i++;
            Delays.sleep(0.035);
        }
    }

    private void mergeSort(int[] array, int min, int max) {
        if (max - min == 0) { // only one element.
            Delays.sleep(1); // no swap
        } else if (max - min == 1) { // only two elements and swaps them
            if (Reads.compareValues(array[min], array[max]) == 1) {
                Writes.swap(array, min, max, 0.035, true, false);
            }
        } else {
            int mid = ((int) Math.floor((min + max) / 2)); // The midpoint

            mergeSort(array, min, mid); // sort the left side
            mergeSort(array, mid + 1, max); // sort the right side
            merge(array, min, max, mid); // combines them
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.mergeSort(array, 0, currentLength - 1);
    }
}

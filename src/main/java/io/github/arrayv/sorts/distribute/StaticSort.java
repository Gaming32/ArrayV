package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.select.MaxHeapSort;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 thatsOven

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
@SortMeta(name = "Static")
public final class StaticSort extends Sort {
    MaxHeapSort heapSorter;
    InsertionSort insertSorter;

    public StaticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public int[] findMinMax(int[] array, int a, int b) {
        int min = array[a],
                max = min;

        for (int i = a + 1; i < b; i++) {
            if (Reads.compareIndexValue(array, i, min, 0.5, true) < 0)
                min = array[i];
            else if (Reads.compareIndexValue(array, i, max, 0.5, true) > 0)
                max = array[i];
        }

        return new int[] { min, max };
    }

    public void staticSort(int[] array, int a, int b) {
        int[] minMax = this.findMinMax(array, a, b);
        int auxLen = b - a;

        int[] count = Writes.createExternalArray(auxLen + 1),
                offset = Writes.createExternalArray(auxLen + 1);

        float CONST = (float) auxLen / (minMax[1] - minMax[0] + 1);

        int idx;
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            idx = (int) ((array[i] - minMax[0]) * CONST);
            Writes.write(count, idx, count[idx] + 1, 1, false, true);
        }

        Writes.write(offset, 0, a, 0, false, true);

        for (int i = 1; i < auxLen; i++) {
            Writes.write(offset, i, count[i - 1] + offset[i - 1], 0, false, true);
        }

        for (int v = 0; v < auxLen; v++) {
            while (count[v] > 0) {
                int origin = offset[v];
                int from = origin;
                int num = array[from];

                Writes.write(array, from, -1, 0.5, true, false);

                do {
                    idx = (int) ((num - minMax[0]) * CONST);
                    int to = offset[idx];

                    Writes.write(offset, idx, offset[idx] + 1, 1, false, true);
                    Writes.write(count, idx, count[idx] - 1, 1, false, true);

                    int temp = array[to];
                    Writes.write(array, to, num, 1, true, false);

                    num = temp;
                    from = to;
                } while (from != origin);
            }
        }

        for (int i = 0; i < auxLen; i++) {
            int s = (i > 1) ? offset[i - 1] : a,
                    e = offset[i];

            if (e - s <= 1)
                continue;

            if (e - s > 16)
                heapSorter.customHeapSort(array, s, e, 1);
            else
                insertSorter.customInsertSort(array, s, e, 1, false);
        }

        Writes.deleteExternalArray(count);
        Writes.deleteExternalArray(offset);
    }

    @Override
    public void runSort(int[] mainArray, int size, int bucketCount) throws Exception {
        heapSorter = new MaxHeapSort(this.arrayVisualizer);
        insertSorter = new InsertionSort(this.arrayVisualizer);

        this.staticSort(mainArray, 0, size);
    }
}

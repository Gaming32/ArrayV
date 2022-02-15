package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 Gaming32

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

public final class AsynchronousSort extends Sort {
    public AsynchronousSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Asynchronous");
        this.setRunAllSortsName("Asynchronous Sort");
        this.setRunSortName("Asynchronous Sort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] ext = Writes.createExternalArray(length);

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            Writes.write(ext, i, array[i], 0.5, true, true);
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }
        max++;

        int cur = min, i = 0;
        while (i < length) {
            for (int j = 0; j < length; j++) {
                Highlights.markArray(2, j);
                if (Reads.compareValues(ext[j], cur) <= 0) {
                    Writes.write(array, i, ext[j], 0.01, true, false);
                    Writes.write(ext, j, max, 0, false, true);
                    i++;
                }
                Delays.sleep(0.01);
            }
            cur++;
        }

        Writes.deleteExternalArray(ext);

        // Necessary for floats
        InsertionSort insertSort = new InsertionSort(arrayVisualizer);
        Highlights.clearMark(2);
        insertSort.customInsertSort(array, 0, cur, 0.2, false);
    }
}

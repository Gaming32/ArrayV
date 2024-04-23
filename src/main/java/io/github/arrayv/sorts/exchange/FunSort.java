package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2020 fungamer2
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
@SortMeta(name = "Fun")
public final class FunSort extends Sort {

    public FunSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    /*
     * Fun Sort - or the chaos of unordered binary search
     * https://www.sciencedirect.com/science/article/pii/S0166218X04001131
     * Best Case: O(n log n)
     * Average/Worst Case: O(n^2 log n)
     */

    public int binarySearch(int[] array, int start, int end, int value) {
        while (start < end) {
            int mid = (start + end) >>> 1;
            Highlights.markArray(1, start);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, end);
            if (Reads.compareValues(array[mid], value) < 0) {
                start = mid + 1;
            } else {
                end = mid;
            }
            Delays.sleep(1);
        }
        Highlights.clearMark(1);
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return start;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length; i++) {
            boolean done = false;
            do {
                done = true;
                int pos = binarySearch(array, 0, length - 1, array[i]);
                if (Reads.compareIndices(array, pos, i, 0, false) != 0) {
                    if (i < pos - 1) {
                        Writes.swap(array, i, pos - 1, 2, true, false);
                    } else if (i > pos) {
                        Writes.swap(array, i, pos, 2, true, false);
                    }
                    done = false;
                }
            } while (!done);
        }
    }
}

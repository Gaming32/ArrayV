package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

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
@SortMeta(name = "Iterative Top-Down Merge")
public class IterativeTopDownMergeSort extends Sort {

    public IterativeTopDownMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private static int ceilPowerOfTwo(int x) {
        --x;
        for (int i = 16; i > 0; i >>= 1)
            x |= x >> i;
        return ++x;
    }

    protected void merge(int[] array, int[] tmp, int start, int mid, int end) {
        int low = start;
        int high = mid;

        Highlights.markArray(1, low);
        Highlights.markArray(2, high);
        int nxt = start;
        for (; low < mid && high < end; ++nxt) {
            if (Reads.compareValues(array[low], array[high]) == 1) {
                Writes.write(tmp, nxt, array[high++], 1, false, true);
                Highlights.markArray(2, high);
            } else {
                Writes.write(tmp, nxt, array[low++], 1, false, true);
                Highlights.markArray(1, low);
            }
        }

        if (low >= mid) {
            while (high < end) {
                Writes.write(tmp, nxt++, array[high++], 1, false, true);
                Highlights.markArray(2, high);
            }
        } else {
            while (low < mid) {
                Writes.write(tmp, nxt++, array[low++], 1, false, true);
                Highlights.markArray(1, low);
            }
        }
        Highlights.clearMark(1);
        Highlights.clearMark(2);

        for (int i = start; i < end; i++) {
            Writes.write(array, i, tmp[i], 1, true, false);
        }
    }

    protected void mergeSort(int[] array, int[] tmp, int length) {
        if (length < 1 << 15)
            for (int subarrayCount = ceilPowerOfTwo(length); subarrayCount > 1; subarrayCount >>= 1)
                for (int i = 0; i < subarrayCount; i += 2)
                    merge(array, tmp, length * i / subarrayCount, length * (i + 1) / subarrayCount,
                            length * (i + 2) / subarrayCount);
        else
            runSortLarge(array, tmp, length);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] tmp = Writes.createExternalArray(length);
        mergeSort(array, tmp, length);
        Writes.deleteExternalArray(tmp);
    }

    // implements "rational numbers" instead of multiplication/division but pretty
    // hacky
    private void runSortLarge(int[] array, int[] tmp, int length) {
        for (int subarrayCount = ceilPowerOfTwo(length), wholeI = length / subarrayCount,
                fracI = length % subarrayCount; subarrayCount > 1;) {
            for (int whole = 0, frac = 0; whole < length;) {
                int start = whole;
                whole += wholeI;
                frac += fracI;
                if (frac >= subarrayCount) {
                    ++whole;
                    frac -= subarrayCount;
                }
                int mid = whole;
                whole += wholeI;
                frac += fracI;
                if (frac >= subarrayCount) {
                    ++whole;
                    frac -= subarrayCount;
                }
                merge(array, tmp, start, mid, whole);
            }
            subarrayCount >>= 1;
            wholeI <<= 1;
            if (fracI >= subarrayCount) {
                ++wholeI;
                fracI -= subarrayCount;
            }
        }
    }
}

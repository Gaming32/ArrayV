package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
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

public class WeavedMergeSort extends Sort {

    public WeavedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved Merge");
        this.setRunAllSortsName("Weaved Merge Sort");
        this.setRunSortName("Weaved Mergesort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void merge(int[] array, int[] tmp, int length, int residue, int modulus) {
        if (residue+modulus >= length)
            return;

        int low = residue;
        int high = residue+modulus;
        int dmodulus = modulus<<1;

        merge(array, tmp, length, low, dmodulus);
        merge(array, tmp, length, high, dmodulus);

        Highlights.markArray(1, low);
        Highlights.markArray(2, high);
        int nxt = residue;
        for (; low < length && high < length; nxt+=modulus) {
            int cmp = Reads.compareValues(array[low], array[high]);
            if (cmp == 1 || cmp == 0 && low > high) {
                Writes.write(tmp, nxt, array[high], 1, false, true);
                high += dmodulus;
                Highlights.markArray(2, high);
            } else {
                Writes.write(tmp, nxt, array[low], 1, false, true);
                low += dmodulus;
                Highlights.markArray(1, low);
            }
        }

        if (low >= length) {
            while (high < length) {
                Writes.write(tmp, nxt, array[high], 1, false, true);
                nxt += modulus;
                high += dmodulus;
                Highlights.markArray(2, high);
            }
        } else {
            while (low < length) {
                Writes.write(tmp, nxt, array[low], 1, false, true);
                nxt += modulus;
                low += dmodulus;
                Highlights.markArray(1, low);
            }
        }
        Highlights.clearMark(1);
        Highlights.clearMark(2);

        for (int i = residue; i < length; i+=modulus) {
            Writes.write(array, i, tmp[i], 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] tmp = Writes.createExternalArray(length);

        merge(array, tmp, length, 0 , 1);

        Writes.deleteExternalArray(tmp);
    }
}

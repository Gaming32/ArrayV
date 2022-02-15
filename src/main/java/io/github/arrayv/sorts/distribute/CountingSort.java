package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
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

public final class CountingSort extends Sort {
    public CountingSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Counting");
        this.setRunAllSortsName("Counting Sort");
        this.setRunSortName("Counting Sort");
        this.setCategory("Distribution Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int max = Reads.analyzeMax(array, sortLength, 0, false);

        int[] output = Writes.copyOfArray(array, sortLength);
        int[] counts = Writes.createExternalArray(max + 1);

        for (int i = 0; i < sortLength; i++) {
            Writes.write(counts, array[i], counts[array[i]] + 1, 1, false, true);
            Highlights.markArray(1, i);
        }

        for (int i = 1; i < counts.length; i++) {
            Writes.write(counts, i, counts[i] + counts[i - 1], 1, true, true);
        }

        for (int i = sortLength - 1; i >= 0; i--) {
            output[counts[array[i]] - 1] = array[i];
            counts[array[i]]--;
        }

        // Extra loop to simulate the results from the "output" array being written
        // to the visual array.
        for (int i = sortLength - 1; i >= 0; i--) {
            Writes.write(array, i, output[i], 1, true, false);
            Writes.changeAuxWrites(1);
        }

        Writes.deleteExternalArray(output);
        Writes.deleteExternalArray(counts);
    }
}

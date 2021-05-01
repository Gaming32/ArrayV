package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.PDMergeSort;
import sorts.templates.Sort;

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

public class OptimizedPDMergeSort extends PDMergeSort {
    final static int MIN_RUN_SIZE = 16;

    public OptimizedPDMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized Pattern-Defeating Merge");
        this.setRunAllSortsName("Optimized Pattern-Defeating Merge Sort");
        this.setRunSortName("Optimized Pattern-Defeating Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void insertSort(int[] array, int start, int mid, int end) {
        int pos;
        int cur;
        for (int i = mid; i < end; i++) {
            cur = array[i];
            pos = i - 1;
            while (pos >= start && Reads.compareValues(array[pos], cur) > 0) {
                Writes.write(array, pos + 1, array[pos], 0.5, true, false);
                pos--;
            }
            Writes.write(array, pos + 1, cur, 0.5, true, false);
        }
    }

    protected int identifyRun(int[] array, int index, int maxIndex) {
        int startIndex = index;

        Highlights.markArray(1, index);
        if (index >= maxIndex) {
            return -1;
        }

        boolean cmp = compare(array[index], array[index + 1]);
        index++;
        Highlights.markArray(1, index);
        
        while (index < maxIndex) {
            Delays.sleep(1);
            boolean checkCmp = compare(array[index], array[index + 1]);
            if (checkCmp != cmp) {
                break;
            }
            index++;
            Highlights.markArray(1, index);
        }
        Delays.sleep(1);

        if (!cmp) {
            // arrayVisualizer.setHeading("PDMerge -- Reversing Run");
            Writes.reversal(array, startIndex, index, 1, true, false);
            Highlights.clearMark(2);
            // arrayVisualizer.setHeading("PDMerge -- Finding Runs");
        }
        int length = index - startIndex + 1;
        if (length < MIN_RUN_SIZE) {
            int end = startIndex + MIN_RUN_SIZE;
            if (end > maxIndex + 1) {
                end = maxIndex + 1;
            }
            insertSort(array, startIndex, index + 1, end);
            return end > maxIndex ? -1 : end;
        }
        if (index >= maxIndex) {
            return -1;
        }
        return index + 1;
    }

    protected int[] findRuns(int[] array, int maxIndex) {
        int[] runs = Writes.createExternalArray(maxIndex / MIN_RUN_SIZE + 2);
        runCount = 0;

        int lastRun = 0;
        while (lastRun != -1) {
            Writes.write(runs, runCount++, lastRun, 0.5, true, true);
            int newRun = identifyRun(array, lastRun, maxIndex);
            lastRun = newRun;
        }

        return runs;
    }
}
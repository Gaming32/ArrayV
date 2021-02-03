package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
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

final public class OptimizedPDMergeSort extends Sort {
    final static int MIN_RUN_SIZE = 16;
    int[] copied;
    int runCount;

    public OptimizedPDMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized PD Merge");
        this.setRunAllSortsName("Pattern-Defeating Merge Sort");
        this.setRunSortName("Pattern-Defeating Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void mergeUp(int[] array, int start, int mid, int end) {
        for (int i = 0; i < mid - start; i++) {
            Highlights.markArray(1, i + start);
            Writes.write(copied, i, array[i + start], 1, false, true);
        }

        int bufferPointer = 0;
        int left = start;
        int right = mid;

        while (left < right && right < end) {
            Highlights.markArray(2, right);
            if (Reads.compareValues(copied[bufferPointer], array[right]) <= 0)
                Writes.write(array, left++, copied[bufferPointer++], 1, true, false);
            else
                Writes.write(array, left++, array[right++], 1, true, false);
        }
        Highlights.clearMark(2);
    
        while (left < right)
            Writes.write(array, left++, copied[bufferPointer++], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    private void mergeDown(int[] array, int start, int mid, int end) {
        for (int i = 0; i < end - mid; i++) {
            Highlights.markArray(1, i + mid);
            Writes.write(copied, i, array[i + mid], 1, false, true);
        }

        int bufferPointer = end - mid - 1;
        int left = mid - 1;
        int right = end - 1;

        while (right > left && left >= start) {
            Highlights.markArray(2, left);
            if (Reads.compareValues(copied[bufferPointer], array[left]) >= 0)
                Writes.write(array, right--, copied[bufferPointer--], 1, true, false);
            else
                Writes.write(array, right--, array[left--], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, right--, copied[bufferPointer--], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    private void merge(int[] array, int leftStart, int rightStart, int end) {
        if (end - rightStart < rightStart - leftStart) {
            // arrayVisualizer.setHeading("PDMerge -- Merging Down");
            mergeDown(array, leftStart, rightStart, end);
        } else {
            // arrayVisualizer.setHeading("PDMerge -- Merging Up");
            mergeUp(array, leftStart, rightStart, end);
        }
    }

    private boolean compare(int a, int b) {
        return Reads.compareValues(a, b) <= 0;
    }

    private int identifyRun(int[] array, int index, int maxIndex) {
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

        int length = index - startIndex + 1;
        if (length < MIN_RUN_SIZE) {
            int end = startIndex + MIN_RUN_SIZE;
            if (end > maxIndex + 1) {
                end = maxIndex + 1;
            }
            new InsertionSort(arrayVisualizer).customInsertSort(array, startIndex, end, 0.5, false);
            return end > maxIndex ? -1 : end;
        }
        if (!cmp) {
            // arrayVisualizer.setHeading("PDMerge -- Reversing Run");
            Writes.reversal(array, startIndex, index, 1, true, false);
            Highlights.clearMark(2);
            // arrayVisualizer.setHeading("PDMerge -- Finding Runs");
        }
        if (index >= maxIndex) {
            return -1;
        }
        return index + 1;
    }

    private int[] findRuns(int[] array, int maxIndex) {
        int[] runs = Writes.createExternalArray((maxIndex + 2) / 2);
        runCount = 0;

        int lastRun = 0;
        while (lastRun != -1) {
            Writes.write(runs, runCount++, lastRun, 0.5, true, true);
            int newRun = identifyRun(array, lastRun, maxIndex);
            lastRun = newRun;
        }

        return runs;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // arrayVisualizer.setHeading("PDMerge -- Finding Runs");
        int[] runs = findRuns(array, length - 1);
        copied = Writes.createExternalArray(length / 2);
        
        // arrayVisualizer.setHeading("PDMerge -- Merging Runs");
        while (runCount > 1) {
            for (int i = 0; i < runCount - 1; i += 2) {
                int end = i + 2 >= runCount ? length : (runs[i + 2]);
                merge(array, runs[i], runs[i + 1], end);
            }
            for (int i = 1, j = 2; i < runCount; i++, j+=2, runCount--) {
                Writes.write(runs, i, runs[j], 0.5, true, true);
            }
        }

        // arrayVisualizer.setHeading("Pattern-Defeating Mergesort");

        Writes.deleteExternalArray(runs);
        Writes.deleteExternalArray(copied);
    }
}
package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32

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

final public class PDMergeSort extends Sort {
    int smallestRunSize;
    int[] copied;

    public PDMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("PD Merge");
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

    private void ensureCapacity(int length) {
        if (length > copied.length) {
            Writes.deleteExternalArray(copied);
            copied = Writes.createExternalArray(length);
        }
    }

    // Optimized from ParitialMergeSort.java
    private void mergeUp(int[] array, int leftStart, int rightStart, int end) {
        ensureCapacity(rightStart - leftStart);
        for (int i = 0; i < rightStart - leftStart; i++) {
            Highlights.markArray(1, i + leftStart);
            Writes.write(copied, i, array[i + leftStart], 1, false, true);
        }

        int left = leftStart;
        int right = rightStart;
        for(int nxt = 0; nxt < end - leftStart; nxt++){
            if(left >= rightStart && right >= end) break;

            Highlights.markArray(1, nxt + leftStart);
            Highlights.markArray(2, right);

            if(left < rightStart && right >= end){
                Highlights.clearMark(2);
                Writes.write(array, nxt + leftStart, copied[(left++) - leftStart], 1, false, false);
            }
            else if(left >= rightStart && right < end){
                Highlights.clearMark(1);
                break;
            }
            else if(Reads.compareValues(copied[left - leftStart], array[right]) <= 0){
                Writes.write(array, nxt + leftStart, copied[(left++) - leftStart], 1, false, false);
            }
            else {
                Writes.write(array, nxt + leftStart, array[right++], 1, false, false);
            }
        }

        Highlights.clearAllMarks();
    }

    private void mergeDown(int[] array, int leftStart, int rightStart, int end) {
        ensureCapacity(end - rightStart);
        for (int i = 0; i < end - rightStart; i++) {
            Highlights.markArray(1, i + rightStart);
            Writes.write(copied, i, array[i + rightStart], 1, false, true);
        }

        int left = rightStart - 1;
        int right = end;
        for (int nxt = end - leftStart - 1; nxt >= 0; nxt--) {
            if (left <= leftStart && right <= rightStart) break;

            Highlights.markArray(1, leftStart + nxt);
            Highlights.markArray(2, (int)Math.max(left, 0));

            if (left < leftStart && right >= leftStart) {
                Highlights.clearMark(2);
                Writes.write(array, leftStart + nxt, copied[(right--) - rightStart - 1], 1, false, false);
            }
            else if ((left >= leftStart && right < leftStart) || right < rightStart + 1) {
                Highlights.clearMark(1);
                break;
            }
            else if (Reads.compareValues(array[left], copied[right - rightStart - 1]) <= 0) {
                Writes.write(array, leftStart + nxt, copied[(right--) - rightStart - 1], 1, false, false);
            }
            else {
                Writes.write(array, leftStart + nxt, array[left--], 1, false, false);
            }
        }

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
        if (cmp == false) {
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

    private ArrayList<Integer> findRuns(int[] array, int maxIndex) {
        ArrayList<Integer> runs = new ArrayList<>();

        int lastRun = 0;
        while (lastRun != -1) {
            Writes.arrayListAdd(runs, lastRun);
            Writes.mockWrite(runs.size(), runs.size() - 1, lastRun, 0);
            int newRun = identifyRun(array, lastRun, maxIndex);
            if (newRun == -1) {
                smallestRunSize = Math.min(smallestRunSize, maxIndex - lastRun + 1);
            }
            else {
                int runSize = newRun - lastRun + 1;
                smallestRunSize = Math.min(smallestRunSize, runSize);
            }
            lastRun = newRun;
        }

        return runs;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // arrayVisualizer.setHeading("PDMerge -- Finding Runs");
        smallestRunSize = 0;
        ArrayList<Integer> runs = findRuns(array, length - 1);
        copied = Writes.createExternalArray(smallestRunSize);
        
        // arrayVisualizer.setHeading("PDMerge -- Merging Runs");
        while (runs.size() > 1) {
            for (int i = 0; i < runs.size() - 1; i += 2) {
                int end = i + 2 >= runs.size() ? length : (runs.get(i + 2));
                merge(array, runs.get(i), runs.get(i + 1), end);
            }
            for (int i = 1; i < runs.size(); i++) {
                Writes.arrayListRemoveAt(runs, i);
            }
        }

        // arrayVisualizer.setHeading("Pattern-Defeating Mergesort");

        Writes.deleteArrayList(runs);
        Writes.deleteExternalArray(copied);
    }
}
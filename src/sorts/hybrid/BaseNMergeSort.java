package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
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

final public class BaseNMergeSort extends Sort {
    int[] tmp;

    private InsertionSort insertSorter;

    public BaseNMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Base-N Merge");
        this.setRunAllSortsName("Base-N Merge Sort, 4 Bases");
        this.setRunSortName("Base-N Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(true);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the number of bases for this sort:", 4);
    }

    private int[] copyStarts(int[] starts, int baseCount) {
        int[] copy = new int[baseCount];
        Writes.changeAllocAmount(copy.length);
        for (int i = 0; i < baseCount; i++) {
            copy[i] = starts[i];
            Writes.write(copy, i, starts[i], 0, false, true);
        }
        return copy;
    }

    private void highlightMerge(int[] starts, int[] copiedStarts, int end, int baseCount) {
        for (int i = 0; i < baseCount; i++) {
            int startEnd = i == baseCount - 1 ? end : starts[i + 1];
            if (copiedStarts[i] < startEnd) {
                Highlights.markArray(i + 1, copiedStarts[i]);
            }
            else {
                Highlights.clearMark(i + 1);
            }
        }
    }

    private void merge(int[] array, int[] starts, int end, int baseCount) {
        int[] copiedStarts = copyStarts(starts, baseCount);

        int length = end - starts[0];

        for (int nxt = 0; nxt < length; nxt++) {
            highlightMerge(starts, copiedStarts, end, baseCount);
            int minValue = Integer.MAX_VALUE;
            int minIndex = 0;
            for (int i = 0; i < baseCount; i++) {
                int startEnd = i == baseCount - 1 ? end : starts[i + 1];
                if (copiedStarts[i] >= startEnd) {
                    continue;
                }
                if (Reads.compareValues(array[copiedStarts[i]], minValue) == -1) {
                    minValue = array[copiedStarts[i]];
                    minIndex = i;
                }
            }
            Writes.write(tmp, nxt, minValue, 1, false, true);
            Writes.write(copiedStarts, minIndex, copiedStarts[minIndex] + 1, 0, false, true);
        }
        Highlights.clearAllMarks();

        for(int i = 0; i < length; i++){
            Writes.write(array, starts[0] + i, tmp[i], 1, true, false);
        }
        
        Writes.changeAllocAmount(-copiedStarts.length);
    }

    private void mergeRun(int[] array, int[] starts, int end, int baseCount) {
        if (end - starts[0] < baseCount) {
            insertSorter.customInsertSort(array, starts[0], end, 1, false);
            Writes.deleteExternalArray(starts);
            return;
        }

        for (int i = 0; i < baseCount; i++) {
            int[] subStarts = calculateStarts(starts[i], starts[1] - starts[0], baseCount);
            int startEnd = i == baseCount - 1 ? end : starts[i + 1];
            mergeRun(array, subStarts, startEnd, baseCount);
        }

        merge(array, starts, end, baseCount);

        Writes.changeAllocAmount(-starts.length);
    }

    private int[] calculateStarts(int start, int length, int baseCount) {
        int[] starts = new int[baseCount];
        Writes.changeAllocAmount(starts.length);
        int size = length / baseCount;
        int current = start;

        for (int i = 0; i < baseCount; i++) {
            Writes.write(starts, i, current, 0, false, true);
            current += size;
        }

        return starts;
    }

    private double logBase(int value, int base) {
        double result;
        Writes.startLap();
        result = Math.log(value) / Math.log(base);
        Writes.stopLap();
        return result;
    }

    // Copied from MergeSorting.java:52
    // Used for merging wrong powers
    private void mergeBase2(int[] array, int start, int mid, int end) {
        int length = end - start;
        
        int low = start;
        int high = mid;
        
        for(int nxt = 0; nxt < length; nxt++){
            if(low >= mid && high >= end) break;
            
            Highlights.markArray(1, low);
            Highlights.markArray(2, high);
            
            if(low < mid && high >= end){
                Highlights.clearMark(2);
                Writes.write(tmp, nxt, array[low++], 1, false, true);
            }
            else if(low >= mid && high < end){
                Highlights.clearMark(1);
                Writes.write(tmp, nxt, array[high++], 1, false, true);
            }
            else if(Reads.compareValues(array[low], array[high]) == -1){
                Writes.write(tmp, nxt, array[low++], 1, false, true);
            }
            else{
                Writes.write(tmp, nxt, array[high++], 1, false, true);
            }
        }
        Highlights.clearMark(2);
        
        for(int i = 0; i < length; i++){
            Writes.write(array, start + i, tmp[i], 1, true, false);
        }
    }

    private void baseNMerge(int[] array, int length, int baseCount) {
        boolean useBinary = false;
        double logBaseCount = logBase(baseCount, 2);
        if (Math.pow(2, logBaseCount) == Math.pow(2, (int)logBaseCount)) {
            useBinary = true;
        }

        int start = 0;

        int lengthBase = useBinary ? 2 : baseCount;
        double logLength = logBase(length, lengthBase);
        if (Math.pow(lengthBase, (int)logLength) < Math.pow(lengthBase, logLength)) {
            start = (int)(length - Math.pow(lengthBase, (int)logLength));
        }

        int[] starts = calculateStarts(start, length - start, baseCount);
        
        mergeRun(array, starts, length, baseCount);

        if (start > 0) {
            baseNMerge(array, start, baseCount);
            mergeBase2(array, 0, start, length);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int baseCount) throws Exception {
        this.setRunAllSortsName("Base-N Merge Sort, " + baseCount + " Bases");

        insertSorter = new InsertionSort(arrayVisualizer);

        tmp = Writes.createExternalArray(length);

        baseNMerge(array, length, baseCount);

        Writes.deleteExternalArray(tmp);
    }
}
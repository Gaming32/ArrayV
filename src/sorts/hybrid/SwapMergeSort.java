package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
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

final public class SwapMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;

    public SwapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("SwapMerge");
        this.setRunAllSortsName("SwapMerge Sort");
        this.setRunSortName("SwapMergeSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void moveDown(int[] array, int start, int dest) {
        for (int i = dest; i < start; i++) {
            Writes.swap(array, i, start, 0.025, true, false);
        }
    }

    private void merge(int[] array, int leftStart, int rightStart, int end) {
        int left = leftStart;
        int right = rightStart;

        while (left < right) {
            if (left >= end || right >= end) {
                break;
            }
            else if (Reads.compareValues(array[left], array[right]) <= 0) {
                left += 1;
            }
            else {
                moveDown(array, right, left);
                left += 1;
                right += 1;
            }
        }
    }

    private void mergeRun(int[] array, int start, int mid, int end) {
        if(start == mid) return;

        mergeRun(array, start, (mid+start)/2, mid);
        mergeRun(array, mid, (mid+end)/2, end);

        if(end - start < 32) {
            return;
        }
        else if(end - start == 32) {
            binaryInserter.customBinaryInsert(array, start, Math.min(array.length, end + 1), 0.333);
        }
        else {
            merge(array, start, mid, end);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        binaryInserter = new BinaryInsertionSort(arrayVisualizer);
        
        if(length < 32) {
            binaryInserter.customBinaryInsert(array, 0, length, 0.333);
            return;
        }
        
        int start = 0;
        int end = length;
        int mid = start + ((end - start) / 2);
        
        mergeRun(array, start, mid, end);
    }
}

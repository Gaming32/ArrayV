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

final public class BlockSelectionMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;

    public BlockSelectionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Block Selection Merge");
        this.setRunAllSortsName("Block Selection Merge Sort");
        this.setRunSortName("Block Selection Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void swapBlocks(int[] array, int left, int right, int length, double sleep) {
        for (int i = 0; i < length; i++) {
            Writes.swap(array, left + i, right + i, sleep, true, false);
        }
    }

    private void blockSelection(int[] array, int start, int end, int blockSize) {
        for (int i = start; i < end - blockSize; i += blockSize) {
            int lowestindex = i;

            for (int j = i + blockSize; j < end; j += blockSize) {
                Highlights.markArray(2, j);
                Delays.sleep(0.01);

                if (Reads.compareValues(array[j], array[lowestindex]) == -1){
                    lowestindex = j;
                    Highlights.markArray(1, lowestindex);
                    Delays.sleep(0.01);
                }
            }
            if (lowestindex > i)
                swapBlocks(array, i, lowestindex, blockSize, blockSize / 64);
            Delays.sleep(0.5);
        }
    }

    private void merge(int[] array, int start, int end) {
        int blockSize = (end - start) / 16;
        while (blockSize >= 4) {
            blockSelection(array, start, end, blockSize);
            blockSize /= 8;
        }
        binaryInserter.customBinaryInsert(array, start, end, 0.333);
    }

    private void mergeRun(int[] array, int start, int mid, int end, int minSize) {
        if(start == mid) return;

        if (end - start == minSize) {
            binaryInserter.customBinaryInsert(array, start, end, 0.333);
            return;
        }

        mergeRun(array, start, (mid+start)/2, mid, minSize);
        mergeRun(array, mid, (mid+end)/2, end, minSize);

        merge(array, start, end);
    }

    public void moveExtraDown(int[] array, int start, int dest, int size) {
        int amount = start - dest;
        if (size > 1) {
            while (amount >= size) {
                for (int i = start; i > start - size; i--) {
                    Writes.swap(array, i - 1, i + size - 1, 0.5, true, false);
                }
                start -= size;
                amount -= size;
            }
            binaryInserter.customBinaryInsert(array, dest, dest + amount + size, 0.333);
        }
        else {
            for (int i = start; i > dest; i--) {
                Writes.swap(array, i, i - 1, 0.5, true, false);
            }
        }
    }

    public void mergeExtra(int[] array, int start, int mid, int end) {
        int lastValue = Integer.MIN_VALUE;

        while (start < mid) {
            int size = 0;
            for (int i = mid; i < end; i++) {
                if (Reads.compareValues(array[i], array[start]) == -1 && Reads.compareValues(array[i], lastValue) >= 0) {
                    Highlights.markArray(1, i);
                    size++;
                }
                else break;
                Delays.sleep(1);
            }
            if (size > 0) {
                moveExtraDown(array, mid, start, size);
            }

            start += size + 1;
            mid += size;
            lastValue = array[start - 1];
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        binaryInserter = new BinaryInsertionSort(arrayVisualizer);
        
        Writes.startLap();
        int minSize = (int)(Math.log(length) / Math.log(2)) / 3 + 2;
        minSize = (int)Math.pow(2, minSize);
        Writes.stopLap();
        if (length <= minSize) {
            binaryInserter.customBinaryInsert(array, 0, length, 0.333);
            return;
        }

        Writes.startLap();
        int useLength = (int)Math.pow(2, Math.floor(Math.log(length) / Math.log(2)));
        Writes.stopLap();
        
        int start = length - useLength;
        int end = length;
        int mid = start + ((end - start) / 2);
        
        mergeRun(array, start, mid, end, minSize);
        if (length > useLength) {
            runSort(array, length - useLength, bucketCount);
            mergeExtra(array, 0, length - useLength, end);
        }
    }
}
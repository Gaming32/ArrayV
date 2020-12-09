package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.merge.ReverseLazyStableSort;
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
    private ReverseLazyStableSort extraMerger;

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

    public void swapBlocks(int[] array, int left, int right, int length, double sleep) {
        for (int i = 0; i < length; i++) {
            Writes.swap(array, left + i, right + i, sleep, true, false);
        }
    }

    public void blockSelection(int[] array, int start, int end, int blockSize, double compSleep, double writeSleep) {
        for (int i = start; i < end - blockSize; i += blockSize) {
            int lowestindex = i;

            for (int j = i + blockSize; j < end; j += blockSize) {
                Highlights.markArray(2, j);
                Delays.sleep(compSleep);

                if (Reads.compareValues(array[j], array[lowestindex]) == -1){
                    lowestindex = j;
                    Highlights.markArray(1, lowestindex);
                    Delays.sleep(compSleep);
                }
            }
            if (lowestindex > i)
                swapBlocks(array, i, lowestindex, blockSize, writeSleep);
            Delays.sleep(0.5);
        }
    }

    private void merge(int[] array, int start, int end) {
        int blockSize = (end - start) / 16;
        while (blockSize >= 4) {
            blockSelection(array, start, end, blockSize, 0.01, 0.5);
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
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        binaryInserter = new BinaryInsertionSort(arrayVisualizer);
        extraMerger = new ReverseLazyStableSort(arrayVisualizer);
        
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
            extraMerger.merge(array, 0, length - useLength, end);
        }
    }
}
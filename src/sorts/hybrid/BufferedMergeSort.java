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

final public class BufferedMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;
    private ReverseLazyStableSort finalMerger;
    private BlockSelectionMergeSort blockSelector;

    public BufferedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Buffered Merge");
        this.setRunAllSortsName("Buffered Merge Sort");
        this.setRunSortName("Buffered Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    private static int getBufferSize(int length) {
        return (int)Math.pow(2, Math.ceil(log2((int)log2(length)))) * 2;
    }

    private void mergeUnderBuffer(int[] array, int bufferSize, int start, int mid, int end) {
        int bufferPointer = 0;
        int left = start, right = mid;

        while (left < mid && right < end) {
            if (Reads.compareIndices(array, left, right, 0, true) <= 0) {
                Writes.swap(array, bufferPointer, left, 0.25, true, false);
                left++;
            }
            else {
                Writes.swap(array, bufferPointer, right, 0.25, true, false);
                right++;
            }
            bufferPointer++;
        }

        while (left < mid) {
            Writes.swap(array, bufferPointer, left, 0.25, true, false);
            left++;
            bufferPointer++;
        }
        while (right < end) {
            Writes.swap(array, bufferPointer, right, 0.25, true, false);
            right++;
            bufferPointer++;
        }

        for (int i = 0; i < end - start; i++) {
            Writes.swap(array, i, start + i, 0.5, true, false);
        }
    }

    private void mergeOverBuffer(int[] array, int bufferSize, int start, int mid, int end) {
        int blockSize = bufferSize / 2;
        blockSelector.blockSelection(array, start, end, blockSize, 0.025, 1);

        int checkStart = start;
        while (checkStart < end - blockSize) {
            if (Reads.compareIndices(array, checkStart + blockSize - 1, checkStart + blockSize, 1, true) == 1) {
                mergeUnderBuffer(array, bufferSize, checkStart, checkStart + blockSize, checkStart + bufferSize);
            }
            checkStart += blockSize;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        binaryInserter = new BinaryInsertionSort(arrayVisualizer);
        finalMerger = new ReverseLazyStableSort(arrayVisualizer);
        blockSelector = new BlockSelectionMergeSort(arrayVisualizer);
        
        int bufferSize = BufferedMergeSort.getBufferSize(sortLength);
        int length = sortLength - ((sortLength - bufferSize) % (bufferSize / 2));
        if (bufferSize * 2 >= length) {
            binaryInserter.customBinaryInsert(array, 0, sortLength, 0.333);
            return;
        }

        for (int i = bufferSize; i < length - 1; i += 2) {
            if (Reads.compareIndices(array, i, i + 1, 0.5, true) == 1) {
                Writes.swap(array, i, i + 1, 0.5, true, false);
            }
        }

        for (int gap = 4; gap <= bufferSize; gap *= 2) {
            for (int i = bufferSize; i + gap <= length; i += gap) {
                mergeUnderBuffer(array, bufferSize, i, i + gap / 2, i + gap);
            }
        }

        for (int gap = bufferSize * 2; gap / 2 <= length; gap *= 2) {
            int i;
            for (i = bufferSize; i + gap <= length; i += gap) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, i + gap);
            }
            if (i + gap > length) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, length);
            }
        }

        runSort(array, bufferSize, bucketCount);
        finalMerger.merge(array, 0, bufferSize, length);
        if (sortLength - length > 0) {
            binaryInserter.customBinaryInsert(array, length, sortLength, 0.333);
            finalMerger.merge(array, 0, length, sortLength);
        }
    }
}
package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.TriSearchInsertionSort;
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

final public class StableBufferedMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;
    private BlockSelectionMergeSort blockSelector;
    private TriSearchInsertionSort oddInsertSearcher;

    private int[] buffer;

    public StableBufferedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stable Buffered Merge");
        this.setRunAllSortsName("Stable Buffered Merge Sort");
        this.setRunSortName("Stable Buffered Mergesort");
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

    private void oddInsert(int[] array, int end) {
        int value = array[end];
        int dest = oddInsertSearcher.triSearch(array, 0, end - 1, value, 10);
        Highlights.clearAllMarks();
        Highlights.markArray(2, dest);

        for (int i = end; i > dest; i--) {
            Writes.write(array, i, array[i - 1], 1, true, false);
        }
        Writes.write(array, dest, value, 1, true, false);
    }

    private void mergeUnderBuffer(int[] array, int bufferSize, int start, int mid, int end) {
        int bufferPointer = 0;
        int left = start, right = mid;

        while (left < mid && right < end) {
            if (Reads.compareIndices(array, left, right, 0, true) == -1) {
                Highlights.markArray(2, left);
                Writes.write(this.buffer, bufferPointer, array[left], 0.25, true, true);
                left++;
            }
            else {
                Highlights.markArray(2, right);
                Writes.write(this.buffer, bufferPointer, array[right], 0.25, true, true);
                right++;
            }
            bufferPointer++;
        }

        while (left < mid) {
            Highlights.markArray(2, left);
            Writes.write(this.buffer, bufferPointer, array[left], 0.25, true, true);
            left++;
            bufferPointer++;
        }
        while (right < end) {
            Highlights.markArray(2, right);
            Writes.write(this.buffer, bufferPointer, array[right], 0.25, true, true);
            right++;
            bufferPointer++;
        }

        for (int i = 0; i < end - start; i++) {
            Highlights.markArray(2, i);
            Writes.write(array, start + i, this.buffer[i], 0.5, true, false);
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
        blockSelector = new BlockSelectionMergeSort(arrayVisualizer);
        oddInsertSearcher = new TriSearchInsertionSort(arrayVisualizer);

        boolean isOddLength = sortLength / 2 * 2 != sortLength;
        int length = isOddLength ? sortLength - 1 : sortLength;
        
        int bufferSize = StableBufferedMergeSort.getBufferSize(length);
        if (bufferSize * 2 >= length) {
            binaryInserter.customBinaryInsert(array, 0, sortLength, 0.333);
            return;
        }
        this.buffer = Writes.createExternalArray(bufferSize);

        for (int i = 0; i < length - 1; i += 2) {
            if (Reads.compareIndices(array, i, i + 1, 0.5, true) == 1) {
                Writes.swap(array, i, i + 1, 0.5, true, false);
            }
        }

        for (int gap = 4; gap <= bufferSize; gap *= 2) {
            for (int i = 0; i + gap <= length; i += gap) {
                mergeUnderBuffer(array, bufferSize, i, i + gap / 2, i + gap);
            }
        }

        for (int gap = bufferSize * 2; gap <= length; gap *= 2) {
            int i;
            for (i = 0; i + gap <= length; i += gap) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, i + gap);
            }
            if (i + gap > length) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, length);
            }
        }

        if (isOddLength) {
            oddInsert(array, length);
        }
        Writes.deleteExternalArray(this.buffer);
    }
}
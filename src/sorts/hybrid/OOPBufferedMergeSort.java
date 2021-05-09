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

final public class OOPBufferedMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;
    private ReverseLazyStableSort finalMerger;

    private int[] buffer;

    public OOPBufferedMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Out-of-Place Buffered Merge");
        this.setRunAllSortsName("Out-of-Place Buffered Merge Sort");
        this.setRunSortName("Out-of-Place Buffered Mergesort");
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

    private void blockCopy(int array[], int start, int end) {
        int blockSize = end - start;
        for (int i = end - 1; i >= start; i--) {
            Writes.write(array, i, array[i - blockSize], 0.125, true, false);
        }
    }

    private void blockCopyFromBuffer(int array[], int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            Highlights.markArray(2, i - start);
            Writes.write(array, i, buffer[i - start], 0.125, true, false);
        }
        Highlights.clearMark(2);
    }

    private void blockInsertionSort(int[] array, int start, int mid, int end, int blockSize) {
        for (int i = mid; i < end; i += blockSize) {
            int key = array[i];
            int j = i - blockSize;

            if (Reads.compareValues(key, array[j]) >= 0) {
                continue;
            }

            for (int k = i; k < i + blockSize; k++) {
                Highlights.markArray(2, k);
                Writes.write(buffer, k - i, array[k], 0.5, true, true);
            }
            Highlights.clearMark(2);

            blockCopy(array, j + blockSize, j + 2 * blockSize);
            j -= blockSize;

            while (j >= start && Reads.compareValues(key, array[j]) < 0) {
                blockCopy(array, j + blockSize, j + 2 * blockSize);
                j -= blockSize;
            }
            blockCopyFromBuffer(array, j + blockSize, j + 2 * blockSize);
        }
    }

    private void mergeOverBuffer(int[] array, int bufferSize, int start, int mid, int end) {
        int blockSize = bufferSize / 2;
        blockInsertionSort(array, start, mid, end, blockSize);

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
        
        int bufferSize = OOPBufferedMergeSort.getBufferSize(sortLength);
        int length = sortLength - ((sortLength - bufferSize) % (bufferSize / 2));
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

        for (int gap = bufferSize * 2; gap / 2 <= length; gap *= 2) {
            int i;
            for (i = 0; i + gap <= length; i += gap) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, i + gap);
            }
            if (i + gap > length) {
                mergeOverBuffer(array, bufferSize, i, i + gap / 2, length);
            }
        }

        if (sortLength - length > 0) {
            binaryInserter.customBinaryInsert(array, length, sortLength, 0.333);
            finalMerger.merge(array, 0, length, sortLength);
        }
        Writes.deleteExternalArray(this.buffer);
    }
}
package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.merge.BlockSwapMergeSort;
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

final public class ThirdMergeSort extends Sort {
    private BinaryInsertionSort binaryInserter;
    private BlockSwapMergeSort finalMerger;

    public ThirdMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Third Merge");
        this.setRunAllSortsName("Third Merge Sort");
        this.setRunSortName("Third Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void blockSwap(int[] array, int a, int b, int size) {
        for (int i = 0; i < size; i++) {
            Writes.swap(array, a + i, b + i, 1, true, false);
        }
    }

    private void merge(int[] array, int bufferSize, int start, int mid, int end) {
        blockSwap(array, 0, start, mid - start);

        int bufferPointer = 0;
        int left = start, right = mid;

        while (left < right && right < end) {
            if (Reads.compareIndices(array, bufferPointer, right, 0.1, true) <= 0) {
                Writes.swap(array, bufferPointer++, left++, 0.25, true, false);
            }
            else {
                Writes.swap(array, left++, right++, 0.25, true, false);
            }
        }

        while (left < right) {
            Writes.swap(array, bufferPointer++, left++, 0.25, true, false);
        }
    }

    private int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        binaryInserter = new BinaryInsertionSort(arrayVisualizer);
        finalMerger = new BlockSwapMergeSort(arrayVisualizer);
        
        if (sortLength <= 32) {
            binaryInserter.customBinaryInsert(array, 0, sortLength, 0.333);
            return;
        }
        int thirdSize = sortLength / 3;
        int useLength = thirdSize * 3;

        for (int i = thirdSize; i < useLength - 1; i += 2) {
            if (Reads.compareIndices(array, i, i + 1, 0.5, true) == 1) {
                Writes.swap(array, i, i + 1, 0.5, true, false);
            }
        }

        int subStart, subEnd = useLength, subLength = useLength - thirdSize;
        int gap;
        for (int parlen = subLength; parlen >= 2; parlen = subEnd - thirdSize) {
            subLength = pow2lte(parlen);
            subStart = subEnd - subLength;
            
            for (gap = 4; gap <= subLength; gap *= 2) {
                for (int i = subStart; i + gap <= subEnd; i += gap) {
                    merge(array, thirdSize, i, i + gap / 2, i + gap);
                }
            }
            if (parlen != useLength - thirdSize) {
                merge(array, thirdSize, subStart, subEnd, useLength);
            }
            subEnd = subStart;
        }

        int extra = sortLength - useLength;
        if (extra > 0) {
            if (extra > 1 && Reads.compareIndices(array, sortLength - 2, sortLength - 1, 0.5, true) == 1) {
                Writes.swap(array, sortLength - 2, sortLength - 1, 0.5, true, false);
            }
            finalMerger.multiSwapMerge(array, thirdSize, useLength, sortLength);
        }

        runSort(array, thirdSize, bucketCount);
        finalMerger.multiSwapMerge(array, 0, thirdSize, sortLength);
    }
}
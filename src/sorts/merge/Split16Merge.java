package sorts.merge;

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

final public class Split16Merge extends Sort {
    public Split16Merge(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Split-16 Merge");
        this.setRunAllSortsName("Split-16 Merge Sort");
        this.setRunSortName("Split-16 Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int[] medianOfSixteenSwaps = new int[] {   
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
        1, 3, 5, 7, 9, 11, 13, 15, 2, 4, 6, 8, 10, 12, 14, 16,
        1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16,
        1, 9, 2, 10, 3, 11, 4, 12, 5, 13, 6, 14, 7, 15, 8, 16,
        6, 11, 7, 10, 4, 13, 14, 15, 8, 12, 2, 3, 5, 9,
        2, 5, 8, 14, 3, 9, 12, 15, 6, 7, 10, 11,
        3, 5, 12, 14, 4, 9, 8, 13,
        7, 9, 11, 13, 4, 6, 8, 10, 
        4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
        7, 8, 9, 10
    };

    private void compSwap1(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 1, true) > 0) {
            Writes.swap(array, a, b, 0.5, true, false);
        }
    }

    private void compSwap2(int[] array, int a, int b, int gap, int start) {
        if (Reads.compareIndices(array, start+(a*gap), start+(b*gap), 1, true) > 0) {
            Writes.swap(array, start+(a*gap), start+(b*gap), 0.5, true, false);
        }
    }

    private void medianOfSixteen(int[] array, int a, int gap) {
        for (int i = 0; i < this.medianOfSixteenSwaps.length; i += 2)
            compSwap2(array, this.medianOfSixteenSwaps[i] - 1, this.medianOfSixteenSwaps[i+1] - 1, gap, a);
    }

    private void merge(int[] array, int start, int size) {
        int gap = size / 16;
        for (int i = 0; i < gap; i++) {
            medianOfSixteen(array, start + i, gap);
        }
        for (int subgap = gap / 2; subgap > 0; subgap /= 2) {
            for (int i = 0; i < size - subgap; i++) {
                compSwap1(array, start + i, start + i + subgap);
            }
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length - 15; i += 16) {
            this.medianOfSixteen(array, i, 1);
        }

        int gap;
        for (gap = 32; gap <= length; gap *= 2) {
            for (int i = 0; i + gap <= length; i += gap) {
                merge(array, i, gap);
            }
        }
    }
}

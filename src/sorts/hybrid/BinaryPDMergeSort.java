package sorts.hybrid;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2021 Gaming32

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

public class BinaryPDMergeSort extends OptimizedPDMergeSort {
    public BinaryPDMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Binary Pattern-Defeating Merge");
        this.setRunAllSortsName("Binary Pattern-Defeating Merge Sort");
        this.setRunSortName("Binary Pattern-Defeating Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int monoboundLeft(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;
            
            if (Reads.compareValueIndex(array, value, end - mid, 0.5, true) <= 0) {
                end -= mid;
            }
            top -= mid;
        }

        if (Reads.compareValueIndex(array, value, end - 1, 0.5, true) <= 0) {
            return end - 1;
        }
        return end;
    }

    private int monoboundRight(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareIndexValue(array, start + mid, value, 0.5, true) <= 0) {
                start += mid;
            }
            top -= mid;
        }

        if (Reads.compareIndexValue(array, start, value, 0.5, true) <= 0) {
            return start + 1;
        }
        return start;
    }

    protected void merge(int[] array, int start, int mid, int end) {
        start = monoboundRight(array, start, mid, array[mid]);
        if (start == mid) return;
        end = monoboundLeft(array, mid, end, array[mid - 1]);
        super.merge(array, start, mid, end);
    }
}
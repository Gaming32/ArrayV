package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.ArrayVList;

/*
 *
MIT License

Copyright (c) 2017 Rodney Shaghoulian
Copyright (c) 2020 Josiah Glosson

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

final public class StableQuickSortMiddlePivot extends Sort {
    private int length;

    public StableQuickSortMiddlePivot(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Quick (Middle Pivot)");
        this.setRunAllSortsName("Stable Quick Sort (Middle Pivot)");
        this.setRunSortName("Stable Quicksort (Middle Pivot)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Author: Rodney Shaghoulian
    // Github: github.com/RodneyShag

    // Author: Josiah Glosson
    // Github: github.com/gaming32

    private void copy(ArrayVList list, int [] array, int startIndex) {
        for (int num : list) {
            Writes.write(array, startIndex++, num, 0.25, false, false);
            Highlights.markArray(1, startIndex);
        }
    }

    /* Partition/Quicksort "Stable Sort" version using O(n) space */
    private int stablePartition(int[] array, int start, int end) {
        int pivot = start + (end - start) / 2;
        int pivotValue = array[pivot];
        Highlights.markArray(3, start);
        Highlights.markArray(4, pivot);

        ArrayVList leftList  = Writes.createArrayList(this.length);
        ArrayVList rightList = Writes.createArrayList(this.length);

        for (int i = start ; i <= end; i++) {
            Highlights.markArray(1, i);
            if (i == pivot) continue;

            if (Reads.compareValues(array[i], pivotValue) == -1) {
                Writes.arrayListAdd(leftList, array[i], true, 0.25);
            }
            else {
                Writes.arrayListAdd(rightList, array[i], true, 0.25);
            }
        }

        /* Recreate array */
        this.copy(leftList, array, start);

        int newPivotIndex = start + leftList.size();

        Writes.write(array, newPivotIndex, pivotValue, 0.25, false, false);
        Highlights.markArray(1, newPivotIndex);

        this.copy(rightList, array, newPivotIndex + 1);

        Writes.deleteArrayList(leftList);
        Writes.deleteArrayList(rightList);

        return newPivotIndex;
    }

    private void stableQuickSort(int [] array, int start, int end) {
        if (start < end) {
            int pivotIndex = this.stablePartition(array, start, end);
            this.stableQuickSort(array, start, pivotIndex - 1);
            this.stableQuickSort(array, pivotIndex + 1, end);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.length = length;
        this.stableQuickSort(array, 0, length - 1);
    }
}
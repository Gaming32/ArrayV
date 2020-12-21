package sorts.exchange;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.merge.ReverseLazyStableSort;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32 (Josiah Glosson)

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

final public class InPlaceQuickSort extends Sort {
    ReverseLazyStableSort rotater;

    public InPlaceQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("In-Place Quick");
        this.setRunAllSortsName("In-Place Stable Quick Sort");
        this.setRunSortName("In-Place Quicksort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int stablePartition(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        int pivot = array[mid];

        int ltLeft = start;
        int runstart = start;
        int runsize = 0;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[i], pivot) <= 0) {
                runsize++;
            }
            else {
                if (runsize > 0 && runstart > start) {
                    rotater.rotateSmart(array, runstart, ltLeft, runsize);
                }
                ltLeft += runsize;
                runstart = i + 1;
                runsize = 0;
            }
        }

        // Necessary if the run is at the end
        if (runsize > 0) {
            if (end - start == runsize) {
                int tmp = array[mid];
                for (int i = mid; i < end - 1; i++) {
                    Writes.write(array, i, array[i + 1], 2.5, true, false);
                }
                Writes.write(array, end - 1, tmp, 7.5, true, false);
                return stablePartition(array, start, end);
            }
            rotater.rotateSmart(array, runstart, ltLeft, runsize);
            ltLeft += runsize;
        }

        return ltLeft;
    }

    private void stableQuickSort(int [] array, int start, int end) {
        if (end - start == 1) return;
        else if (end - start == 2) {
            if (Reads.compareIndices(array, start, end - 1, 0.5, true) > 0) {
                Writes.swap(array, start, end - 1, 0.5, true, false);
            }
        }
        else if (start < end) {
            int pivotIndex = this.stablePartition(array, start, end);
            this.stableQuickSort(array, start, pivotIndex);
            this.stableQuickSort(array, pivotIndex, end);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        rotater = new ReverseLazyStableSort(arrayVisualizer);
        this.stableQuickSort(array, 0, length);
    }
}
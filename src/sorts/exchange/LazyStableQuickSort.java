package sorts.exchange;

import main.ArrayVisualizer;
import sorts.insert.BinaryDoubleInsertionSort;
import sorts.insert.InsertionSort;
import sorts.merge.ReverseLazyStableSort;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020-2021 Gaming32 (Josiah Glosson)

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

final public class LazyStableQuickSort extends Sort {
    ReverseLazyStableSort rotater;
    InsertionSort inserter;
    BinaryDoubleInsertionSort fallback;

    public LazyStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Lazy Stable Quick");
        this.setRunAllSortsName("Lazy Stable Quick Sort");
        this.setRunSortName("Lazy Stable Quicksort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    private int findPivot(int[] array, int start, int mid, int end) {
        if (Reads.compareIndices(array, start, mid, 0.5, true) < 0) {
            if (Reads.compareIndices(array, mid, end, 0.5, true) < 0) {
                return mid;
            } else if (Reads.compareIndices(array, start, end, 0.5, true) < 0) {
                return end;
            }
            return start;
        } else {
            if (Reads.compareIndices(array, mid, end, 0.5, true) > 0) {
                return mid;
            } else if (Reads.compareIndices(array, start, end, 0.5, true) < 0) {
                return start;
            }
            return end;
        }
    }
    
    private int stablePartition(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        int pivotPos = findPivot(array, start, mid, end - 1);
        while (true) {
            int pivot = array[pivotPos];

            int ltLeft = start;
            int runstart = start;
            int runsize = 0;
            int equalCount = 0;
            for (int i = start; i < end; i++) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                int comp = Reads.compareValues(array[i], pivot);
                if (comp < 0) {
                    runsize++;
                    continue;
                } else if (comp == 0) {
                    equalCount++;
                }
                if (runsize > 0 && runstart > start) {
                    rotater.rotateSmart(array, runstart, ltLeft, runsize);
                }
                ltLeft += runsize;
                runstart = i + 1;
                runsize = 0;
            }

            if (equalCount == end - start) return -1;

            // Necessary if the run is at the end
            if (runsize > 0) {
                rotater.rotateSmart(array, runstart, ltLeft, runsize);
                ltLeft += runsize;
            }

            if (ltLeft == start) {
                pivotPos++;
                if (pivotPos == end) {
                    pivotPos = start;
                }
                while (Reads.compareValueIndex(array, pivot, pivotPos, 1, true) == 0) {
                    pivotPos++;
                    if (pivotPos == end) {
                        pivotPos = start;
                    }
                }
                continue;
            }

            return ltLeft;
        }
    }

    private void stableQuickSort(int [] array, int start, int end, int depth) {
        while (end - start > 16) {
            if (depth == 0) {
                fallback.customDoubleInsert(array, start, end, 1);
                return;
            }
            int pivotIndex = this.stablePartition(array, start, end);
            if (pivotIndex == -1) return;
            int left = pivotIndex - start, right = end - pivotIndex;
            if (left > right) {
                this.stableQuickSort(array, pivotIndex, end, --depth);
                end = pivotIndex;
            } else {
                this.stableQuickSort(array, start, pivotIndex, --depth);
                start = pivotIndex;
            }
        }
        inserter.customInsertSort(array, start, end, 1, false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        rotater = new ReverseLazyStableSort(arrayVisualizer);
        inserter = new InsertionSort(arrayVisualizer);
        fallback = new BinaryDoubleInsertionSort(arrayVisualizer);
        this.stableQuickSort(array, 0, length, 2 * log2(length));
    }
}
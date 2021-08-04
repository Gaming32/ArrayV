package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class ooPQuicksort extends Sort {

    public ooPQuicksort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("OopsQuick");
        this.setRunAllSortsName("Out of Place Stable QuickSort (by Control)");
        this.setRunSortName("Out of Place Stable Quicksort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Based on Ideas by Scandum and Control
    private void ooPquickSort(int[] array, int[] swap, int start, int end, boolean forwards, boolean fromSwap) {

        int a = start;
        int e = end;
        int c = start + (end - start + 1) / 2;
        int b = start + (c - start + 1) / 2;
        int d = c + (end - c + 1) / 2;
        int temp = 0;

        if (end - start > 10) {
            if (array[a] > array[b]) {
                temp = a;
                a = b;
                b = temp;
            }
            Highlights.markArray(1, a);
            Highlights.markArray(2, b);
            Delays.sleep(0.25);
            if (array[c] > array[d]) {
                temp = c;
                c = d;
                d = temp;
            }
            Highlights.markArray(1, c);
            Highlights.markArray(2, d);
            Delays.sleep(0.25);
            if (array[b] > array[d]) {
                temp = b;
                b = d;
                d = temp;
            }
            Highlights.markArray(1, b);
            Highlights.markArray(2, d);
            Delays.sleep(0.25);
            if (array[a] > array[c]) {
                temp = a;
                a = c;
                c = temp;
            }
            Highlights.markArray(1, a);
            Highlights.markArray(2, c);
            Delays.sleep(0.25);
            if (array[c] > array[e]) {
                temp = c;
                c = e;
                e = temp;
            }
            Highlights.markArray(1, c);
            Highlights.markArray(2, e);
            Delays.sleep(0.25);
            if (array[b] > array[c]) {
                temp = b;
                b = c;
                c = temp;
            }
            Highlights.markArray(1, b);
            Highlights.markArray(2, c);
            Delays.sleep(0.25);
        }

        int pivotpos = array[c];
        Highlights.markArray(1, pivotpos);

        int pivot = array[pivotpos];

        int leftswappointer = start;
        Highlights.markArray(1, leftswappointer);
        int rightswappointer = end;
        Highlights.markArray(1, rightswappointer);

        array[pivotpos] = 0;

        if (forwards) {
            int pointer = start;
            Highlights.markArray(1, pointer);
            while ((leftswappointer < rightswappointer)) {
                if (pointer == pivotpos) {
                    pointer++;
                }
                if (Reads.compareValues(array[pointer], pivot) < 0) {
                    Writes.write(swap, leftswappointer, array[pointer], 0.05, false, fromSwap);
                    leftswappointer++;
                } else if (Reads.compareValues(array[pointer], pivot) > 0) {
                    Writes.write(swap, rightswappointer, array[pointer], 0.05, false, fromSwap);
                    rightswappointer--;
                } else if (Reads.compareValues(pointer, pivotpos) < 0) {
                    Writes.write(swap, leftswappointer, array[pointer], 0.05, false, fromSwap);
                    leftswappointer++;
                } else {
                    Writes.write(swap, rightswappointer, array[pointer], 0.05, false, fromSwap);
                    rightswappointer--;
                }
                array[pointer] = 0;
                pointer++;
                Highlights.markArray(1, pointer);
                Highlights.markArray(2, leftswappointer);
                Highlights.markArray(3, rightswappointer);
                Delays.sleep(0.25);
            }
        } else if (!forwards) {
            int pointer = end;
            Highlights.markArray(1, pointer);
            while ((leftswappointer < rightswappointer)) {
                if (pointer == pivotpos) {
                    pointer--;
                }
                if (Reads.compareValues(array[pointer], pivot) < 0) {
                    Writes.write(swap, leftswappointer, array[pointer], 0.05, false, fromSwap);
                    leftswappointer++;
                } else if (Reads.compareValues(array[pointer], pivot) > 0) {
                    Writes.write(swap, rightswappointer, array[pointer], 0.05, false, fromSwap);
                    rightswappointer--;
                } else if (Reads.compareValues(pointer, pivotpos) > 0) {
                    Writes.write(swap, leftswappointer, array[pointer], 0.05, false, fromSwap);
                    leftswappointer++;
                } else {
                    Writes.write(swap, rightswappointer, array[pointer], 0.05, false, fromSwap);
                    rightswappointer--;
                }
                array[pointer] = 0;
                pointer--;
                Highlights.markArray(1, pointer);
                Highlights.markArray(2, leftswappointer);
                Highlights.markArray(3, rightswappointer);
                Delays.sleep(0.25);
            }
        }

        if (!fromSwap) {
            Writes.write(array, rightswappointer, pivot, 0.05, true, false);
        } else {
            Writes.write(swap, rightswappointer, pivot, 0.05, true, false);
        }

        // recursively calls itself
        if (end - leftswappointer >= 1) {
            ooPquickSort(swap, array, leftswappointer + 1, end, false, !fromSwap);
        }
        if (rightswappointer - start >= 1) {
            ooPquickSort(swap, array, start, rightswappointer - 1, true, !fromSwap);
        }

    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] swaparray = Writes.createExternalArray(currentLength);
        this.ooPquickSort(array, swaparray, 0, currentLength - 1, true, false);
        Writes.deleteExternalArray(swaparray);
    }
}

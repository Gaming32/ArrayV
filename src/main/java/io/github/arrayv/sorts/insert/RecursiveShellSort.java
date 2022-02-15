package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class RecursiveShellSort extends Sort {
    public RecursiveShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Recursive Shell");
        this.setRunAllSortsName("yuji's Recursive Shell Sort");
        this.setRunSortName("yuji's Recursive Shell Sort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void gappedInsertionSort(int[] arr, int a, int b, int gap) {
        for (int i = a+gap; i < b; i+=gap) {
            int key = arr[i];
            int j = i-gap;

            while (j >= a && Reads.compareValues(key, arr[j]) < 0) {
                Writes.write(arr, j+gap, arr[j], 0.5, true, false);
                j-=gap;
            }
            Writes.write(arr, j+gap, key, 0.5, true, false);
        }
        Highlights.clearAllMarks();
    }

    public void recursiveShellSort(int[] array, int start, int end, int g) {
        if (start+g <= end) {
            this.recursiveShellSort(array, start, end, 3*g);
            this.recursiveShellSort(array, start+g, end, 3*g);
            this.recursiveShellSort(array, start+(2*g), end, 3*g);
            this.gappedInsertionSort(array, start, end, g);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.recursiveShellSort(array, 0, length, 1);
    }
}

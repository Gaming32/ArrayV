package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Snuffle", slowSort = true, unreasonableLimit = 100)
public class SnuffleSort extends Sort {
    private static double DELAY = 1;

    public SnuffleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void snuffleSort(int[] arr, int start, int stop) {
        if (stop - start + 1 >= 2) {
            Highlights.markArray(0, start);
            Highlights.markArray(1, stop);
            if (Reads.compareValues(arr[start], arr[stop]) == 1)
                Writes.swap(arr, start, stop, DELAY, false, false);
            if (stop - start + 1 >= 3) {
                int mid = (stop - start) / 2 + start;
                for (int i = 0; i < (int) Math.ceil((stop - start + 1) / 2); i++) {
                    this.snuffleSort(arr, start, mid);
                    this.snuffleSort(arr, mid, stop);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int buckets) {
        this.snuffleSort(array, 0, length - 1);
    }
}

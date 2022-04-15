package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
	name = "Recursive Diamond",
	listName = "Diamond (Recursive)"
)
public final class DiamondSortRecursive extends Sort {
    private final double DELAY = 0.05;
    public DiamondSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void sort(int[] arr, int start, int stop, boolean merge) {
        if (stop - start == 2) {
            if (Reads.compareIndices(arr, start, stop - 1, DELAY, true) == 1)
                Writes.swap(arr, start, stop - 1, DELAY, true, false);
        } else if (stop - start >= 3) {
            double div = (stop - start) / 4d;
            int mid = (stop - start) / 2 + start;
            if (merge) {
                this.sort(arr, start, mid, true);
                this.sort(arr, mid, stop, true);
            }
            this.sort(arr, (int) div + start, (int) (div * 3) + start, false);
            this.sort(arr, start, mid, false);
            this.sort(arr, mid, stop, false);
            this.sort(arr, (int) div + start, (int) (div * 3) + start, false);
        }
    }

    @Override
    public void runSort(int[] arr, int length, int buckets) {
        this.sort(arr, 0, length, true);
    }
}

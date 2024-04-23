package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Bose-Nelson (Recursive)", runName = "Recursive Bose-Nelson Sorting Network")
public final class BoseNelsonSortRecursive extends Sort {

    public BoseNelsonSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void compareSwap(int[] array, int start, int end, double sleep) {
        if (Reads.compareIndices(array, start, end, sleep, true) == 1) {
            Writes.swap(array, start, end, 2 * sleep, true, false);
        }
    }

    private void boseNelson(int[] array, int start, int length, double sleep) {
        if (length > 1) {
            int mid = length / 2;
            boseNelson(array, start, mid, sleep);
            boseNelson(array, start + mid, length - mid, sleep);
            boseNelsonMerge(array, start, mid, start + mid, length - mid, sleep);
        }
    }

    private void boseNelsonMerge(int[] array, int start1, int len1, int start2, int len2, double sleep) {
        if (len1 == 1 && len2 == 1) {
            compareSwap(array, start1, start2, sleep);
        } else if (len1 == 1 && len2 == 2) {
            compareSwap(array, start1, start2 + 1, sleep);
            compareSwap(array, start1, start2, sleep);
        } else if (len1 == 2 && len2 == 1) {
            compareSwap(array, start1, start2, sleep);
            compareSwap(array, start1 + 1, start2, sleep);
        } else {
            int mid1 = len1 / 2;
            int mid2 = len1 % 2 == 1 ? len2 / 2 : (len2 + 1) / 2;
            boseNelsonMerge(array, start1, mid1, start2, mid2, sleep);
            boseNelsonMerge(array, start1 + mid1, len1 - mid1, start2 + mid2, len2 - mid2, sleep);
            boseNelsonMerge(array, start1 + mid1, len1 - mid1, start2, mid2, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.boseNelson(array, 0, length, 0.25);
    }
}

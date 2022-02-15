package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Min-Max Heaps translated from https://github.com/BartMassey/minmaxheap
public final class MinMaxHeapSort extends Sort {
    int[] a;
    int start, end;

    public MinMaxHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Min-Max Heap");
        this.setRunAllSortsName("Min-Max Heap Sort");
        this.setRunSortName("Min-max Heapsort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean compare(int x, int y, boolean isGt) {
        if (isGt) {
            int z = x;
            x = y;
            y = z;
        }
        return Reads.compareValues(x, y) < 0;
    }

    public void swap(int i, int j) {
        Writes.swap(this.a, i, j, 1, true, false);
    }

    public boolean is_min_level(int index) {
        index = index - this.start + 1;
        return ((32 - Integer.numberOfLeadingZeros(index)) & 1) == 1;
    }

    public void downheap(int i) {
        boolean cf;
        if (this.is_min_level(i)) {
            cf = false;
        } else {
            cf = true;
        }
        int left = 2 * i + 1;
        while (left < this.end) {
            int right = left + 1;
            int nexti = left;
            for (int c : new int[] {right, 2 * left + 1, 2 * left + 2, 2 * right + 1, 2 * right + 2}) {
                if (c >= this.end) {
                    break;
                }
                if (compare(this.a[c], this.a[nexti], cf)) {
                    nexti = c;
                }
            }
            if (nexti <= right) {
                if (compare(this.a[nexti], this.a[i], cf)) {
                    this.swap(nexti, i);
                }
                return;
            } else {
                if (compare(this.a[nexti], this.a[i], cf)) {
                    this.swap(nexti, i);
                    int parent = (nexti - 1) / 2;
                    if (compare(this.a[parent], this.a[nexti], cf)) {
                        this.swap(nexti, parent);
                    }
                } else {
                    return;
                }
            }
            i = nexti;
            left = 2 * i + 1;
        }
    }

    public void heapify() {
        for (int i = (this.end - 1) / 2; i >= this.start; i--) {
            this.downheap(i);
        }
    }

    // public void store_min() {
    //     this.end--;
    //     if (this.end <= this.start) {
    //         return;
    //     }
    //     this.swap(this.start, this.end);
    //     this.downheap(this.start);
    // }

    public void store_max() {
        if (this.end <= this.start + 1) {
            return;
        }
        int imax = this.start + 1;
        if (this.end > imax + 1 && compare(this.a[imax], this.a[imax + 1], false)) {
            imax++;
        }
        this.end--;
        this.swap(imax, this.end);
        if (imax < this.end) {
            this.downheap(imax);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.a = array;
        this.start = 0;
        this.end = currentLength;
        this.heapify();
        for (int i = this.end - 1; i > this.start; i--) {
            this.store_max();
        }
    }
}

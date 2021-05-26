package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class BitonicSortParallel extends Sort {
    private int[] arr;
    private final double DELAY = 1;
    public BitonicSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bitonic (Parallel)");
        this.setRunAllSortsName("Parallel Bitonic Sort");
        this.setRunSortName("Parallel Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }

    private class SortThread extends Thread {
        private int start, stop;
        private boolean ascending;
        SortThread(int start, int stop, boolean ascending) {
            this.start = start;
            this.stop = stop;
            this.ascending = ascending;
        }
        public void run() {
            BitonicSortParallel.this.bitonicSort(this.start, this.stop, this.ascending);
        }
    }

    private class MergeThread extends Thread {
        private int start, mid, stop, gap, flag;
        MergeThread(int start, int mid, int stop, int gap, int flag) {
            this.start = start;
            this.mid = mid;
            this.stop = stop;
            this.gap = gap;
            this.flag = flag;
        }
        public void run() {
            BitonicSortParallel.this.bitonicMerge(this.start, this.mid, this.stop, this.gap, this.flag);
        }
    }

    private void bitonicMerge(int start, int mid, int stop, int gap, int flag) {
        if (stop - start >= 2) {
            for (int i = start; i < mid; i++)
                if (Reads.compareIndices(arr, i, i + gap, DELAY, true) == flag)
                    Writes.swap(arr, i, i + gap, DELAY, true, false);
            int newGap = gap / 2;
                MergeThread left = new MergeThread(start, (mid - start) / 2 + start, mid, newGap, flag);
                MergeThread right = new MergeThread(mid, (stop - mid) / 2 + mid, stop, newGap, flag);
                left.start();
                right.start();
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void bitonicSort(int start, int stop, boolean ascending) {
        int length = stop - start;
        if (length >= 2) {
            int gap = length / 2;
            int mid = gap + start;
                SortThread left = new SortThread(start, mid, !ascending);
                SortThread right = new SortThread(mid, stop, ascending);
                left.start();
                right.start();
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            this.bitonicMerge(start, mid, stop, gap, ascending ? 1 : -1);
        }
    }
    
    @Override
    public void runSort(int[] arr, int length, int buckets) {
        this.arr = arr;
        this.bitonicSort(0, length, true);
    }
}
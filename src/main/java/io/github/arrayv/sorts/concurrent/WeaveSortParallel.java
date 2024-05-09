package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Weave (Parallel)", runName = "Parallel Weave Sorting Network", unreasonableLimit = 4096)
public final class WeaveSortParallel extends Sort {
    private int[] arr;
    private int length;
    private double DELAY = 1;

    public WeaveSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private class SortThread extends Thread {
        private int start, gap;

        SortThread(int start, int gap) {
            this.start = start;
            this.gap = gap;
        }

        @Override
        public void run() {
            WeaveSortParallel.this.wrapper(this.start, this.gap);
        }
    }

    private class CircleThread extends Thread {
        private int start, stop, gap;

        CircleThread(int start, int stop, int gap) {
            this.start = start;
            this.stop = stop;
            this.gap = gap;
        }

        @Override
        public void run() {
            WeaveSortParallel.this.circle(this.start, this.stop, this.gap);
        }
    }

    private void step(int x, int y) {
        if (Reads.compareIndices(arr, x, y, this.DELAY, true) == 1)
            Writes.swap(arr, x, y, this.DELAY, true, false);
    }

    private void circle(int start, int stop, int gap) {
        if ((stop - start) / gap >= 1) {
            int left = start, right = stop;
            while (left < right) {
                this.step(left, right);
                left += gap;
                right -= gap;
            }
            CircleThread leftT = new CircleThread(start, right, gap);
            CircleThread rightT = new CircleThread(left, stop, gap);
            leftT.start();
            rightT.start();
            try {
                leftT.join();
                rightT.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void wrapper(int start, int gap) {
        if (gap < length) {
            SortThread left = new SortThread(start, gap * 2);
            SortThread right = new SortThread(start + gap, gap * 2);
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            this.circle(start, length - gap + start, gap);
        }
    }

    @Override
    public void runSort(int[] arr, int length, int bucketCount) {
        this.arr = arr;
        this.length = length;
        this.wrapper(0, 1);
    }
}

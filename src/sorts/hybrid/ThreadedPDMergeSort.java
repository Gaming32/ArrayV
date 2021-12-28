package sorts.hybrid;

import java.util.concurrent.locks.ReentrantLock;

import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2021 Gaming32

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

public class ThreadedPDMergeSort extends BinaryPDMergeSort {
    final int MAX_THREADS = 24;
    volatile int threadCount;
    volatile ReentrantLock countLock;

    public ThreadedPDMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Threaded Pattern-Defeating Merge");
        this.setRunAllSortsName("Threaded Pattern-Defeating Merge Sort");
        this.setRunSortName("Threaded Pattern-Defeating Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void mergeUp(int[] array, int start, int mid, int end) {
        for (int i = start; i < mid; i++) {
            Highlights.markArray(1, i);
            Writes.write(copied, i, array[i], 1, false, true);
        }

        int bufferPointer = start;
        int left = start;
        int right = mid;

        while (left < right && right < end) {
            Highlights.markArray(2, right);
            if (Reads.compareValues(copied[bufferPointer], array[right]) <= 0)
                Writes.write(array, left++, copied[bufferPointer++], 1, true, false);
            else
                Writes.write(array, left++, array[right++], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, left++, copied[bufferPointer++], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    protected void mergeDown(int[] array, int start, int mid, int end) {
        for (int i = mid; i < end; i++) {
            Highlights.markArray(1, i);
            Writes.write(copied, i, array[i], 1, false, true);
        }

        int bufferPointer = end - 1;
        int left = mid - 1;
        int right = end - 1;

        while (right > left && left >= start) {
            Highlights.markArray(2, left);
            if (Reads.compareValues(copied[bufferPointer], array[left]) >= 0)
                Writes.write(array, right--, copied[bufferPointer--], 1, true, false);
            else
                Writes.write(array, right--, array[left--], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, right--, copied[bufferPointer--], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    protected void merge(int[] array, int start, int mid, int end) {
        countLock.lock();
        if (threadCount < MAX_THREADS) {
            threadCount++;
            new Thread("ThreadedPDMerge-" + threadCount) {
                @Override
                public void run() {
                    ThreadedPDMergeSort.super.merge(array, start, mid, end);
                    countLock.lock();
                    threadCount--;
                    synchronized (countLock) {
                        countLock.notify();
                    }
                    countLock.unlock();
                }
            }.start();
            countLock.unlock();
            return;
        }
        countLock.unlock();
        super.merge(array, start, mid, end);
    }

    public void runSort(int[] array, int length, int bucketCount) {
        threadCount = 0;
        countLock = new ReentrantLock();

        int[] runs = findRuns(array, length - 1);
        copied = Writes.createExternalArray(length);

        // arrayVisualizer.setHeading("PDMerge -- Merging Runs");
        while (runCount > 1) {
            for (int i = 0; i < runCount - 1; i += 2) {
                int end = i + 2 >= runCount ? length : (runs[i + 2]);
                merge(array, runs[i], runs[i + 1], end);
            }
            while (threadCount > 0) {
                synchronized (countLock) {
                    try {
                        countLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 1, j = 2; i < runCount; i++, j+=2, runCount--) {
                Writes.write(runs, i, runs[j], 0.5, true, true);
            }
        }

        // arrayVisualizer.setHeading("Pattern-Defeating Mergesort");

        Writes.deleteExternalArray(runs);
        Writes.deleteExternalArray(copied);
    }
}
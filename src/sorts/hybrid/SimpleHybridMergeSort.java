package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*
 * The Stackless Timsort was made by aphitorite.
 * This variant of Stackless Timsort has removed the galloping mode code and 
 * in the subarrays needs to be merged, used rotation if the last item of
 * the second subarray is smaller than the first item of the first subarray.
 *  
 */

/**
 * @author aphitorite - the creator of Stackless Timsort
 * @author Yuri-chan2007 - who implemented this sorting algorithm from Stackless
 *         Timsort
 * @implNote This sorting algorithm is based on Stackless Timsort, which is
 *           written by aphitorite.
 * 
 */
public final class SimpleHybridMergeSort extends Sort {

    public SimpleHybridMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Simple Hybrid Merge");
        this.setRunAllSortsName("Simple Hybrid Merge Sort");
        this.setRunSortName("Simple Hybrid Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // The next 10 methods are refactored from Stackless Timsort (special thanks to
    // aphitorite)
    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;

        return this.leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);

        if (a > b) {
            int temp = array[a];

            do
                Writes.write(array, a, array[--a], 0.25, true, false);
            while (a > b);

            Writes.write(array, b, temp, 0.25, true, false);
        }
    }

    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

    private void buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;

        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1)
                    i++;
                Writes.reversal(array, j, i - 1, 1, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0)
                    i++;

            if (i < b)
                j = i - (i - j - 1) % mRun - 1;

            while (i - j < mRun && i < b) {
                this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
    }

    protected void mergeFW(int[] array, int[] temp, int start, int mid, int end) {
        for (int i = 0; i < mid - start; i++) {
            Highlights.markArray(1, i + start);
            Writes.write(temp, i, array[i + start], 1, false, true);
        }

        int bufferPointer = 0;
        int left = start;
        int right = mid;

        while (left < right && right < end) {
            Highlights.markArray(2, right);
            if (Reads.compareValues(temp[bufferPointer], array[right]) <= 0)
                Writes.write(array, left++, temp[bufferPointer++], 1, true, false);
            else
                Writes.write(array, left++, array[right++], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, left++, temp[bufferPointer++], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    protected void mergeBW(int[] array, int[] temp, int start, int mid, int end) {
        for (int i = 0; i < end - mid; i++) {
            Highlights.markArray(1, i + mid);
            Writes.write(temp, i, array[i + mid], 1, false, true);
        }

        int bufferPointer = end - mid - 1;
        int left = mid - 1;
        int right = end - 1;

        while (right > left && left >= start) {
            Highlights.markArray(2, left);
            if (Reads.compareValues(temp[bufferPointer], array[left]) >= 0)
                Writes.write(array, right--, temp[bufferPointer--], 1, true, false);
            else
                Writes.write(array, right--, array[left--], 1, true, false);
        }
        Highlights.clearMark(2);

        while (right > left)
            Writes.write(array, right--, temp[bufferPointer--], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    protected void smartMerge(int[] array, int[] tmp, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = this.leftBoundSearch(array, a, m, array[m]);
        b = this.rightBoundSearch(array, m, b, array[m - 1]);
        if (Reads.compareIndices(array, a, b - 1, 0.0, true) > 0)
            rotate(array, a, m, b);
        else if (b - m < m - a)
            this.mergeBW(array, tmp, a, m, b);
        else
            this.mergeFW(array, tmp, a, m, b);

    }

    public void adaptiveMergeSort(int[] array, int start, int end) {
        int[] tmp = Writes.createExternalArray((end - start) / 2);
        int mRun = end - start;
        for (; mRun >= 32; mRun = (mRun + 1) / 2)
            ;

        this.buildRuns(array, start, end, mRun);

        for (int i, j = mRun; j < (end - start); j *= 2) {
            for (i = start; i + 2 * j <= end; i += 2 * j)
                this.smartMerge(array, tmp, i, i + j, i + 2 * j);

            if (i + j < end)
                this.smartMerge(array, tmp, i, i + j, end);
        }
        Writes.deleteExternalArray(tmp);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        adaptiveMergeSort(array, 0, sortLength);

    }

}

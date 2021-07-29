package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/**
 * @author Yuri-chan
 * @author thatsOven
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

    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

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

    // by thatsOven
    public int triSearch(int[] arr, int l, int h, int val, double sleep) {
        int mid = l + ((h - l) / 2);
        Highlights.markArray(0, l);
        Highlights.markArray(1, h);
        Highlights.markArray(2, mid);
        Delays.sleep(sleep);
        if (Reads.compareValues(val, arr[l]) < 0) {
            return l;
        } else {
            if (Reads.compareValues(val, arr[h]) < 0) {
                if (Reads.compareValues(val, arr[mid]) < 0) {
                    return this.triSearch(arr, l + 1, mid - 1, val, sleep);
                } else {
                    return this.triSearch(arr, mid + 1, h - 1, val, sleep);
                }
            } else {
                return h + 1;
            }
        }
    }

    private void standardInsert(int[] array, int start, int end, double compSleep, double writeSleep) {
        int i = start + 1;
        if (Reads.compareIndices(array, i - 1, i++, compSleep, true) == 1) {
            while (i < end && Reads.compareIndices(array, i - 1, i, compSleep, true) == 1)
                i++;
            Writes.reversal(array, start, i - 1, writeSleep, true, false);
        } else
            while (i < end && Reads.compareIndices(array, i - 1, i, compSleep, true) <= 0)
                i++;

        Highlights.clearMark(2);

        while (i < end) {
            int num = array[i];
            int lo = start;

            lo = this.triSearch(array, start, i - 1, num, compSleep);
            Highlights.clearAllMarks();

            int j = i - 1;

            while (j >= lo) {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, lo, num, writeSleep, true, false);

            Highlights.clearAllMarks();
            i++;
        }
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

    protected void mergeSort(int[] array, int[] tmp, int start, int end) {
        if (end - start > 16) {
            int mid = start + (end - start) / 2;
            mergeSort(array, tmp, start, mid);
            mergeSort(array, tmp, mid, end);
            smartMerge(array, tmp, start, mid, end);
        } else {
            standardInsert(array, start, end, 0.25, 0.5);
        }
    }

    public void customSort(int[] array, int start, int end) {
        int[] tmp = Writes.createExternalArray((end - start) / 2);
        mergeSort(array, tmp, start, end);
        Writes.deleteExternalArray(tmp);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] tmp = Writes.createExternalArray(sortLength / 2);
        mergeSort(array, tmp, 0, sortLength);
        Writes.deleteExternalArray(tmp);

    }

}

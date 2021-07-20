package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Lancewer
 * @author Yuri-chan2007
 * @author thatsOven
 *
 */
public final class OptimizedBisurgeSort extends Sort {

    public OptimizedBisurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Bisurge");
        this.setRunAllSortsName("Optimized Bisurge Sort");
        this.setRunSortName("Optimized Bisurgesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

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
        if(Reads.compareIndices(array, i - 1, i++, compSleep, true) == 1) {
            while(i < end && Reads.compareIndices(array, i - 1, i, compSleep, true) == 1) i++;
            Writes.reversal(array, start, i - 1, writeSleep, true, false);
        }
        else while(i < end && Reads.compareIndices(array, i - 1, i, compSleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while(i < end) {
            int num = array[i];
            int lo = start;
            
            lo = this.triSearch(array, start, i-1, num, compSleep);
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

    private void bisurgeInsert(int[] array, int start, int mid, int end, double compSleep, double writeSleep) {
        for (int i = mid; i < end; i++) {
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
        }
    }

    protected void bisurgeSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        if (end - start > 16) {
            int mid = start + (end - start) / 2;
            bisurgeSort(array, mid, end, compSleep, writeSleep);
            bisurgeSort(array, start, mid, compSleep, writeSleep);
            bisurgeInsert(array, start, mid, end, compSleep, writeSleep);
        } else {
            standardInsert(array, start, end, compSleep, writeSleep);
        }
    }

    public void customSort(int[] array, int start, int end, double sleep) {
        bisurgeSort(array, start, end, sleep / 2.0, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        bisurgeSort(array, 0, sortLength, 0.125, 0.25);

    }

}

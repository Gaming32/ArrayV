package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author invented by Lancewer
 * @author implemented in Java by Yuri-chan2007
 *
 */
public final class BisurgeSort extends Sort {

    public BisurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bisurge");
        this.setRunAllSortsName("Bisurge Sort");
        this.setRunSortName("Bisurgesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void bisurgeInsert(int[] array, int start, int mid, int end, double compSleep, double writeSleep) {
        for (int i = mid; i < end; i++) {
            int temp = array[i];
            int a = start, b = i;

            while (a < b) {
                int m = a + ((b - a) / 2); // avoid int overflow!
                Highlights.markArray(1, a);
                Highlights.markArray(2, m);
                Highlights.markArray(3, b);

                Delays.sleep(compSleep);

                if (Reads.compareValues(temp, array[m]) < 0) { // do NOT move equal elements to right of inserted
                                                               // element; this maintains stability!
                    b = m;
                } else {
                    a = m + 1;
                }
            }

            Highlights.clearMark(3);

            // item has to go into position lo

            int j = i - 1;

            while (j >= a) {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, a, temp, writeSleep, true, false);

            Highlights.clearAllMarks();
        }
    }

    private void bisurgeSort(int[] array, int start, int end, double compSleep, double writeSleep) {
        if (end - start < 2)
            return;
        int mid = start + (end - start) / 2;
        bisurgeSort(array, mid, end, compSleep, writeSleep);
        bisurgeSort(array, start, mid, compSleep, writeSleep);
        bisurgeInsert(array, start, mid, end, compSleep, writeSleep);
    }

    public void customSort(int[] array, int start, int end, double sleep) {
        bisurgeSort(array, start, end, sleep / 2.0, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        bisurgeSort(array, 0, sortLength, 0.125, 0.25);

    }

}

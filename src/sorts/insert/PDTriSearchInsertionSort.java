package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Gaming32
 * @author Yuri-chan2007
 * @author thatsOven
 *
 */
public final class PDTriSearchInsertionSort extends Sort {

    public PDTriSearchInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating TriSearch Insertion");
        this.setRunAllSortsName("Pattern-Defeating TriSearch Insertion Sort");
        this.setRunSortName("Pattern-Defeating TriSearch Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    //by thatsOven
    private int triSearch(int[] arr, int l, int h, int val, double sleep) {
        int mid = l + ((h-l) / 2);
        Highlights.markArray(0, l);
        Highlights.markArray(1, h);
        Highlights.markArray(2, mid);
        Delays.sleep(sleep);
        if (Reads.compareValues(val, arr[l]) < 0) {
            return l;
        } else {
            if (Reads.compareValues(val, arr[h]) < 0) {
                if (Reads.compareValues(val, arr[mid]) < 0) {
                    return this.triSearch(arr, l+1, mid-1, val, sleep);
                } else {
                    return this.triSearch(arr, mid+1, h-1, val, sleep);
                }
            } else {
                return h+1;
            }
        }
    }
    
    //pattern-defeating insertion sort (thanks to Gaming32)
    //here I used trisearch (by thatsOven)
    private void insertionSort(int[] array, int start, int end, double compSleep, double writeSleep) {
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
    
    public void customSort(int[] array, int start, int end, double sleep) {
        insertionSort(array, start, end, sleep/2.0, sleep);
    }


    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        insertionSort(array, 0, sortLength, 40.0, 1.0);

    }

}

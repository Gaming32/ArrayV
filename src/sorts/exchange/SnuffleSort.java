package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.lang.Math;

public class SnuffleSort extends Sort {
    private static double DELAY = 0.005;
    public SnuffleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Snuffle");
        this.setRunAllSortsName("Snuffle Sort");
        this.setRunSortName("Snuffle Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(65);
        this.setBogoSort(false);
    }
    
	private void snuffleSort(int[] arr, int start, int stop) {
        if (stop - start + 1 >= 2) {
    	    Highlights.markArray(0, start);
            Highlights.markArray(1, stop);
            Delays.sleep(DELAY);
            if (Reads.compareValues(arr[start], arr[stop]) == 1)
                Writes.swap(arr, start, stop, DELAY, false, false);
            if (stop - start + 1 >= 3) {
                int mid = (stop - start) / 2 + start;
                for (int i = 0; i < (int) Math.ceil((stop - start + 1) / 2); i++) {
                    this.snuffleSort(arr, start, mid);
                    this.snuffleSort(arr, mid, stop);
                }
            }
        }
	}

    @Override
    public void runSort(int[] array, int length, int buckets) {
        this.snuffleSort(array, 0, length - 1);
    }
}
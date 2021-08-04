/**
 * 
 */
package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author thatsOven - for the TriSearch algorithm
 * @author mingyue12 - for the rest of this sorting algorithm
 *
 */
public final class TriSearchGnomeSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public TriSearchGnomeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
        this.setSortListName("TriSearch Gnome");
        this.setRunAllSortsName("TriSearch Gnome Sort");
        this.setRunSortName("TriSearch Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}

    public int triSearch(int[] arr, int l, int h, int val, double sleep) {
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
    public void triGnomeSort(int[] array, int start, int end, double compSleep, double writeSleep) {
    	for (int i = start+1; i < end; i++) {
            int num = array[i];
            int lo = start;
            
            lo = this.triSearch(array, start, i-1, num, compSleep);
            Highlights.clearAllMarks();
            int j = i ;
            while(j>lo) {
            	Writes.swap(array, j, j - 1, writeSleep, true, false);
            	j--;
            }
            Highlights.clearAllMarks();

    	}
    }

	@Override
	public void runSort(int[] array, int length, int bucketCount){
		triGnomeSort(array, 0, length, 40, 1);

	}

}

package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Code refactored from Python: http://wiki.c2.com/?SlowSort

final public class SlowSort extends Sort {
    public SlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Slow");
        this.setRunAllSortsName("Slow Sort");
        this.setRunSortName("Slowsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }
    
	private void slowSort(int[] A, int i, int j) {	
	    Delays.sleep(1);
	    
	    if (i >= j) {
			return;
		}
		
	    int m = i + ((j - i) / 2);
	
	    this.slowSort(A, i, m);
	    this.slowSort(A, m + 1, j);
	
	    if (Reads.compareValues(A[m], A[j]) == 1) {
	        Writes.swap(A, m, j, 1, true, false);
	    }
	    
	    Highlights.markArray(1, j);
	    Highlights.markArray(2, m);
	    
	    this.slowSort(A, i, j - 1);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.slowSort(array, 0, currentLength - 1);
    }
}
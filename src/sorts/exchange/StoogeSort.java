package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from: https://en.wikipedia.org/wiki/Stooge_sort
final public class StoogeSort extends Sort {
    public StoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stooge");
        this.setRunAllSortsName("Stooge Sort");
        this.setRunSortName("Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    
	private void stoogeSort(int[] A, int i, int j) {
	    if (Reads.compareValues(A[i], A[j]) == 1) {
	        Writes.swap(A, i, j, 0.005, true, false);
	    }
	    
	    Delays.sleep(0.0025);
	    
	    Highlights.markArray(1, i);
        Highlights.markArray(2, j);
	    
        if (j - i + 1 >= 3) {
	        int t = (j - i + 1) / 3;
	        
	        Highlights.markArray(3, j - t);
	        Highlights.markArray(4, i + t);
	
	        this.stoogeSort(A, i, j-t);
	        this.stoogeSort(A, i+t, j);
	        this.stoogeSort(A, i, j-t);
	    }
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1);
    }
}
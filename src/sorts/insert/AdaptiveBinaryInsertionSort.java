/**
 * 
 */
package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author PiotrGrochowski
 *
 */
public final class AdaptiveBinaryInsertionSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public AdaptiveBinaryInsertionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
        this.setSortListName("Adaptive Binary Insert");
        this.setRunAllSortsName("Adaptive Binary Insertion Sort");
        this.setRunSortName("Adaptive Binary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}
	
	protected void abinaryinsert(int[] array, int start, int end, double sleep) {
	    int count = 0;
	    for (int i = start + 1; i < end; i++) {
	        int num = array[i];
	        int v = (2*count / (i - start)) + 1; //I'VE SOLVED IT!!
	        int lo = Math.max(i - v, start), hi = i;
	        while ((lo >= start) && (Reads.compareValues(array[lo], num) == 1)){
	            lo -= v;
	            hi -= v;
	        }
	        lo++;
	        if (lo < start){
	            lo = start;
	        }
	        while (lo < hi) {
	            int mid = lo + ((hi - lo) / 2); // avoid int overflow!
	            Highlights.markArray(2, mid);
	            
	            Delays.sleep(sleep);
	            
	            if (Reads.compareValues(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element; this maintains stability!
	                hi = mid;
	            }
	            else {
	                lo = mid + 1;
	            }
	        }

	        // item has to go into position lo
	        count += (i - lo);

	        int j = i - 1;
	        
	        if (j >= lo){
	            while (j >= lo)
	            {
	                Writes.write(array, j + 1, array[j], sleep, true, false);
	                j--;
	            }
	            Writes.write(array, lo, num, sleep, true, false);
	        }
	        
	        Highlights.clearAllMarks();
	    }
	}
	
    public void customSort(int[] array, int start, int end) {
        this.abinaryinsert(array, start, end, 1);
    }

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		this.abinaryinsert(array, 0, sortLength, 0.0875);
	}

}

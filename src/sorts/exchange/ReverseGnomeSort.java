/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * This is the reversed variant of Optimized Gnomesort.
 * @author mingyue12
 *
 */
public final class ReverseGnomeSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public ReverseGnomeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		// TODO Auto-generated constructor stub
		this.setSortListName("Reverse Gnome");
		this.setRunAllSortsName("Reverse Gnome Sort");
		this.setRunSortName("Reverse Gnomesort");
		this.setCategory("Exchange Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);

	}
    private void reverseGnomeSort(int[] array, int lowerBound, int upperBound, double sleep) {
        int pos = lowerBound;
        
        while(pos < upperBound && Reads.compareValues(array[pos], array[pos + 1]) == 1) {
            Writes.swap(array, pos, pos + 1, sleep, true, false);
            pos++;
        }
    }
    public void customSort(int[] array, int low, int high, double sleep) {
        for(int i = high; i >= low; i--) {
            reverseGnomeSort(array, i, high, sleep);
        }
    }


	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		// TODO Auto-generated method stub
        for(int i = sortLength - 1; i >= 0; i--) {
            reverseGnomeSort(array, i, sortLength - 1, 0.05);
        }    

	}

}

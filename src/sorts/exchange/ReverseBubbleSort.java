/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author mingyue12
 *
 */
public final class ReverseBubbleSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public ReverseBubbleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		// TODO Auto-generated constructor stub
		this.setSortListName("Reverse Bubble");
        this.setRunAllSortsName("Reverse Bubble Sort");
        this.setRunSortName("Reverse Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		// TODO Auto-generated method stub
		boolean sorted = false;

        while(!sorted) {
            sorted = true;
            for(int i = sortLength - 1; i > 0; i--) {             
                if(Reads.compareValues(array[i], array[i - 1]) == -1){
                    Writes.swap(array, i, i - 1, 0.075, true, false);
                    sorted = false;
                }

                Highlights.markArray(1, i);
                Highlights.markArray(2, i - 1);
                Delays.sleep(0.05);
            }
        } 

	}

}

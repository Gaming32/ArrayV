/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author McDude_73
 * @author EilrahcF
 * @implNote This sorting algorithm is bad for arrays, but good for linked
 *           lists.
 *
 */
public final class ReverseGrateSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public ReverseGrateSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Reverse Grate");
		setRunAllSortsName("Reverse Grate Sort");
		setRunSortName("Reverse Gratesort");
		setCategory("Exchange Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(512);
		setBogoSort(false);

	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i = 0; i < currentLength - 1; i++) {
				for (int j = i + 1; j < currentLength; j++) {
					this.Highlights.markArray(1, i);
					this.Highlights.markArray(2, j);
					this.Delays.sleep(0.25D);
					if (this.Reads.compareValues(array[i], array[j]) > 0) {
						sorted = false;
						this.Writes.swap(array, i, j, 0.1D, true, false);
						break;
					}
				}
			}
		}

	}

}

/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author McDude_73
 * @author aphitorite
 * @author EilrahcF
 *
 */
public final class WiggleSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public WiggleSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Wiggle");
		setRunAllSortsName("Wiggle Sort");
		setRunSortName("Wigglesort");
		setCategory("Exchange Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}

	private void wiggleSort(int[] array, int arrLen, int start, int end) {
		if (end - start < 2)
			return;
		int leftPoint = start;
		int rightPoint = end;

		int midPoint = (leftPoint + rightPoint) / 2;

		boolean startLeft = true;
		int j = midPoint;

		for (int i = leftPoint; i < midPoint; i++) {
			for (int k = midPoint; k < end; k++) {
				this.Highlights.markArray(1, i);
				this.Highlights.markArray(2, j);
				if (this.Reads.compareValues(array[i], array[j]) >= 0) {
					this.Writes.swap(array, i, j, 1.0D, true, false);
				} else {
					this.Delays.sleep(0.025D);
				}

				if (startLeft) {
					j++;
				} else {
					j--;
				}

			}
			if (startLeft) {
				j--;
				startLeft = false;
			} else {

				j++;
				startLeft = true;
			}
		}

		wiggleSort(array, arrLen, start, midPoint);
		wiggleSort(array, arrLen, midPoint, end);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) throws Exception {
		wiggleSort(array, length, 0, length);

	}

}

/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 * @author EilrahcF
 *
 */
public final class QuadStoogeSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public QuadStoogeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Quad Stooge");
		setRunAllSortsName("Quad Stooge Sort");
		setRunSortName("Quad Stoogesort");
		setCategory("Impractical Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(2048);
		setBogoSort(false);

	}

	private void quadStooge(int[] array, int pos, int len) {
		if (len >= 2 && this.Reads.compareValues(array[pos], array[pos + len - 1]) == 1) {
			this.Writes.swap(array, pos, pos + len - 1, 1.0D, true, false);
		}
		if (len <= 2) {
			return;
		}
		this.Delays.sleep(0.5D);

		this.Highlights.markArray(1, pos);
		this.Highlights.markArray(2, pos + len - 1);

		int len1 = len / 2;
		int len2 = (len + 1) / 2;
		int len3 = (len1 + 1) / 2 + (len2 + 1) / 2;

		quadStooge(array, pos, len1);
		quadStooge(array, pos + len1, len2);
		quadStooge(array, pos + len1 / 2, len3);
		quadStooge(array, pos + len1, len2);
		quadStooge(array, pos, len1);
		if (len > 3) {
			quadStooge(array, pos + len1 / 2, len3);
		}
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		quadStooge(array, 0, sortLength);

	}

}

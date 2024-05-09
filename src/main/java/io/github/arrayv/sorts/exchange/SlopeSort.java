/**
 *
 */
package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author McDude_73
 * @author EilrahcF
 *
 */
@SortMeta(name = "Slope")
public final class SlopeSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public SlopeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		for (int i = 1, j = 1; i < length; i++, j++) {
			for (int k = i - 1; k >= 0; k--, i--) {
				if (this.Reads.compareIndices(array, i, k, 0.04D, true) < 0) {
					this.Writes.swap(array, i, k, 0.02D, true, false);
				}
			}
			i = j;
		}

	}

}

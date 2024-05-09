/**
 *
 */
package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author _fluffyy
 * @author thatsOven
 *
 */
@SortMeta(name = "Buffered Stooge")
public final class BufferedStoogeSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public BufferedStoogeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int compare(int[] arr, int x, int y) {
		return this.Reads.compareIndices(arr, x, y, 0, true);
	}

	public void wrapper(int[] arr, int start, int stop) {
		if (stop - start > 1) {
			if (stop - start == 2 && compare(arr, start, stop - 1) == 1) {
				this.Writes.swap(arr, start, stop - 1, 1.0D, true, false);
			}
			if (stop - start > 2) {
				int third = (int) Math.ceil((stop - start) / 3.0D) + start;
				int twoThird = (int) Math.ceil((stop - start) / 3.0D * 2.0D) + start;
				if (twoThird - third < third) {
					twoThird--;
				}
				if ((stop - start - 2) % 3 == 0) {
					twoThird--;
				}
				wrapper(arr, third, twoThird);
				wrapper(arr, twoThird, stop);
				int left = third;
				int right = twoThird;
				int bufferStart = start;
				while (left < twoThird && right < stop) {
					if (compare(arr, left, right) == 1) {
						this.Writes.swap(arr, bufferStart, right, 1.0D, true, false);
						right++;
					} else {
						this.Writes.swap(arr, bufferStart, left, 1.0D, true, false);
						left++;
					}
					bufferStart++;
				}
				while (right < stop) {
					this.Writes.swap(arr, bufferStart, right, 1.0D, true, false);
					right++;
					bufferStart++;
				}
				wrapper(arr, twoThird, stop);
				left = twoThird - 1;
				right = stop - 1;
				while (right > left && left >= start) {
					if (compare(arr, left, right) == 1) {
						for (int i = left; i < right; i++) {
							this.Writes.swap(arr, i, i + 1, 0.3D, true, false);
						}
						left--;
					}
					right--;
				}
			}
		}
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		wrapper(array, 0, sortLength);
	}

}

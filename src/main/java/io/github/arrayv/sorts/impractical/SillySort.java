package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

// Written by Tom Duff, and found here: http://home.tiac.net/~cri_d/cri/2001/badsort.html
// from https://stackoverflow.com/questions/2609857/are-there-any-worse-sorting-algorithms-than-bogosort-a-k-a-monkey-sort/
@SortMeta(name = "Silly", slowSort = true, unreasonableLimit = 150)
public final class SillySort extends Sort {
	public SillySort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private void sillySort(int[] array, int i, int j) {
		int m;

		if (i < j) {
			/* find the middle of the array */
			m = i + ((j - i) / 2);

			/*
			 * use this function (recursively) to find put the minimum elements of
			 * each half into the first elements of each half
			 */
			this.sillySort(array, i, m);
			this.sillySort(array, m + 1, j);

			/*
			 * Choose the smallest element of the two halves, and put that element in
			 * the first position
			 */
			if (Reads.compareIndices(array, i, m + 1, 1, true) >= 0) {
				Writes.swap(array, i, m + 1, 0, true, false);
			}

			this.sillySort(array, i + 1, j);
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.sillySort(array, 0, currentLength - 1);
	}
}

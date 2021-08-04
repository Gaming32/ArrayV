/**
 * 
 */
package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.InsertionSort;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author MP
 * @author McDude_73
 * @author EilrahcF
 *
 */
public final class GambitInsertionSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public GambitInsertionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Gambit Insertion");
		setRunAllSortsName("Gambit Insertion Sort");
		setRunSortName("Gambit Insertsort");
		setCategory("Insertion Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}

	private int binSearch(int[] array, int begin, int end, int target) {
		while (true) {
			int delta = end - begin;
			if (delta <= 0)
				break;
			int p = begin + delta / 2;
			if (this.Reads.compareIndices(array, p, target, 0.5D, true) == 0)
				return p;

			if (this.Reads.compareIndices(array, p, target, 0.5D, true) > 0) {
				end = p;
				continue;
			}
			begin = p + 1;
		}
		return end;
	}

	private void binInsert(int[] array, int len, int start, int end) {
		int offset = 1;
		for (; offset * offset < len; offset *= 2)
			;

		for (int bStart = 0, bEnd = end, i = start + offset; i < end; i++) {
			int target = binSearch(array, bStart, bEnd, i);

			int tmp = array[i];
			int j = i - 1;
			while (j >= target && array[j] > tmp) {
				this.Writes.write(array, j + 1, array[j], 0.125D, true, false);
				j--;
			}
			array[j + 1] = tmp;
		}
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		binInsert(array, sortLength, 0, sortLength);
		Highlights.clearAllMarks();
		InsertionSort ins = new InsertionSort(arrayVisualizer);
		ins.customInsertSort(array, 0, sortLength, 0.1D, true);

	}

}

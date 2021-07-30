package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OptimizedGrateSort extends Sort {
	public OptimizedGrateSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Optimized Grate");
		setRunAllSortsName("PCBoy's Optimized Grate Sort");
		setRunSortName("Optimized Gratesort");
		setCategory("Exchange Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(true);
		setUnreasonableLimit(1024);
		setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) throws Exception {
		int bound = currentLength - 1;
		int left = 0;
		int right = currentLength - 1;
		int firstswap = 0;
		int lastswap = 0;
		int testi = currentLength - 1;
		boolean sorted = false;
		boolean higherval = false;
		while (!sorted) {
			if (!sorted) {
				while (!higherval) {
					if (testi < left) {
						bound--;
						testi = right;
						if (bound < right) {
							higherval = true;
						}
					} else {
						Highlights.markArray(1, testi);
						Highlights.markArray(2, bound);
						Delays.sleep(0.125);
						if (Reads.compareValues(array[testi], array[bound]) > 0) {
							higherval = true;
						} else {
							testi--;
						}
					}
				}
			}
			sorted = true;
			for (int i = left; i < right; i++) {
				for (int j = bound; j > i; j--) {
					Highlights.markArray(1, i);
					Highlights.markArray(2, j);
					Delays.sleep(0.125);
					if (Reads.compareValues(array[i], array[j]) > 0) {
						if (sorted) {
							firstswap = i;
						} else {
							lastswap = i;
						}
						sorted = false;
						Writes.swap(array, i, j, 0.125, true, false);
						break;
					}
				}
			}
			bound--;
			testi = right;
			higherval = false;
			left = firstswap;
			right = lastswap + 1;
		}
	}
}
package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.Sort;
/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class NaturalSort extends Sort {
	public NaturalSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		this.setSortListName("Natural");
		this.setRunAllSortsName("Natural Sort");
		this.setRunSortName("Naturalsort");
		this.setCategory("Exchange Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		int i = 1;
		int verifyi = 0;
		boolean anyswaps = true;
		boolean verifypass = false;
		while (!verifypass) {
			i = verifyi + 1;
			anyswaps = true;
			while (i + 1 <= currentLength && anyswaps) {
				Highlights.markArray(1, i - 1);
				Highlights.markArray(2, i);
				Delays.sleep(0.05);
				if (Reads.compareValues(array[i - 1], array[i]) > 0) {
					Writes.swap(array, i - 1, i, 0.05, true, false);
				} else {
					anyswaps = false;
				}
				i++;
			}
			if (verifyi > 1) {			
				verifyi--;
			} else {
				verifyi = 1;
			}
			verifypass = true;
			while (verifyi != currentLength && verifypass) {
				Highlights.markArray(1, verifyi - 1);
				Highlights.markArray(2, verifyi);
				Delays.sleep(0.05);
				if (Reads.compareValues(array[verifyi - 1], array[verifyi]) <= 0) {
					verifyi++;
				} else {
					verifypass = false;
					Writes.swap(array, verifyi - 1, verifyi, 0.05, true, false);
				}
			}
		}
	}
}
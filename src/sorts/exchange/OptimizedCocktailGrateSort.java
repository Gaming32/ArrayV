package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OptimizedCocktailGrateSort extends Sort {
	public OptimizedCocktailGrateSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Optimized Cocktail Grate");
		setRunAllSortsName("Optimized Cocktail Grate Sort");
		setRunSortName("Optimized Cocktail Gratesort");
		setCategory("Exchange Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(true);
		setUnreasonableLimit(2048);
		setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) throws Exception {
		int Abound = currentLength - 1;
		int left = 0;
		int Aright = currentLength - 1;
		int Afirstswap = 0;
		int Alastswap = 0;
		int Atesti = currentLength - 1;
		int Bbound = currentLength - 1;
		int Bfirstswap = 0;
		int Blastswap = 0;
		boolean sorted = false;
		boolean Ahigherval = false;
		while (!sorted) {
			if (!sorted) {
				while (!Ahigherval) {
					if (Atesti < left) {
						Abound--;
						Atesti = Aright;
						if (Abound < Aright) {
							Ahigherval = true;
						}
					} else {
						Highlights.markArray(1, Atesti);
						Highlights.markArray(2, Abound);
						Delays.sleep(0.125);
						if (Reads.compareValues(array[Atesti], array[Abound]) > 0) {
							Ahigherval = true;
						} else {
							Atesti--;
						}
					}
				}
			}
			sorted = true;
			for (int i = left; i < Aright; i++) {
				for (int j = Abound; j > i; j--) {
					Highlights.markArray(1, i);
					Highlights.markArray(2, j);
					Delays.sleep(0.125);
					if (Reads.compareValues(array[i], array[j]) > 0) {
						if (sorted) {
							Afirstswap = i;
						}
						Alastswap = i;
						sorted = false;
						Writes.swap(array, i, j, 0.125, true, false);
						break;
					}
				}
			}
			Bbound--;
			Atesti = Aright;
			Ahigherval = false;
			left = Afirstswap;
			Aright = Alastswap + 1;
			if (sorted) {
				break;
			}
			sorted = true;
			for (int i = left; i < Bbound; i++) {
				for (int j = i + 1; j <= Bbound; j++) {
					Highlights.markArray(1, i);
					Highlights.markArray(2, j);
					Delays.sleep(0.125);
					if (Reads.compareValues(array[i], array[j]) > 0) {
						if (sorted) {
							Bfirstswap = i;
						}
						Blastswap = i;
						sorted = false;
						Writes.swap(array, i, j, 0.125, true, false);
						break;
					}
				}
			}
			Bbound = Blastswap;
			left = Bfirstswap;
			Abound = Bbound;
		}
	}
}
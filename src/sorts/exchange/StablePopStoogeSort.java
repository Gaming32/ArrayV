package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * The Pop Stooge Sort was made by Lancewer.
 * This variant used Stable Stooge Sort as it's underlying sort
 * (special thanks to fungamer2).
 *  
 */

/**
 * @author Yuri-chan2007
 *
 */
public final class StablePopStoogeSort extends Sort {

    public StablePopStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Pop Stooge");
        this.setRunAllSortsName("Stable Pop Stooge Sort");
        this.setRunSortName("Stable Pop Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }
    
    private void stoogeSort(int[] array, int start, int end, boolean fw) {
        int cmp = fw ? 1 : -1;
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) == cmp) {
                Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stoogeSort(array, start, end - third, fw);
            stoogeSort(array, start + third, end, fw);
            stoogeSort(array, start, end - third, fw);
        }
    }
    
    private void popSort(int[] array, int start, int end) {
        stoogeSort(array, start, start + ((end - start) / 4) - 1, false);
        stoogeSort(array, start + ((end - start) / 4), start + ((end - start) / 2) - 1, true);
        stoogeSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4) - 1, false);
        stoogeSort(array, start + (3 * (end - start) / 4), end - 1, true);
        stoogeSort(array, start, start + ((end - start) / 2) - 1, false);
        stoogeSort(array, start + ((end - start) / 2), end - 1, true);
        stoogeSort(array, start, end - 1, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        popSort(array, 0, sortLength);

    }

}

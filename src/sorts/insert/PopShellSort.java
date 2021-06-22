package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.ShellSorting;

/**
 * @author Yuri-chan2007
 *
 */
public final class PopShellSort extends ShellSorting {

    public PopShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop Shell");
        this.setRunAllSortsName("Pop Shell Sort");
        this.setRunSortName("Pop Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    /**
     * Sorts the range {@code [start, end)} of {@code array} using Shellsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     * @param fw    whether to sort ascending
     */
    public void shellSort(int[] array, int start, int end, boolean fw) {
        int[] gaps = this.ExtendedCiuraGaps;
        int cmp = fw ? 1 : -1;
        for (int k = 0; k < gaps.length; k++) {
            if(gaps[k] < end-start) {
                int h = gaps[k];
                for (int i = start + h; i < end; i++) {
                    int v = array[i];
                    int j = i;

                    Highlights.markArray(1, j);
                    Highlights.markArray(2, j - h);

                    while (j >= start + h && Reads.compareValues(array[j - h], v) == cmp)
                    {
                        Writes.write(array, j, array[j - h], 1.0, false, false);
                        j -= h;
                        
                        Highlights.markArray(1, j);
                        
                        if(j - h >= start) {
                            Highlights.markArray(2, j - h);
                        }
                        else {
                            Highlights.clearMark(2);
                        }
                    }
                    Writes.write(array, j, v, 1.0, true, false);
                }
            }
        }
    }
    
    /**
     * Sorts the range {@code [start, end)} of {@code array} using Pop Shellsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     */
    public void popSort(int[] array, int start, int end) {
        shellSort(array, start, start + ((end - start) / 4), false);
        shellSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        shellSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        shellSort(array, start + (3 * (end - start) / 4), end, true);
        shellSort(array, start, start + ((end - start) / 2), false);
        shellSort(array, start + ((end - start) / 2), end, true);
        shellSort(array, start, end, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}

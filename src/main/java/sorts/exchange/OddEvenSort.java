package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * This version of Odd-Even Sort was taken from here, written by Rachit Belwariar:
 * https://www.geeksforgeeks.org/odd-even-sort-brick-sort/
 */

final public class OddEvenSort extends Sort {
    public OddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Odd-Even");
        this.setRunAllSortsName("Odd-Even Sort");
        this.setRunSortName("Odd-Even Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean sorted = false;
        
        while (!sorted) {
            sorted = true;
    
            for (int i = 1; i < length - 1; i += 2) {
                if(Reads.compareValues(array[i], array[i + 1]) == 1) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    sorted = false;
                }
                
                Highlights.markArray(1, i);
                Delays.sleep(0.025);
            }
    
            for (int i = 0; i < length - 1; i += 2) {
                if(Reads.compareValues(array[i], array[i + 1]) == 1) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    sorted = false;
                }
                
                Highlights.markArray(2, i);
                Delays.sleep(0.025);
            }
        }
    }
}
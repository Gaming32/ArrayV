package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class LLQuickSort extends Sort {
    public LLQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Left/Left Quick");
        this.setRunAllSortsName("Quick Sort, Left/Left Pointers");
        this.setRunSortName("Left/Left Quicksort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int i = lo;
        
        for(int j = lo; j < hi; j++) {
            Highlights.markArray(1, j);
            if(Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                i++;
            }
            Delays.sleep(1);
        }
        Writes.swap(array, i, hi, 1, true, false);
        return i;
    }
    
    private void quickSort(int[] array, int lo, int hi) {
        if(lo < hi) {
            int p = this.partition(array, lo, hi);
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p + 1, hi);
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}
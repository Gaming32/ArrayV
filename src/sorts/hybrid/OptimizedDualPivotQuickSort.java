package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

// code retrieved from https://codeblab.com/wp-content/uploads/2009/09/DualPivotQuicksort.pdf
// written by Vladimir Yaroslavskiy

final public class OptimizedDualPivotQuickSort extends Sort {
    private InsertionSort insertSorter;
    
    public OptimizedDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Opti. Dual-Pivot Quick");
        //this.setRunAllID("Optimized Dual-Pivot Quick Sort");
        this.setRunAllSortsName("Optimized Dual-Pivot Quick Sort [Arrays.sort]");
        this.setRunSortName("Optimized Dual-Pivot Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void dualPivot(int[] array, int left, int right, int divisor) {
        int length = right - left;
        
        // insertion sort for tiny array
        if(length < 27) {
            Highlights.clearMark(2);
            insertSorter.customInsertSort(array, left, right + 1, 0.333, false);
            return;
        }
        
        int third = length / divisor;
        
        // "medians"
        int med1 = left  + third;
        int med2 = right - third;
        
        if(med1 <= left) {
            med1 = left + 1;
        }
        if(med2 >= right) {
            med2 = right - 1;
        }
        if(Reads.compareValues(array[med1], array[med2]) == -1) {
            Writes.swap(array, med1, left,  1, true, false);
            Writes.swap(array, med2, right, 1, true, false);
        }
        else {
            Writes.swap(array, med1, right, 1, true, false);
            Writes.swap(array, med2, left,  1, true, false);
        }
        
        // pivots
        int pivot1 = array[left];
        int pivot2 = array[right];
        
        // pointers
        int less  = left  + 1;
        int great = right - 1;
        
        // sorting
        for(int k = less; k <= great; k++) {
            if(Reads.compareValues(array[k], pivot1) == -1) {
                Writes.swap(array, k, less++, 1, true, false);
            }
            else if(Reads.compareValues(array[k], pivot2) == 1) {
                while(k < great && Reads.compareValues(array[great], pivot2) == 1) {
                    great--;
                    Highlights.markArray(3, great);
                    Delays.sleep(1);
                }
                Writes.swap(array, k, great--, 1, true, false);
                Highlights.clearMark(3);
                
                if(Reads.compareValues(array[k], pivot1) == -1) {
                    Writes.swap(array, k, less++, 1, true, false);
                }
            }
        }
        
        // swaps
        int dist = great - less;
        
        if(dist < 13) {
            divisor++;
        }
        Writes.swap(array, less  - 1, left,  1, true, false);
        Writes.swap(array, great + 1, right, 1, true, false);
        
        // subarrays
        this.dualPivot(array, left,   less - 2, divisor);
        this.dualPivot(array, great + 2, right, divisor);
        
        // equal elements
        if(dist > length - 13 && pivot1 != pivot2) {
            for(int k = less; k <= great; k++) {
                if(Reads.compareValues(array[k], pivot1) == 0) {
                    Writes.swap(array, k, less++, 1, true, false);
                }
                else if(Reads.compareValues(array[k], pivot2) == 0) {
                    Writes.swap(array, k, great--, 1, true, false);
                    
                    if(Reads.compareValues(array[k], pivot1) == 0) {
                        Writes.swap(array, k, less++, 1, true, false);
                    }
                }
            }
        }
        
        // subarray
        if(pivot1 < pivot2) {
            this.dualPivot(array, less, great, divisor);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.insertSorter = new InsertionSort(this.arrayVisualizer);
        this.dualPivot(array, 0, currentLength - 1, 3);
    }
}
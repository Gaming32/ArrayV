package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BinaryGnomeSort extends Sort {
    public BinaryGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Binary Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort + Binary Search");
        this.setRunSortName("Optimized Gnomesort + Binary Search");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = 1; i < sortLength; i++) {
            int num = array[i];

            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2);
                
                Highlights.markArray(1, lo);
                Highlights.markArray(3, mid);
                Highlights.markArray(2, hi);
                
                Delays.sleep(1);
                
                if (Reads.compareValues(num, array[mid]) < 0) { // do NOT shift equal elements past each other; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            // item has to go into position lo

            Highlights.clearMark(1);
            Highlights.clearMark(2);
            
            int j = i;
            while (j > lo) {   
                Writes.swap(array, j, j - 1, 0.05, true, false);
                j--;
            }
        }         
    }
}
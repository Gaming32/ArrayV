package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class GnomeSort extends Sort {
    public GnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Gnome");
        this.setRunAllSortsName("Gnome Sort");
        this.setRunSortName("Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Code retrieved from http://www.algostructure.com/sorting/gnomesort.php
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length;)
        {
            if (Reads.compareValues(array[i], array[i-1]) >= 0)
            {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.04);
            }
            else
            {
                Writes.swap(array, i, i - 1, 0.02, true, false);
                
                Highlights.clearMark(2);
                
                if (i > 1) {
                    i--;
                    Highlights.markArray(1, i);
                    Delays.sleep(0.02);
                }
            }
        }
    }
}
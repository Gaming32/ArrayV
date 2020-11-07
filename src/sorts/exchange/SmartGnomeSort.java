package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class SmartGnomeSort extends Sort {
    public SmartGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Smart Gnome");
        this.setRunAllSortsName("Optimized Gnome Sort");
        this.setRunSortName("Optimized Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Taken from https://en.wikipedia.org/wiki/Gnome_sort
    private void smartGnomeSort(int[] array, int lowerBound, int upperBound, double sleep) {
        int pos = upperBound;
        
        while(pos > lowerBound && Reads.compareValues(array[pos - 1], array[pos]) == 1) {
            Writes.swap(array, pos - 1, pos, sleep, true, false);
            pos--;
        }
    }

    public void customSort(int[] array, int low, int high, double sleep) {
        for(int i = low + 1; i < high; i++) {
            smartGnomeSort(array, low, i, sleep);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i = 1; i < length; i++) {
            smartGnomeSort(array, 0, i, 0.05);
        }    
    }
}
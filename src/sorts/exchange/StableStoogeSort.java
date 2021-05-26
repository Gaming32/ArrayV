package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class StableStoogeSort extends Sort {
    public StableStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stable Stooge");
        this.setRunAllSortsName("Stable Stooge Sort");
        this.setRunSortName("Stable Stoogesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    
    private void stableStooge(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) == 1) {
    	        Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stableStooge(array, start, end - third);
            stableStooge(array, start + third, end);
            stableStooge(array, start, end - third);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        stableStooge(array, 0, currentLength - 1);
    }
}
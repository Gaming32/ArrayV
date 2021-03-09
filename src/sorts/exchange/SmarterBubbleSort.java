package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class SmarterBubbleSort extends Sort {
    public SmarterBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Smarter Bubble");
        this.setRunAllSortsName("More Optimized Bubble Sort");
        this.setRunSortName("More Optimized Bubblesort");
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
    	int consecSorted;
        for(int i = length - 1; i > 0; i -= consecSorted) {
            consecSorted = 1;
            for(int j = 0; j < i; j++) {
                if(Reads.compareIndices(array, j, j + 1, 0.025, true) > 0){
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                    consecSorted = 1;
                } else consecSorted++;
            }
        }
    }
}
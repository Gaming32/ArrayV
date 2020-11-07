package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class LessBogoSort extends BogoSorting {
    public LessBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Less Bogo");
        this.setRunAllSortsName("Less Bogo Sort");
        this.setRunSortName("Less Bogosort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false); //Comparisons are not used to swap elements
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(true);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int iterator = 0;
        
        while(iterator != sortLength) {
            while(!this.isMinSorted(array, sortLength, iterator)) {
                this.bogoSwap(array, sortLength, iterator);
            }
            //Highlights.markArray(1, iterator);
            iterator++;
        }
    }
}
package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class SliceBogoSort extends BogoSorting {
    public SliceBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Slice Bogo");
        this.setRunAllSortsName("Slice Bogo Sort");
        this.setRunSortName("Slice Bogosort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(false); //Comparisons ARE used to swap elements
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        while(!bogoIsSorted(array, currentLen)) {
            int index1 = (int) (Math.random() * currentLen),
                index2 = (int) (Math.random() * currentLen);
            
            Highlights.markArray(1, index1);
            Highlights.markArray(2, index2);
            
            if(index1 < index2) {
                bogoSwap(array, index2 + 1, index1);
            }
            else {
                bogoSwap(array, index1 + 1, index2);
            }
        }
    }
}
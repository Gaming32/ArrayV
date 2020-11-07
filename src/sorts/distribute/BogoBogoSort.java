package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class BogoBogoSort extends BogoSorting {
    public BogoBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Bogo Bogo");
        this.setRunAllSortsName("Bogo Bogo Sort");
        this.setRunSortName("Bogobogosort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false); //Comparisons are not used to swap elements
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(6);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int bogoLength = 2;
        boolean arrayNotSorted = true;
        
        while(arrayNotSorted) {
            if(bogoIsSorted(array, bogoLength)) {
                if(bogoLength == sortLength) {
                    arrayNotSorted = false;
                }
                else {
                    bogoLength++;
                }
            }
            else {
                bogoLength = 2;
            }
            if(arrayNotSorted) bogoSwap(array, bogoLength, 0);
        }
    }
}
package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class CocktailBogoSort extends BogoSorting {
    public CocktailBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Cocktail Bogo");
        this.setRunAllSortsName("Cocktail Bogo Sort");
        this.setRunSortName("Cocktail Bogosort");
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
        int minIterator = 0;
        int maxIterator = sortLength - 1;
        
        while(minIterator < maxIterator) {
            boolean maxSorted = this.isMaxSorted(array, minIterator, maxIterator);
            boolean minSorted = this.isMinSorted(array, maxIterator + 1, minIterator);
            
            while(!maxSorted && !minSorted) {
                this.bogoSwap(array, maxIterator + 1, minIterator);
                
                maxSorted = this.isMaxSorted(array, minIterator, maxIterator);
                minSorted = this.isMinSorted(array, maxIterator + 1, minIterator);
            }
            
            if(minSorted) {
                //Highlights.markArray(1, minIterator);
                minIterator++;
                minSorted = false;
            }
            if(maxSorted) {
                //Highlights.markArray(2, maxIterator);
                maxIterator--;
                maxSorted = false;
            }
        }
    }
}
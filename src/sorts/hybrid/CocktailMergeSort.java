package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.exchange.CocktailShakerSort;
import sorts.templates.Sort;
import sorts.templates.TimSorting;

// Inspired by Sorting Stuff's "Obscure Sorting Algorithms": https://www.youtube.com/watch?v=fWubJgIWyxQ

// Basically, "Cocktail Merge Sort" is a hybrid between Cocktail Shaker Sort and TimSort. It starts by building
// runs of TimSort's minimum length using Cocktail Shaker, then merges all these runs using TimSort. This 
// effectively replaces Binary Insertion Sort, used for building runs in TimSort. Big-O analysis would still say
// this is constant time, as the minrun value is not dependent on the number of elements we are sorting, but
// Cocktail Shaker has worse constant factors than Insertion Sort. So basically, this is just for fun.
// But hey, why not? ;)

public class CocktailMergeSort extends Sort {
    private TimSorting timSortInstance; // TimSort cannot be simply written off as an abstract class, as it creates an instance of itself
                                        // in order to track its state. Plus, it contains both instance and static methods, requiring even
                                        // more refactoring, which would be just doing unnecessary busy work. Instead of what we've done for
                                        // the rest of the algorithms, we'll favor composition over inheritance here and pass "util" objects
                                        // to it.
    
    public CocktailMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Cocktail Merge");
        this.setRunAllSortsName("Cocktail Merge Sort");
        this.setRunSortName("Cocktail Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        CocktailShakerSort cocktailShaker = new CocktailShakerSort(this.arrayVisualizer);
        int minRunLen = TimSorting.minRunLength(sortLength);
        
        if (sortLength == minRunLen) {
            cocktailShaker.runSort(array, sortLength, bucketCount);
        }
        else {
            int i = 0;
            for (; i <= (sortLength - minRunLen); i += minRunLen) {
                cocktailShaker.customSort(array, i, i + minRunLen);
            }
            if (i + minRunLen > sortLength) {
                cocktailShaker.customSort(array, i, sortLength);
            }
            
            Highlights.clearAllMarks();
            
            this.timSortInstance = new TimSorting(array, sortLength, this.arrayVisualizer);
            TimSorting.sort(this.timSortInstance, array, sortLength);
        }
    }
}
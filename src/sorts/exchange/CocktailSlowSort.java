package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author PiotrGrochowski
 *
 */
public final class CocktailSlowSort extends Sort {

    public CocktailSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cocktail Slow");
        this.setRunAllSortsName("Cocktail Slow Sort");
        this.setRunSortName("Cocktail Slowsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }
    
    private void cocktailslowSort(int[] A, int i, int j) {  
        if (i >= j) {
            return;
        }
        int m = i + ((j - i) / 2);
        
        Highlights.markArray(3, m);
    
        this.cocktailslowSort(A, i, m);
        this.cocktailslowSort(A, m + 1, j);
    
        if (Reads.compareValues(A[m], A[j]) == 1) {
            Writes.swap(A, m, j, 1, true, false);
        }
        if (((j-i)>1)&&(Reads.compareValues(A[i], A[m+1]) == 1)) {
            Writes.swap(A, i, m+1, 1, true, false);
        }
        
        Highlights.markArray(1, j);
        Highlights.markArray(2, m);
        
        this.cocktailslowSort(A, i+1, j - 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        cocktailslowSort(array, 0, sortLength - 1);

    }

}

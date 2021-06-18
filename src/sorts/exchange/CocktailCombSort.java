package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 *
 */
public final class CocktailCombSort extends Sort {

    public CocktailCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Cocktail Comb");
        this.setRunAllSortsName("Cocktail Comb Sort");
        this.setRunSortName("Cocktail Combsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void combSort(int[] array, int length, double shrink) {
        boolean anyswap = false;
        boolean dir = true;
        int gap = length;
        while((gap > 1) || anyswap) {
            if (gap > 1) 
                gap = (int) (gap / shrink);
            anyswap = false;
            if(dir) {
                for(int i = 0; (gap+i)<length; i++) {
                    if(Reads.compareIndices(array, i, i + gap, 0.25d, true) > 0) {
                        Writes.swap(array, i, i+gap, 0.75d, true, false);
                        anyswap = true;
                    }
                }
            }else {
                for(int i = length - 1; i>=gap; i--) {
                    if(Reads.compareIndices(array, i - gap, i, 0.25d, true) > 0) {
                        Writes.swap(array, i - gap, i, 0.75d, true, false);
                        anyswap = true;
                    }
                }
            }
            dir = !dir;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        combSort(array, sortLength, 1.3d);

    }

}

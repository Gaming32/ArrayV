package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 *
 */
public final class CocktailPopSort extends Sort {

    public CocktailPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Pop");
        this.setRunAllSortsName("Cocktail Pop Sort");
        this.setRunSortName("Cocktail Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void cocktailSort(int[] array, int start, int end, boolean fw) {
        int cmp = fw ? 1 : -1;
        int i = start;
        while(i < ((end / 2) + start)) {
            boolean sorted = true;
            for(int j = i; j < end + start - i - 1; j++) {
                if(Reads.compareIndices(array, j, j + 1, 0.125, true) == cmp) {
                    Writes.swap(array, j, j + 1, 0.25, true, false);
                    sorted = false;
                }
                
            }
            for(int j = end + start - i - 1; j > i; j--){
                if(Reads.compareIndices(array, j - 1, j, 0.125, true) == cmp) {
                    Writes.swap(array, j, j - 1, 0.25, true, false);
                    sorted = false;
                }
                
            }
            if(sorted) break;
            else i++;
        }
    }
    
    public void popSort(int[] array, int start, int end) {
        cocktailSort(array, start, start + ((end - start) / 4), false);
        cocktailSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        cocktailSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        cocktailSort(array, start + (3 * (end - start) / 4), end, true);
        cocktailSort(array, start, start + ((end - start) / 2), false);
        cocktailSort(array, start + ((end - start) / 2), end, true);
        cocktailSort(array, start, end, true);

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        popSort(array, 0, sortLength);

    }

}

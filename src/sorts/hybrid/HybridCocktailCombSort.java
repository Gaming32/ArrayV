package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.DoubleInsertionSort;
import sorts.templates.Sort;

public final class HybridCocktailCombSort extends Sort {

    public HybridCocktailCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Hybrid Cocktail Comb");
        this.setRunAllSortsName("Hybrid Cocktail Comb Sort");
        this.setRunSortName("Hybrid Cocktail Combsort");
        this.setCategory("Hybrid Sorts");
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
            if(gap <= Math.min(8, length * 0.03125)) {
                DoubleInsertionSort ins = new DoubleInsertionSort(arrayVisualizer);
                ins.customInsertSort(array, 0, length, 0.5d, false);
                break;
            }
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
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        combSort(array, sortLength, 1.3d);

    }

}

package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class ReverseCombSort extends Sort {

    public ReverseCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Reverse Comb");
        this.setRunAllSortsName("Reverse Comb Sort");
        this.setRunSortName("Reverse Combsort");
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
        int gap = length;
        while((gap > 1) || anyswap) {
            if (gap > 1) 
                gap = (int) (gap / shrink);
            anyswap = false;
            for(int i = length - 1; i>=gap; i--) {
                if(Reads.compareIndices(array, i - gap, i, 0.25d, true) > 0) {
                    Writes.swap(array, i - gap, i, 0.75d, true, false);
                    anyswap = true;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        combSort(array, sortLength, 1.3d);

    }

}

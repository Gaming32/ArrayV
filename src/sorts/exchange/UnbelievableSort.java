package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- THANKS, STANLEY P. Y. FUNG -
------------------------------

This algorithm is described here: https://arxiv.org/abs/2110.01111

*/
final public class UnbelievableSort extends Sort {
    public UnbelievableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unbelievable");
        this.setRunAllSortsName("Unbelievable Sort");
        this.setRunSortName("Unbelievable Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++)
            for (int j = 0; j < currentLength; j++) 
                if (Reads.compareIndices(array, i, j, 0.05, true) < 0) 
                    Writes.swap(array, i, j, 0.1, true, false);
    }
}
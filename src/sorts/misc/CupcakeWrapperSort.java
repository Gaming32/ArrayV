package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class CupcakeWrapperSort extends Sort {
    public CupcakeWrapperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cupcake Wrapper");
        this.setRunAllSortsName("Cupcake Wrapper Sort");
        this.setRunSortName("Cupcake Wrapper Sort");
        this.setCategory("Miscellaneous Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int right = currentLength;
        for (int limit = currentLength - 1; limit >= 0; limit--) {
            for (right = limit; right > 0; right--) {
                Highlights.markArray(1, 0);
                Highlights.markArray(2, right);
                Delays.sleep(0.25);
                if (Reads.compareValues(array[0], array[right]) > 0) {
                    Writes.reversal(array, 0, right, 0.25, true, false);
                }
            }
            Writes.reversal(array, 0, currentLength - 1, 0.25, true, false);
            Writes.reversal(array, 0, currentLength - 2, 0.25, true, false);
        }
    }
}
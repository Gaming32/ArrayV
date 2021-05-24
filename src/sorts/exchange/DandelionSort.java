package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author yuji
 * @author McDude_73
 *
 */
public final class DandelionSort extends Sort {

    public DandelionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Dandelion");
        setRunAllSortsName("Dandelion Sort");
        setRunSortName("Dandelion Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        for (int b = 0; b < sortLength; ) {
            this.Highlights.markArray(1, b);
            int pointer = b;
            boolean anyswap = false;
            
            while (pointer < sortLength - 1 && this.Reads.compareIndices(array, pointer + 1, pointer, 0.25D, true) < 0) {
              this.Writes.swap(array, pointer, pointer + 1, 0.5D, true, true);
              anyswap = true;
              pointer++;
            } 
            
            if (anyswap) {
              if (b > 0) b--;  continue;
            }  b++;
          } 


    }

}

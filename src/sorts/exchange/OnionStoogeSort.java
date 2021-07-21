package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author invented by Lancewer
 * @author implemented in Java by Yuri-chan2007
 *
 */
public final class OnionStoogeSort extends Sort {

    public OnionStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Onion Stooge");
        this.setRunAllSortsName("Onion Stooge Sort");
        this.setRunSortName("Onion Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }
    
    private void stoogeSort(int[] A, int i, int j) {
        if (Reads.compareIndices(A, i, j, 0.025, true) == 1) {
            Writes.swap(A, i, j, 0.05, true, false);
        }
        
        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;
            
            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
    
            this.stoogeSort(A, i, j-t);
            this.stoogeSort(A, i+t, j);
            this.stoogeSort(A, i, j-t);
        }
    }
    
    private void onionStooge(int[] array, int i, int l) {
        for(int ticker = 1; ticker < l; ticker++) {
            stoogeSort(array, i, i + ticker);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        onionStooge(array, 0, sortLength);

    }

}

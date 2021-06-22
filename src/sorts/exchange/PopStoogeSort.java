package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author invented by Lancewer
 * @author implemented in Java by Yuri-chan2007
 *
 */
public final class PopStoogeSort extends Sort {

    public PopStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop Stooge");
        this.setRunAllSortsName("Pop Stooge Sort");
        this.setRunSortName("Pop Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }
    
    private void stoogeSort(int[] A, int i, int j, boolean fw) {
        int cmp = fw ? 1 : -1;
        if (Reads.compareValues(A[i], A[j]) == cmp) {
            Writes.swap(A, i, j, 0.005, true, false);
        }
        
        Delays.sleep(0.0025);
        
        Highlights.markArray(1, i);
        Highlights.markArray(2, j);
        
        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;
            
            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
    
            this.stoogeSort(A, i, j-t, fw);
            this.stoogeSort(A, i+t, j, fw);
            this.stoogeSort(A, i, j-t, fw);
        }
    }
    
    private void popSort(int[] array, int start, int end) {
        stoogeSort(array, start, start + ((end - start) / 4) - 1, false);
        stoogeSort(array, start + ((end - start) / 4), start + ((end - start) / 2) - 1, true);
        stoogeSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4) - 1, false);
        stoogeSort(array, start + (3 * (end - start) / 4), end - 1, true);
        stoogeSort(array, start, start + ((end - start) / 2) - 1, false);
        stoogeSort(array, start + ((end - start) / 2), end - 1, true);
        stoogeSort(array, start, end - 1, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        popSort(array, 0, sortLength);

    }

}

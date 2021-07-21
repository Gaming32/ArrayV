package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*
 * The original Laziest Stable Sort was made by aphitorite.
 * This variant uses Pattern-Defeating Insertion Sort (thanks to
 * Gaming32) to create sorted segments.
 *  
 */

/**
 * @author Yuri-chan2007
 *
 */
public final class PDLaziestSort extends Sort {

    public PDLaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Laziest Stable");
        this.setRunAllSortsName("Pattern-Defeating Laziest Stable Sort");
        this.setRunSortName("Pattern-Defeating Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while(i < b) {
            int current = array[i];
            int pos = i - 1;
            while(pos >= a && Reads.compareValues(array[pos], current) > 0){
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
            }
            Writes.write(array, pos + 1, current, sleep, true, auxwrite);

            i++;
        }
    }
    
    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.cycleReverse(array, a, m, b, 1.0, true, false);
    }
    
    private int leftBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;
            
            if(Reads.compareValues(val, array[m]) <= 0) 
                b = m;
            else     
                a = m+1;
        }
        
        return a;
    }
    
    private int leftExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while(a-1+i < b && Reads.compareValues(val, array[a-1+i]) > 0) i *= 2;
        
        return this.leftBinSearch(array, a+i/2, Math.min(b, a-1+i), val);
    }
    
    private void inPlaceMerge(int[] array, int a, int m, int b) {
        int i = a, j = m, k;
        
        while(i < j && j < b){
            if(Reads.compareValues(array[i], array[j]) == 1) {
                k = this.leftExpSearch(array, j+1, b, array[i]);
                this.rotate(array, i, j, k);
                
                i += k-j;
                j = k;
            } 
            else i++;
        }
    }
    
    public void laziestStableSort(int[] array, int start, int end) {
        int len = end - start;
        if(len <= 16) {
            insertionSort(array, start, end, 0.5, false);
            return;
        }
        int i, blockLen = Math.max(16, (int)Math.sqrt(len));
        for(i = start; i+2*blockLen < end; i+=blockLen) {
            insertionSort(array, i, i + blockLen, 0.5, false);
        }
        insertionSort(array, i, end, 0.5, false);
        while(i-blockLen >= start) { 
            this.inPlaceMerge(array, i-blockLen, i, end);
            i-=blockLen;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.laziestStableSort(array, 0, sortLength);

    }

}

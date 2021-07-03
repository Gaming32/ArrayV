package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * The original Laziest Stable Sort was made by aphitorite.
 * This variant uses out-of-place merging.
 * 
 */

/**
 * @author Yuri-chan2007
 *
 */
public final class OOPLaziestSort extends Sort {

    public OOPLaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out-of-Place Laziest Stable");
        this.setRunAllSortsName("Out-of-Place Laziest Stable Sort");
        this.setRunSortName("Out-of-Place Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
        Writes.write(array, b, temp, 0.5, true, false);
    }
    
    private int rightBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;
            
            if(Reads.compareValues(val, array[m]) < 0) 
                b = m;
            else     
                a = m+1;
        }
        
        return a;
    }
    
    private void binaryInsertion(int[] array, int a, int b) {
        for(int i = a+1; i < b; i++)
            this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }
    
    protected void merge(int[] array, int[] temp, int start, int mid, int end) {
        for (int i = 0; i < mid - start; i++) {
            Highlights.markArray(1, i + start);
            Writes.write(temp, i, array[i + start], 1, false, true);
        }

        int bufferPointer = 0;
        int left = start;
        int right = mid;

        while (left < right && right < end) {
            Highlights.markArray(2, right);
            if (Reads.compareValues(temp[bufferPointer], array[right]) <= 0)
                Writes.write(array, left++, temp[bufferPointer++], 1, true, false);
            else
                Writes.write(array, left++, array[right++], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, left++, temp[bufferPointer++], 0.5, true, false);
        Highlights.clearAllMarks();
    }
    
    public void laziestStableSort(int[] array, int start, int end) {
        int len = end - start;
        if(len <= 16) {
            this.binaryInsertion(array, start, end);
            return;
        }
        
        int i, blockLen = Math.max(16, (int)Math.sqrt(len));
        for(i = start; i+2*blockLen < end; i+=blockLen) {
            this.binaryInsertion(array, i, i+blockLen);
        }
        this.binaryInsertion(array, i, end);
        int[] temp = Writes.createExternalArray(blockLen);
        
        while(i-blockLen >= start) { 
            this.merge(array, temp, i-blockLen, i, end);
            i-=blockLen;
        }
        Writes.deleteExternalArray(temp);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        laziestStableSort(array, 0, sortLength);

    }

}

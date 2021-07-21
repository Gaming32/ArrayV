package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Gaming32
 * @author Yuri-chan2007
 *
 */
public final class PDBinaryInsertionSort extends Sort {

    public PDBinaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Binary Insert");
        this.setRunAllSortsName("Pattern-Defeating Binary Insertion Sort");
        this.setRunSortName("Pattern-Defeating Binary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int rightBinSearch(int[] array, int a, int b, int val, double sleep) {
        while(a < b) {
            int m = a+(b-a)/2;
            Highlights.markArray(0, a);
            Highlights.markArray(1, m);
            Highlights.markArray(2, b);
            Delays.sleep(sleep);
            if(Reads.compareValues(val, array[m]) < 0) 
                b = m;
            else     
                a = m+1;
        }
        
        return a;
    }
    
    private void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        while(a > b) Writes.write(array, a, array[--a], sleep, true, false);
        Writes.write(array, b, temp, sleep, true, false);
    }
    
    //pattern-defeating insertion sort (thanks to Gaming32)
    //here I used binary search
    private void insertionSort(int[] array, int a, int b, double compSleep, double writeSleep) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, compSleep, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, compSleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, writeSleep, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, compSleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while(i < b) {
            insertTo(array, i, rightBinSearch(array, a, i, array[i], compSleep), writeSleep);
            i++;
        }
    }
    
    public void customBinaryInsert(int[] array, int start, int end, double sleep) {
        insertionSort(array, start, end, sleep/2.0, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertionSort(array, 0, sortLength, 1.0, 0.05);

    }

}

package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PatternDefeatingInsertionSort extends Sort {
    public PatternDefeatingInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pattern-Defeating Insertion");
        this.setRunAllSortsName("Pattern-Defeating Insertion Sort");
        this.setRunSortName("Pattern-Defeating Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
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
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 1, false);
    }
}
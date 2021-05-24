package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class AdaptiveInsertionSort extends Sort {
    public AdaptiveInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Adaptive Insertion");
        this.setRunAllSortsName("Adaptive Insertion Sort");
        this.setRunSortName("Adaptive Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int findRun(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, sleep, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    public void moveFront(int[] array, int a, int m, int b, double sleep, boolean auxwrite) {
        int mini = m;
        int minv = array[mini];
        Highlights.markArray(2, mini);
        for (int i = m + 1; i < b; i++) {
            if (Reads.compareIndexValue(array, i, minv, sleep, true) < 0) {
                mini = i;
                minv = array[i];
                Highlights.markArray(2, mini);
            }
        }
        Highlights.clearMark(2);
        m--;
        while (mini > m) {
            Writes.write(array, mini, array[mini - 1], sleep, true, false);
            mini--;
        }
        --a;
        while(mini > a && Reads.compareValues(array[mini], minv) > 0) {
            Writes.write(array, mini + 1, array[mini], sleep, true, auxwrite);
            mini--;
        }
        Writes.write(array, mini + 1, minv, sleep, true, auxwrite);
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = findRun(array, a, b, sleep, auxwrite);
        if (i < b) {
            moveFront(array, a, i++, b, sleep, auxwrite);
            while(i < b) {
                int current = array[i];
                int pos = i - 1;
                while(Reads.compareValues(array[pos], current) > 0) {
                    Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                    pos--;
                }
                Writes.write(array, pos + 1, current, sleep, true, auxwrite);
                i++;
            }
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 1, false);
    }
}
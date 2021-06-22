package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class PopSelectionSort extends Sort {

    public PopSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop Selection");
        this.setRunAllSortsName("Pop Selection Sort");
        this.setRunSortName("Pop Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void selectionSort(int[] array, int start, int end, boolean fw) {
        int cmp = fw ? -1 : 1;
        for (int i = start; i < end - 1; i++) {
            int idx = i;
            
            for (int j = i + 1; j < end; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(0.125);
                
                if (Reads.compareValues(array[j], array[idx]) == cmp){
                    idx = j;
                    Highlights.markArray(1, idx);
                    Delays.sleep(0.125);
                }
            }
            Writes.swap(array, i, idx, 0.25, true, false);
        }
    }
    
    public void popSort(int[] array, int start, int end) {
        selectionSort(array, start, start + ((end - start) / 4), false);
        selectionSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        selectionSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        selectionSort(array, start + (3 * (end - start) / 4), end, true);
        selectionSort(array, start, start + ((end - start) / 2), false);
        selectionSort(array, start + ((end - start) / 2), end, true);
        selectionSort(array, start, end, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}

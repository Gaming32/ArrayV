package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class UnstableInsertionSort extends Sort {
    public UnstableInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Unstable Insertion");
        this.setRunAllSortsName("Unstable Insertion Sort");
        this.setRunSortName("Unstable Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void unstableInsertionSort(int[] array, int start, int end) {
        for (int i = start + 1; i < end; ++i) {
            if (Reads.compareIndices(array, i, start, 1, true) < 0) {
                Writes.swap(array, i, start, 1, true, false);
            }
            int tmp = array[i];
            int j = i - 1;
            for (; Reads.compareValues(array[j], tmp) > 0; --j) {
                Writes.write(array, j+1, array[j], 1, true, false);
            }
            Writes.write(array, j+1, tmp, 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.unstableInsertionSort(array, 0, currentLength);
    }
}

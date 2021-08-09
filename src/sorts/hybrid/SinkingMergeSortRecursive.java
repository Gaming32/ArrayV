package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 *
 */
public final class SinkingMergeSortRecursive extends Sort {

    public SinkingMergeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sinking Merge (Recursive)");
        this.setRunAllSortsName("Recursive Sinking Merge Sort");
        this.setRunSortName("Recursive Sinking Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void bubbleSort(int[] array, int start, int end, double sleep) {
        int consecSorted = 1;
        for (int i = end - 1; i > start; i -= consecSorted) {
            consecSorted = 1;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, sleep / 2.0, true) > 0) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }

    public void sort(int[] array, int start, int end, double sleep) {
        if (end - start > 16) {
            int mid = start + (end - start) / 2;
            sort(array, start, mid, sleep);
            sort(array, mid, end, sleep);
            bubbleSort(array, start, end, sleep);
        } else
            bubbleSort(array, start, end, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.25);

    }

}

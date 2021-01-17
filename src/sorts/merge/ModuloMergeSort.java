package sorts.merge;

import java.lang.Math;
import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class ModuloMergeSort extends Sort {
    public ModuloMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Modulo Merge");
        this.setRunAllSortsName("Modulo Merge Sort");
        this.setRunSortName("Modulo Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void merge(int[] array, int start, int mid, int end, int maxEl) {
        int left      = start;
        int right     = mid + 1;
        int finalSpot = start;

        Highlights.clearAllMarks();

        while (left <= mid && right <= end) {
            Highlights.markArray(0, left);
            Highlights.markArray(1, right); 
            Delays.sleep(1);

            if (Reads.compareValues(array[left] % maxEl, array[right] % maxEl) <= 0) {
                Writes.write(array, finalSpot, array[finalSpot] + (array[left++] % maxEl) * maxEl, 1, true, false);
            } else {
                Writes.write(array, finalSpot, array[finalSpot] + (array[right++] % maxEl) * maxEl, 1, true, false);
            }
            finalSpot++;
        }

        Highlights.clearAllMarks();

        while (left <= mid) {
            Writes.write(array, finalSpot, array[finalSpot++] + (array[left++] % maxEl) * maxEl, 1, true, false);
        }
        while (right <= end) {
            Writes.write(array, finalSpot, array[finalSpot++] + (array[right++] % maxEl) * maxEl, 1, true, false);
        }

        for (int i = start; i <= end; i++) {
            Writes.write(array, i, array[i] / maxEl, 1, true, false);
        }
    }

    public void mergeSort(int[] array, int start, int end, int maxEl) {
        if (start < end) {
            int mid = start + ((end - start) / 2);
            this.mergeSort(array, start, mid, maxEl);
            this.mergeSort(array, mid + 1, end, maxEl);
            this.merge(array, start, mid, end, maxEl);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.mergeSort(array, 0, currentLength - 1, Reads.analyzeMax(array, currentLength, 0.5, true) + 1);
    }
}
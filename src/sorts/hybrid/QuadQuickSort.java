package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author thatsOven
 *
 */
public final class QuadQuickSort extends Sort {

    public QuadQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Quad Quick");
        setRunAllSortsName("Quad Quick Sort (By thatsOven)");
        setRunSortName("Quad Quicksort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);

    }

    public void insertionSort(int[] arr, int a, int b) {
        for (int i = a; i < b; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= a && this.Reads.compareValues(key, arr[j]) < 0) {
                this.Writes.write(arr, j + 1, arr[j], 0.5D, true, false);
                j--;
            }
            this.Writes.write(arr, j + 1, key, 0.5D, true, false);
        }
    }

    public int[] quadPartition(int[] arr, int start, int end, int[] pivArray) {
        int i = start;
        int j = start;
        int k = start;
        insertionSort(pivArray, 0, 3);
        for (int l = start; l < end; l++) {
            if (this.Reads.compareValues(arr[l], pivArray[2]) <= 0) {
                this.Writes.swap(arr, k, l, 0.5D, true, false);
                if (this.Reads.compareValues(arr[k], pivArray[1]) <= 0) {
                    this.Writes.swap(arr, j, k, 0.5D, true, false);
                    if (this.Reads.compareValues(arr[j], pivArray[0]) <= 0) {
                        this.Writes.swap(arr, i, j, 0.5D, true, false);
                        i++;
                    }
                    j++;
                }
                k++;
            }
        }
        pivArray[0] = i;
        pivArray[1] = j;
        pivArray[2] = k;
        return pivArray;
    }

    public int[] getpivot(int[] arr, int low, int high) {
        int m = low + (high - low) / 2;
        int mOfm = (m - low) / 2;
        int lowsum = arr[low] + arr[low + mOfm] + arr[m];
        int highsum = arr[m] + arr[m + mOfm] + arr[high - 1];
        int sum = lowsum + highsum - arr[m];
        this.Highlights.markArray(0, low);
        this.Highlights.markArray(1, m);
        this.Highlights.markArray(2, high - 1);
        this.Highlights.markArray(3, low + mOfm);
        this.Highlights.markArray(4, m + mOfm);
        this.Delays.sleep(2.0D);
        int[] pivArray = new int[3];
        pivArray[0] = lowsum / 3;
        pivArray[1] = sum / 5;
        pivArray[2] = highsum / 3;
        this.Highlights.clearAllMarks();
        return pivArray;
    }

    public void quickSort(int[] arr, int low, int high) {
        if (high - low > 16) {
            int[] pivArray = quadPartition(arr, low, high, getpivot(arr, low, high - 1));
            quickSort(arr, low, pivArray[0]);
            quickSort(arr, pivArray[0], pivArray[1]);
            quickSort(arr, pivArray[1], pivArray[2]);
            quickSort(arr, pivArray[2], high);
        } else {
            insertionSort(arr, low, high);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}

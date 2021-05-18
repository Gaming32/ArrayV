package sorts.quick;

import static java.lang.Math.cbrt;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public class CubeRootQuickSort extends Sort {
    private static double DELAY = 0.2;
    public CubeRootQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cube Root Quick");
        this.setRunAllSortsName("Cube Root Quick Sort");
        this.setRunSortName("Cube Root Quick Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void sort(int[] arr, int start, int stop) {
        int len = stop - start;
        if (len >= 2) {
            int root = (int) cbrt(len);
            int newStart = start + root;
            this.sort(arr, start, newStart);
            int[] pivots = new int[root];
            Writes.changeAllocAmount(pivots.length);
            for (int i = 0; i < root; i++)
                Writes.write(pivots, i, i + start, 0, false, true);
            for (int i = newStart; i < stop; i++) {
                int left = 0, right = root;
                while (left < right) {
                    int mid = (right - left) / 2 + left;
                    if (Reads.compareIndices(arr, pivots[mid], i, DELAY, true) == 1)
                        right = mid;
                    else
                        left = mid + 1;
                }
                int pos = i;
                for (int j = root - 1; j >= left; j--) {
                    Writes.swap(arr, pivots[j] + 1, pos, DELAY, true, false);
                    Writes.swap(arr, pos = pivots[j], pivots[j] + 1, DELAY, true, false);
                    Writes.write(pivots, j, pivots[j] + 1, 0, false, true);
                }
            }
            this.sort(arr, start, pivots[0]);
            for (int i = 1; i < root; i++)
                this.sort(arr, pivots[i - 1] + 1, pivots[i]);
            this.sort(arr, pivots[root - 1] + 1, stop);
            Writes.changeAllocAmount(-pivots.length);
        }
    }

    @Override
    public void runSort(int[] arr, int length, int buckets) {
        this.sort(arr, 0, length);
    }
}
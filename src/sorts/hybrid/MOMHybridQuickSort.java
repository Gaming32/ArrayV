package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

/**
 * @author aphitorite
 * @author Yuri-chan
 * @author thatsOven
 *
 */
public final class MOMHybridQuickSort extends Sort {

    public MOMHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("MOM Hybrid Quick");
        this.setRunAllSortsName("Median-of-Medians Hybrid Quick Sort");
        this.setRunSortName("Median-of-Medians Hybrid Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    PoplarHeapSort heapSorter;
    InsertionSort insSort;

    private void medianOfThree(int[] array, int a, int b) {
        int m = a + (b - 1 - a) / 2;

        if (Reads.compareIndices(array, a, m, 1, true) == 1)
            Writes.swap(array, a, m, 1, true, false);

        if (Reads.compareIndices(array, m, b - 1, 1, true) == 1) {
            Writes.swap(array, m, b - 1, 1, true, false);

            if (Reads.compareIndices(array, a, m, 1, true) == 1)
                return;
        }

        Writes.swap(array, a, m, 1, true, false);
    }

    // lite version
    private void medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;

        while (end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for (i = start; i + 2 * s <= end; i += s) {
                this.insSort.customInsertSort(array, i, i + s, 0.25, false);
                Writes.swap(array, j++, i + s / 2, 1.0, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                this.insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1.0, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0)
                    ad = !ad;
            }
            end = j;
        }
    }

    public int log2(int N) {
        int result = (int) (Math.log(N) / Math.log(2));
        return result;
    }

    public int partition(int[] array, int a, int b, int p) {
        int i = a - 1;
        int j = b;
        Highlights.markArray(3, p);

        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareIndices(array, i, p, 0, false) == -1);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareIndices(array, j, p, 0, false) == 1);

            if (i < j)
                Writes.swap(array, i, j, 1.0, true, false);
            else
                return j;
        }
    }

    public boolean getSortedRuns(int[] array, int start, int end) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = start; i < end - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, start, end - 1, 1, true, false);
            sorted = true;
        }

        return sorted;
    }

    private void quickSort(int[] array, int start, int end, int depthLimit) {
        while (end - start > 16) {
            if (getSortedRuns(array, start, end))
                return;
            if (depthLimit == 0) {
                heapSorter.heapSort(array, start, end);
                return;
            }
            medianOfThree(array, start, end);
            int p = partition(array, start + 1, end, start);
            int left = p - start;
            int right = end - (p + 1);
            if ((left == 0 || right == 0) || (left / right >= 16 || right / left >= 16)) {
                medianOfMedians(array, start, end, 5);
                p = partition(array, start + 1, end, start);
            }
            Writes.swap(array, start, p, 1.0, true, false);
            depthLimit--;
            quickSort(array, p + 1, end, depthLimit);
            end = p;
        }
        insSort.customInsertSort(array, start, end, 0.5, false);
    }

    public void customSort(int[] array, int start, int end) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, start, end, 2 * log2(end - start));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, 0, sortLength, 2 * log2(sortLength));

    }

}

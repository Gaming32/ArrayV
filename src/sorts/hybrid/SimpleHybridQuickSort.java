package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 *
 */
public final class SimpleHybridQuickSort extends Sort {

    public SimpleHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Simple Hybrid Quick");
        this.setRunAllSortsName("Simple Hybrid Quick Sort");
        this.setRunSortName("Simple Hybrid Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    PoplarHeapSort heapSorter;
    InsertionSort insertSorter;

    private int medianOfThree(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m], array[a]) > 0) {
            if (Reads.compareValues(array[m], array[b]) < 0)
                return m;

            if (Reads.compareValues(array[a], array[b]) > 0)
                return a;

            else
                return b;
        } else {
            if (Reads.compareValues(array[m], array[b]) > 0)
                return m;

            if (Reads.compareValues(array[a], array[b]) < 0)
                return a;

            else
                return b;
        }
    }

    private int ninther(int[] array, int a, int b) {
        int s = (b - a) / 9;

        int a1 = this.medianOfThree(array, a, a + s, a + 2 * s);
        int m1 = this.medianOfThree(array, a + 3 * s, a + 4 * s, a + 5 * s);
        int b1 = this.medianOfThree(array, a + 6 * s, a + 7 * s, a + 8 * s);

        return this.medianOfThree(array, a1, m1, b1);
    }

    private int medianOfThreeNinthers(int[] array, int a, int b) {
        int s = (b - a + 2) / 3;

        int a1 = this.ninther(array, a, a + s);
        int m1 = this.ninther(array, a + s, a + 2 * s);
        int b1 = this.ninther(array, a + 2 * s, b);

        return this.medianOfThree(array, a1, m1, b1);
    }

    private int partition(int[] array, int a, int b, int val) {
        int i = a, j = b;
        while (i <= j) {
            while (this.Reads.compareValues(array[i], val) < 0) {
                i++;
                this.Highlights.markArray(1, i);
                this.Delays.sleep(0.5D);
            }
            while (this.Reads.compareValues(array[j], val) > 0) {
                j--;
                this.Highlights.markArray(2, j);
                this.Delays.sleep(0.5D);
            }

            if (i <= j)
                this.Writes.swap(array, i++, j--, 1.0D, true, false);

        }
        return i;
    }

    private void sort(int[] array, int a, int b, int depthLimit) {
        while (b - a > 16) {
            if (depthLimit == 0) {
                heapSorter.heapSort(array, a, b);
                return;
            }
            int piv = medianOfThree(array, a, a + ((b - a) / 2), b - 1);
            int p = partition(array, a, b - 1, array[piv]);
            int left = p - a;
            int right = b - p;
            if ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16)) {
                piv = medianOfThreeNinthers(array, a, b - 1);
                p = partition(array, a, b - 1, array[piv]);
            }
            depthLimit--;
            sort(array, p, b, depthLimit);
            b = p;
        }

        insertSorter.customInsertSort(array, a, b, 0.5D, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSorter = new InsertionSort(this.arrayVisualizer);
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        sort(array, 0, sortLength, 2 * (int) (Math.log(sortLength) / Math.log(2.0D)));

    }

}

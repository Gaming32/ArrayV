package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/**
 * @author aphitorite
 *
 */
public final class OptimizedStableQuickSort extends Sort {

    public OptimizedStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Stable Quick");
        this.setRunAllSortsName("Optimized Stable Quick Sort");
        this.setRunSortName("Optimized Stable Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * The temporary storage for partitioning.
     */
    private int[] temp;
    
    /**
     * The current size of the temporary storage.
     * 
     */
    private int tempCount;
    
    private InsertionSort sort;

    private void add(int val) {
        this.Writes.write(this.temp, this.tempCount++, val, 0.5D, false, true);
    }

    private void copy(int[] array, int startIndex) {
        for (int i = 0; i < this.tempCount; i++)
            this.Writes.write(array, startIndex++, this.temp[i], 0.5D, true, false);
        this.tempCount = 0;
    }

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

    private int stablePartition(int[] array, int start, int end, int shift) {
        int j = start;
        this.Highlights.markArray(3, start);

        double s = 1.0D / (shift + 1);
        if (shift % 2 == 0)
            s = 2.0D - s;

        int fourths = (end - start) / 4;
        int offset = start + (int) (s * fourths);
        int pivotIndex = medianOfThree(array, offset, offset + fourths, offset + 2 * fourths);
        int pivotValue = array[pivotIndex];
        this.Highlights.markArray(4, pivotIndex);

        int a = start, b = end - 1;

        while (a < pivotIndex && this.Reads.compareValues(array[a], pivotValue) <= 0) {
            this.Highlights.markArray(1, a);
            this.Delays.sleep(0.5D);
            a++;
            j++;
        }
        if (a < pivotIndex) {
            add(array[a]);
            a++;
        }
        while (b > pivotIndex && this.Reads.compareValues(array[b], pivotValue) >= 0) {
            this.Highlights.markArray(1, b);
            this.Delays.sleep(0.5D);
            b--;
        }
        this.Highlights.clearMark(4);

        int i;
        for (i = a; i < pivotIndex; i++) {
            this.Highlights.markArray(1, i);
            this.Highlights.markArray(2, j);

            if (this.Reads.compareValues(array[i], pivotValue) <= 0) {
                this.Writes.write(array, j++, array[i], 0.5D, false, false);
            } else {

                add(array[i]);
            }
        }
        for (i = pivotIndex + 1; i < b; i++) {
            this.Highlights.markArray(1, i);
            this.Highlights.markArray(2, j);

            if (this.Reads.compareValues(array[i], pivotValue) == -1) {
                this.Writes.write(array, j++, array[i], 0.5D, false, false);
            } else {

                add(array[i]);
            }
        }
        if (b > pivotIndex) {
            this.Highlights.markArray(2, j);
            this.Writes.write(array, j++, array[b], 0.5D, false, false);
        }

        int p = j;
        this.Highlights.markArray(2, p);
        this.Writes.write(array, p, pivotValue, 0.5D, false, false);

        copy(array, p + 1);
        this.Highlights.clearMark(2);

        return p;
    }

    private void stableQuickSort(int[] array, int start, int end, int shift) {
        int a = start, length = end - a;
        while (length > 12) {
            int pivotIndex = stablePartition(array, a, end, shift);

            int left = pivotIndex - a;
            int right = end - pivotIndex - 1;

            if (left / length < 0.0625D || right / length < 0.0625D)
                shift++;

            stableQuickSort(array, a, pivotIndex, shift);

            a = pivotIndex + 1;
            length = end - a;
        }
        this.Highlights.clearAllMarks();
        this.sort.customInsertSort(array, a, end, 0.5D, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.sort = new InsertionSort(this.arrayVisualizer);
        this.temp = Writes.createExternalArray(sortLength);
        this.tempCount = 0;

        stableQuickSort(array, 0, sortLength, 0);

        Writes.deleteExternalArray(this.temp);

    }

}

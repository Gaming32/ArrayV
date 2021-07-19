package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

public final class OptimizedMeanQuickSort extends Sort {

    public OptimizedMeanQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Mean Quick");
        this.setRunAllSortsName("Optimized Mean Quick Sort");
        this.setRunSortName("Optimized Mean Quicksort");
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

    private int compareIntDouble(int left, double right) {

        int cmpVal = 0;

        if (left > right)
            cmpVal = 1;
        else if (left < right)
            cmpVal = -1;
        else
            cmpVal = 0;

        return Reads.compareValues(cmpVal, 0);
    }
    
    private int partition(int[] array, int a, int b, double val) {
        int i = a, j = b;
        while (i <= j) {
            while (this.compareIntDouble(array[i], val) < 0) {
                i++;
                this.Highlights.markArray(1, i);
                this.Delays.sleep(0.5D);
            }
            while (this.compareIntDouble(array[j], val) > 0) {
                j--;
                this.Highlights.markArray(2, j);
                this.Delays.sleep(0.5D);
            }

            if (i <= j)
                this.Writes.swap(array, i++, j--, 1.0D, true, false);

        }
        return i;
    }
    
    private double getPivot(int[] array, int start, int end) {
        double result = 0.0;
        for(int i = start; i<end; i++) {
            result += (double) array[i];
        }
        result/=(end-start);
        return result;
    }
    
    public boolean getSortedRuns(int[] array, int start, int end) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = start; i < end-1; i++) {
            comp = Reads.compareIndices(array, i, i+1, 0.5, true);
            if (comp > 0) sorted = false;
            else reverseSorted = false;
            if ((!reverseSorted) && (!sorted)) return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, start, end-1, 1, true, false); 
            sorted = true;
        }

        return sorted;
    }
    
    protected void sort(int[] array, int start, int end, int depthLimit) {
        while(end-start>16) {
            if(getSortedRuns(array, start, end)) return;
            if(depthLimit == 0) {
                heapSorter.heapSort(array, start, end);
                return;
            }
            double pivot = getPivot(array, start, end);
            int p = partition(array, start, end - 1, pivot);
            depthLimit--;
            sort(array, p, end, depthLimit);
            end = p;
        }
        insertSorter.customInsertSort(array, start, end, 0.5, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insertSorter = new InsertionSort(arrayVisualizer);
        sort(array, 0, sortLength,  2*(int)(Math.log(sortLength)/Math.log(2.0)));

    }

}

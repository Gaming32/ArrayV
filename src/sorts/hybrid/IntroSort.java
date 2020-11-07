package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

// original Copyright Ralph Unden, 
// http://ralphunden.net/content/tutorials/a-guide-to-introsort/?q=a-guide-to-introsort
// Modifications: Bernhard Pfahringer 
// changes include: local insertion sort, no global array

final public class IntroSort extends Sort {
    private MaxHeapSort heapSorter;

    private int middle;
    private int sizeThreshold = 16;
    
    public IntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Intro");
        //this.setRunAllID("Introspective Sort (std::sort)");
        this.setRunAllSortsName("Introspective Sort [std::sort]");
        this.setRunSortName("Introsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int floorLogBaseTwo(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2)));
    }
    
    // Swaps the median of arr[left], arr[mid], and arr[right] to index left.
    // taken from gcc source code found here: https://gcc.gnu.org/onlinedocs/gcc-4.7.2/libstdc++/api/a01462_source.html
    private int gccmedianof3(int[] arr, int left, int mid, int right) {
        if (Reads.compareValues(arr[left], arr[mid]) < 0) {
            if (Reads.compareValues(arr[mid], arr[right]) < 0) {
                Writes.swap(arr, left, mid, 1, true, false);
            }
            else if (Reads.compareValues(arr[left], arr[right]) < 0) {
                Writes.swap(arr, left, right, 1, true, false);
            }
        }
        else if (Reads.compareValues(arr[left], arr[right]) < 0) {
            middle = left;
            Highlights.markArray(3, left);
            return arr[left];
        }
        else if (Reads.compareValues(arr[mid], arr[right]) < 0) {
            Writes.swap(arr, left, right, 1, true, false);
        }
        else {
            Writes.swap(arr, left, mid, 1, true, false);
        }
        middle = left;
        Highlights.markArray(3, left);
        return arr[left];
    }
    
    private int medianof3(int[] arr, int left, int mid, int right) {
        if(Reads.compareValues(arr[right], arr[left]) == -1) {
            Writes.swap(arr, left, right, 1, true, false); 
        }
        if(Reads.compareValues(arr[mid], arr[left]) == -1) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if(Reads.compareValues(arr[right], arr[mid]) == -1) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        middle = mid;
        Highlights.markArray(3, mid);
        return arr[mid];
    }
    
    private int partition(int[] a, int lo, int hi, int x) {
        int i = lo, j = hi;
        while (true) {
            while (Reads.compareValues(a[i], x) == -1) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }

            j--;

            while (Reads.compareValues(x, a[j]) == -1) {
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
                j--;
            }

            if(!(i < j)) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                return i;
            }

            // Follow the pivot and highlight it.
            if(i == middle) {
                Highlights.markArray(3, j);
            }
            if(j == middle) {
                Highlights.markArray(3, i);
            }

            Writes.swap(a, i, j, 1, true, false);
            i++;
        }
    }
    
    private void introsortLoop (int[] a, int lo, int hi, int depthLimit) {
        while (hi - lo > sizeThreshold) {
            if (depthLimit == 0) {
                Highlights.clearAllMarks();
                heapSorter.customHeapSort(a, lo, hi, 1);
                return;
            }
            depthLimit--;
            int p = partition(a, lo, hi, medianof3(a, lo, lo + ((hi - lo) / 2), hi - 1));
            introsortLoop(a, p, hi, depthLimit);
            hi = p;
        }
        return;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        heapSorter = new MaxHeapSort(this.arrayVisualizer);

        introsortLoop(array, 0, length, 2 * floorLogBaseTwo(length));
        Highlights.clearAllMarks();

        InsertionSort sort = new InsertionSort(this.arrayVisualizer);
        sort.customInsertSort(array, 0, length, 0.5, false);
    }
}
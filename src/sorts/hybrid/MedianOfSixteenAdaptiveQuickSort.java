package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.UnstableInsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

/*
Median-Of-16 Adaptive QuickSort 2020 Copyright (C) thatsOven
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

final public class MedianOfSixteenAdaptiveQuickSort extends Sort {
    PoplarHeapSort heapSorter;
    UnstableInsertionSort insertSorter;

    public MedianOfSixteenAdaptiveQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Median-of-16 Adaptive Quick");
        this.setRunAllSortsName("thatsOven's Median-Of-16 Adaptive QuickSort");
        this.setRunSortName("thatsOven's Median-Of-16 Adaptive QuickSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int[] medianOfSixteenSwaps = new int[] {   
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
        1, 3, 5, 7, 9, 11, 13, 15, 2, 4, 6, 8, 10, 12, 14, 16,
        1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16,
        1, 9, 2, 10, 3, 11, 4, 12, 5, 13, 6, 14, 7, 15, 8, 16,
        6, 11, 7, 10, 4, 13, 14, 15, 8, 12, 2, 3, 5, 9,
        2, 5, 8, 14, 3, 9, 12, 15, 6, 7, 10, 11,
        3, 5, 12, 14, 4, 9, 8, 13,
        7, 9, 11, 13, 4, 6, 8, 10, 
        4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
        7, 8, 9, 10
    };

    int incs[] = {48, 21, 7, 3, 1};

    public int log2(int N) { 
        int result = (int)(Math.log(N) / Math.log(2)); 
        return result; 
    }

    private void shellSort(int[] array, int lo, int hi) {
        Highlights.clearAllMarks();

        for (int k = 0; k < this.incs.length; k++) {
            for (int h = this.incs[k], i = h + lo; i < hi; i++)
            {
                int v = array[i];
                int j = i;

                while (j >= h && Reads.compareValues(array[j-h], v) == 1)
                {
                    Highlights.markArray(1, j);
                    
                    Writes.write(array, j, array[j - h], 1, true, false);
                    j -= h;
                }
                Writes.write(array, j, v, 0.5, true, false);
            }
        }
        Highlights.clearAllMarks();
    }

    public int partition(int[] array, int a, int b, int p) {
        int i = a - 1;
        int j = b;
		    Highlights.markArray(3, p);
		
        while(true) {
            i++;
            while(i < b && Reads.compareIndices(array, i, p, 0, false) == -1) {
                Highlights.markArray(1, i);
                Delays.sleep(0.25);
                i++;
            }
            j--;
            while(j >= a && Reads.compareIndices(array, j, p, 0, false) == 1) {
                Highlights.markArray(2, j);
                Delays.sleep(0.25);
                j--;
            }
            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else      return j;
        }
    }
    
    private void medianOfThree(int[] array, int a, int b) {
      int m = a+(b-1-a)/2;

      if(Reads.compareIndices(array, a, m, 1, true) == 1)
        Writes.swap(array, a, m, 1, true, false);

      if(Reads.compareIndices(array, m, b-1, 1, true) == 1) {
        Writes.swap(array, m, b-1, 1, true, false);

        if(Reads.compareIndices(array, a, m, 1, true) == 1)
          return;
      }
		
      Writes.swap(array, a, m, 1, true, false);
    }

    private void compNSwap(int[] array, int a, int b, int gap, int start) {
        if (Reads.compareIndices(array, start+(a*gap), start+(b*gap), 2, true) > 0) {
            Writes.swap(array, start+(a*gap), start+(b*gap), 2, true, false);
        }
    }
    
    private void medianOfSixteen(int[] array, int a, int b) {
        int gap = (b - 1 - a) / 16;
        
        for (int i = 0; i < this.medianOfSixteenSwaps.length; i += 2) 
		this.compNSwap(array, this.medianOfSixteenSwaps[i], this.medianOfSixteenSwaps[i+1], gap, a);

        Writes.swap(array, a, a + (8*gap), 1, true, false);
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

    public void quickSort(int[] arr, int low, int high, int depthLimit) {
        while (high - low > 16) {
            if (this.getSortedRuns(arr, low, high)) return;
            if (depthLimit == 0){
                heapSorter.heapSort(arr, low, high);
                return;
            }
            this.medianOfThree(arr, low, high);
            int pi = this.partition(arr, low, high, low);
            int left  = pi-low;
            int right = high-(pi+1);
            if ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16)) {
                if (high-low > 80) {
                    this.medianOfSixteen(arr, low, high);
                    pi = this.partition(arr, low+1, high, low);
                } else {
                    this.shellSort(arr, low, high);
                    return;
                }
            }

            Writes.swap(arr, low, pi, 1, true, false);
                
            depthLimit--;
                
            this.quickSort(arr, pi+1, high, depthLimit);
            high = pi;
        }
        insertSorter.unstableInsertionSort(arr, low, high);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insertSorter = new UnstableInsertionSort(arrayVisualizer);

        this.quickSort(array, 0, currentLength, 2*log2(currentLength));
    }
}

package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.UnstableInsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

/*
Logarithmic Average QuickSort 2020 Copyright (C) thatsOven
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

final public class LAQuickSort extends Sort {
    PoplarHeapSort heapSorter;
    UnstableInsertionSort insertSorter;

    public LAQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Logarithmic Average Quick");
        this.setRunAllSortsName("Logarithmic Average QuickSort");
        this.setRunSortName("Logarithmic Average QuickSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    public int ghostPartition(int[] array, int a, int b, int val) {
        int i = a;
        int j = b - 1;
        while (i <= j) {
            while (Reads.compareValues(array[i], val) < 0) {
                i++;
            }
            while (Reads.compareValues(array[j], val) > 0) {
                j--;
            }
            if (i <= j) {
                Writes.swap(array, i++, j--, 1, true, false);
            }
        }
        Highlights.clearAllMarks();
        return i;
    }

    public int log2(int N) { 
        int result = (int)(Math.log(N) / Math.log(2)); 
        return result; 
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

    public int logarithmicAverage(int[] arr, int low, int high) {
        int sum = 0;
        int counter = 0;
        int qta = this.log2(high-low);
        if (2 > qta) {
            qta = 2;
        }
        for (int i = low; i < high; i += ((high-low) / qta)) {
            Highlights.markArray(0, i);
            Delays.sleep(1);
            sum += arr[i];
            counter++;
        }
        Highlights.clearAllMarks();
        sum = sum / counter;
        return sum;
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

    public void quickSort(int[] arr, int low, int high, int depthLimit, int backPivot, boolean logAvg, int equalPivotCount) {
        if (this.getSortedRuns(arr, low, high)) return;
        if (high-low > 16) {
            int pi = low, pivot = low;
            if (!logAvg) {
                this.medianOfThree(arr, low, high);
                pi = this.partition(arr, low, high, low);
                int left  = pi-low;
                int right = high-(pi+1);
                if ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16)) logAvg = true;
                else {
                    Writes.swap(arr, low, pi, 1, true, false);
                    pivot = arr[pi];
                }
            }
            if (logAvg) {
                pivot = this.logarithmicAverage(arr, low, high);
                pi = this.ghostPartition(arr, low, high, pivot);
            }
            if (backPivot == pivot) equalPivotCount++;
            if (depthLimit == 0 || equalPivotCount > 4){
                if (equalPivotCount > 4) equalPivotCount = 0;
                heapSorter.heapSort(arr, low, high);
                return;
            }
            depthLimit--;
            this.quickSort(arr, low, pi, depthLimit, pivot, logAvg, equalPivotCount);
            this.quickSort(arr, pi+(logAvg ? 0 : 1), high, depthLimit, pivot, logAvg, equalPivotCount);
        } else {
            insertSorter.unstableInsertionSort(arr, low, high);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insertSorter = new UnstableInsertionSort(arrayVisualizer);

        this.quickSort(array, 0, currentLength, 2*log2(currentLength), array[1], false, 0);
    }
}

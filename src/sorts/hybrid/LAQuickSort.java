package sorts.hybrid;

import main.ArrayVisualizer;
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
    public LAQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("LA Quick");
        this.setRunAllSortsName("thatsOven's Logarithmic Average QuickSort");
        this.setRunSortName("thatsOven's Logarithmic Average QuickSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insertionSort(int[] arr, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int key = arr[i];
            int j = i-1;
            
            while (j >= a && Reads.compareValues(key, arr[j]) < 0) {
                Writes.write(arr, j+1, arr[j], 0.5, true, false);
                j--;
            }
            Writes.write(arr, j+1, key, 0.5, true, false);
        }
    }

    // Poplar HeapSort by Morwenn
    private int hyperfloor(int n) {
        return (int) Math.pow(2, Math.floor(Math.log(n) / Math.log(2)));
    }

    private void unchecked_insertion_sort(int[] array, int first, int last) {
        for (int cur = first + 1; cur != last; ++cur) {
            int sift = cur;
            int sift_1 = cur - 1;
            if (Reads.compareValues(array[sift], array[sift_1]) == -1) {
                int tmp = array[sift];
                do {
                    Writes.write(array, sift, array[sift_1], 0.25, true, false);
                } while (--sift != first && Reads.compareValues(tmp, array[--sift_1]) == -1);
                Writes.write(array, sift, tmp, 0.25, true, false);
            }
        }
    }

    private void insertion_sort(int[] array, int first, int last) {
        if (first == last) return;
        this.unchecked_insertion_sort(array, first, last);
    }

    private void sift(int[] array, int first, int size) {
        if (size < 2) return;

        int root = first + (size - 1);
        int child_root1 = root - 1;
        int child_root2 = first + (size / 2 - 1);

        while (true) {
            int max_root = root;
            if (Reads.compareValues(array[max_root], array[child_root1]) == -1) {
                max_root = child_root1;
            }
            if (Reads.compareValues(array[max_root], array[child_root2]) == -1) {
                max_root = child_root2;
            }
            if (max_root == root) return;

            Writes.swap(array, root, max_root, 0.75, true, false);
            Highlights.clearMark(2);
            
            size /= 2;
            if (size < 2) return;

            root = max_root;
            child_root1 = root - 1;
            child_root2 = max_root - (size - size / 2);
        }
    }

    private void pop_heap_with_size(int[] array, int first, int last, int size) {
        int poplar_size = this.hyperfloor(size + 1) - 1;
        int last_root = last - 1;
        int bigger = last_root;
        int bigger_size = poplar_size;
	    
        int it = first;
        while (true) {
            int root = it + poplar_size - 1;
            if (root == last_root) break;
            if (Reads.compareValues(array[bigger], array[root]) == -1) {
                bigger = root;
                bigger_size = poplar_size;
            }
            it = root + 1;

            size -= poplar_size;
            poplar_size = this.hyperfloor(size + 1) - 1;
        }

        if (bigger != last_root) {
            Writes.swap(array, bigger, last_root, 0.75, true, false);
            Highlights.clearMark(2);
            this.sift(array, bigger - (bigger_size - 1), bigger_size);
        }
    }

    private void make_heap(int[] array, int first, int last) {
        int size = last - first;
        if (size < 2) return;

        int small_poplar_size = 15;
        if (size <= small_poplar_size) {
            this.unchecked_insertion_sort(array, first, last);
            return;
        }
	    
        int poplar_level = 1;

        int it = first;
        int next = it + small_poplar_size;
        while (true) {

            this.unchecked_insertion_sort(array, it, next);

            int poplar_size = small_poplar_size;

            for (int i = (poplar_level & (0 - poplar_level)) >> 1; i != 0; i >>= 1) {
                it -= poplar_size;
                poplar_size = 2 * poplar_size + 1;
                this.sift(array, it, poplar_size);
                ++next;
            }

            if ((last - next) <= small_poplar_size) {
                this.insertion_sort(array, next, last);
                return;
            }

            it = next;
            next += small_poplar_size;
            ++poplar_level;
        }
    }

    private void sort_heap(int[] array, int first, int last) {
        int size = last - first;
        if (size < 2) return;

        do {
            this.pop_heap_with_size(array, first, last, size);
            --last;
            --size;
        } while (size > 1);
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
                this.make_heap(arr, low, high);
                this.sort_heap(arr, low, high);
                return;
            }
            depthLimit--;
            this.quickSort(arr, low, pi, depthLimit, pivot, logAvg, equalPivotCount);
            this.quickSort(arr, pi+(logAvg ? 0 : 1), high, depthLimit, pivot, logAvg, equalPivotCount);
        } else {
            this.insertionSort(arr, low, high);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength, 2*log2(currentLength), array[1], false, 0);
    }
}

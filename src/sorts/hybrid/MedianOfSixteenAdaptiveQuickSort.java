package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.UnstableInsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/*
Copyright (c) 2020-2021 thatsOven
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:
The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

final public class MedianOfSixteenAdaptiveQuickSort extends Sort {
    MaxHeapSort heapSorter;
    UnstableInsertionSort insertSorter;

    public MedianOfSixteenAdaptiveQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Median-of-16 Adaptive Quick");
        this.setRunAllSortsName("Median-Of-16 Adaptive QuickSort");
        this.setRunSortName("Median-Of-16 Adaptive QuickSort");
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

                while (j >= h + lo && Reads.compareValues(array[j-h], v) == 1)
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

        Writes.swap(array, a, a + (8 * gap), 1, true, false);
    }

    public boolean getSortedRuns(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = a; i < b-1; i++) {
            comp = Reads.compareIndices(array, i, i+1, 0.5, true);
            if (comp > 0) sorted = false;
            else reverseSorted = false;
            if ((!reverseSorted) && (!sorted)) return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, a, b-1, 1, true, false); 
            sorted = true;
        }

        return sorted;
    }

    public void quickSort(int[] array, int a, int b, int depth, boolean unbalanced) {
        while (b - a > 32) {
            if (this.getSortedRuns(array, a, b)) return;
            if (depth == 0){
                heapSorter.customHeapSort(array, a, b, 1);
                return;
            }
            
            int p;
            if (!unbalanced) {
                this.medianOfThree(array, a, b);
                p = this.partition(array, a, b, a);
            } else p = a;
            
            int left  = p - a;
            int right = b - (p + 1);
            if ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16) || unbalanced) {
                if (b - a > 80) {
                    Writes.swap(array, a, p, 1, true, false);
                    if (left < right) {
                        this.quickSort(array, a, p, depth - 1, true);
                        a = p;
                    } else {
                        this.quickSort(array, p + 1, b, depth - 1, true);
                        b = p;
                    }
                    this.medianOfSixteen(array, a, b);
                    p = this.partition(array, a + 1, b, a);
                } else {
                    this.shellSort(array, a, b);
                    return;
                }
            }

            Writes.swap(array, a, p, 1, true, false);
                
            depth--;
                
            this.quickSort(array, p+1, b, depth, false);
            b = p;
        }
        insertSorter.unstableInsertionSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        heapSorter   = new MaxHeapSort(arrayVisualizer);
        insertSorter = new UnstableInsertionSort(arrayVisualizer);

        this.quickSort(array, 0, currentLength, 2*log2(currentLength), false);
    }
}

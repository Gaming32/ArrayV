package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.InsertionSort;
import main.ArrayVisualizer;

/*
 * 
MIT License
Copyright (c) 2020 fungamer2 & EilrahcF
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class StupidQuickSort extends Sort {
    
    public StupidQuickSort(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);
        
        this.setSortListName("Stupid Quick");
        this.setRunAllSortsName("Stupid Quick Sort");
        this.setRunSortName("Stupid Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int times = (int)Math.sqrt(length);
        for (int count = 0; count < times; count++) {
            int i = 0;
            int j = length - 1;
            
            int pivotPos = (int)(Math.random() * length);
            int pivot = array[pivotPos];
            
            while (i < j) {
                while (Reads.compareValues(array[i], pivot) == -1){
                    i++;
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
                while (Reads.compareValues(array[j], pivot) == 1){
                    j--;
                    Highlights.markArray(2, j);
                    Delays.sleep(1);
                }

                if (i < j) {
                    // Follow the pivot and highlight it.
                    if(i == pivotPos) {
                        Highlights.markArray(3, j);
                    }
                    if(j == pivotPos) {
                        Highlights.markArray(3, i);
                    }
                
                    Writes.swap(array, i, j, 1, true, false);
                
                    i++;
                    j--;
                }
            }
        }
        
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        InsertionSort insertSorter = new InsertionSort(arrayVisualizer);
        insertSorter.customInsertSort(array, 0, length, 0.4, false);
    }
}

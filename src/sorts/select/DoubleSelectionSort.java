package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

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

final public class DoubleSelectionSort extends Sort {
    public DoubleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Double Selection");
        this.setRunAllSortsName("Double Selection Sort");
        this.setRunSortName("Double Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int left = 0;
        int right = length - 1;
        int smallest = 0;
        int biggest = 0;
        
        while(left <= right) {
            for(int i = left; i <= right; i++) {
                Highlights.markArray(3, i);
                
                if(Reads.compareValues(array[i], array[biggest]) == 1) {
                    biggest = i;
                    Highlights.markArray(1, biggest);
                    Delays.sleep(0.01);
                }
                if(Reads.compareValues(array[i], array[smallest]) == -1) {
                    smallest = i;
                    Highlights.markArray(2, smallest);
                    Delays.sleep(0.01);
                }
                
                Delays.sleep(0.01);
            }
            if(biggest == left)
                biggest = smallest;
            
            Writes.swap(array, left, smallest, 0.02, true, false);
            Writes.swap(array, right, biggest, 0.02, true, false);
            
            left++;
            right--;
            
            smallest = left;
            biggest = right;
        }
    }
}
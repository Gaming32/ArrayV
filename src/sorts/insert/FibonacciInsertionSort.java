package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2020 fungamer2
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

final public class FibonacciInsertionSort extends Sort {
    
    public FibonacciInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Fibonacci Insertion");
        this.setRunAllSortsName("Fibonacci Insertion Sort");
        this.setRunSortName("Fibonacci Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void fibonacciInsertionSort(int[] array, int length) {
        for (int i = 1; i < length; i++) {
            int tmp = array[i];
            int position = this.fibonacciSearch(array, 0, i - 1, tmp);
            int j = i - 1;
            while (j >= position) {
                Writes.write(array, j + 1, array[j--], 0.15, true, false);
            }
            Writes.write(array, j + 1, tmp, 0.15, true, false);
        }
    }
    
    public int fibonacciSearch(int[] array, int start, int end, int item) {
        int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }
        
        int offset = start - 1;
        
        while (fibM > 1) {
            
            int i = Math.min(offset + fibM2, end);
            
            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);
            
            if (Reads.compareValues(array[i], item) <= 0) {
                fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
                fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
            Delays.sleep(0.6);
        }
        int position = ++offset;
        if (Reads.compareValues(array[position], item) <= 0) {
            ++position;
        }
        return position;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.fibonacciInsertionSort(array, length);
    }
}
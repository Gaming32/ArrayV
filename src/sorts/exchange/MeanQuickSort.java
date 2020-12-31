package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License
Copyright (c) 2020 Gaming32
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

final public class MeanQuickSort extends Sort {
    public MeanQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Mean Quick");
        this.setRunAllSortsName("Mean Quick Sort");
        this.setRunSortName("Mean Quick Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int compareIntDouble(int left, double right) {
        Reads.addComparison();
        
        int cmpVal = 0;
        
        Writes.startLap();

        if(left > right)      cmpVal =  1;
        else if(left < right) cmpVal = -1;
        else                  cmpVal =  0;

        Writes.stopLap();
        
        return cmpVal;
    }

    private void partition(int[] array, int start, int end, int sum) {
        int left = start;
        int right = end;
        Highlights.markArray(1, left);
        Highlights.markArray(2, right);

        int count = right - left + 1;
        if (count <= 1) {
            return;
        }

        double mean = (double)sum / (double)count;

        int lsum = 0;
        int rsum = 0;

        while (left <= right) {
            // while (this.compareIntDouble(array[right], mean) >= 0) {
            //     rsum += array[right];
            //     right--;
            //     Delays.sleep(0.5);
            //     Highlights.markArray(4, right);
            // }

            // if (left > right) {
            //     break;
            // }

            // if (this.compareIntDouble(array[left], mean) >= 0) {
            //     Writes.swap(array, left, right, 1, false, false);
            //     rsum += array[right];
            //     right--;
            //     Highlights.markArray(4, right);
            // }
            // Reads.addComparison();

            // if (left > right || left >= highestEnd) {
            //     break;
            // }

            // lsum += array[left];
            // left++;
            // Highlights.markArray(3, left);
            while (compareIntDouble(array[left], mean) == -1) {
                lsum += array[left];
                left++;
                Highlights.markArray(1, left);
                Delays.sleep(0.5);
            }
            while (compareIntDouble(array[right], mean) == 1) {
                rsum += array[right];
                right--;
                Highlights.markArray(2, right);
                Delays.sleep(0.5);
            }

            if (left <= right) {
                Writes.swap(array, left, right, 1, true, false);
                
                lsum += array[left];
                rsum += array[right];
                left++;
                right--;
            }
        }

        // Highlights.clearAllMarks();
        // partition(array, start, left + 1, lsum, highestEnd);
        // partition(array, left, end, rsum, highestEnd);
        if (start < right) {
            partition(array, start, right, lsum);
        }
        if (left < end) {
            partition(array, left, end, rsum);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int sum = 0;
        for (int i = 0; i < sortLength; i++) {
            Highlights.markArray(1, i);
            Writes.startLap();
            sum += array[i];
            Writes.stopLap();
            Delays.sleep(0.5);
        }
        Highlights.clearMark(1);

        partition(array, 0, sortLength - 1, sum);

        // for (int i = 1; i < sortLength; i++) {
        //     int j = i;
        //     while (j > 0 && Reads.compareIndices(array, j, j - 1, 0.25, true) == -1) {
        //         Writes.swap(array, j, j - 1, 0.25, true, false);
        //         j--;
        //     }
        // }
    }
}
package sorts.merge;

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

final public class PartialMergeSort extends Sort {
    public PartialMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Partial Merge");
        this.setRunAllSortsName("Partial Merge Sort");
        this.setRunSortName("Partial Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void merge(int[] array, int[] copied, int leftStart, int rightStart, int end) {
        Writes.arraycopy(array, leftStart, copied, 0, rightStart-leftStart, 1, true, true);

        int left = leftStart;
        int right = rightStart;
        for(int nxt = 0; nxt < end - leftStart; nxt++){
            if(left >= rightStart && right >= end) break;

            Highlights.markArray(1, nxt + leftStart);
            Highlights.markArray(2, right);

            if(left < rightStart && right >= end){
                Highlights.clearMark(2);
                Writes.write(array, nxt + leftStart, copied[(left++) - leftStart], 1, false, false);
            }
            else if(left >= rightStart && right < end){
                Highlights.clearMark(1);
                Writes.write(array, nxt + leftStart, array[right++], 1, false, false);
            }
            else if(Reads.compareValues(copied[left - leftStart], array[right]) <= 0){
                Writes.write(array, nxt + leftStart, copied[(left++) - leftStart], 1, false, false);
            }
            else{
                Writes.write(array, nxt + leftStart, array[right++], 1, false, false);
            }
        }

        Highlights.clearAllMarks();

    }

    private void mergeRun(int[] array, int[] copied, int start, int mid, int end) {
        if(start == mid) return;

        mergeRun(array, copied, start, (mid+start)/2, mid);
        mergeRun(array, copied, mid, (mid+end)/2, end);

        merge(array, copied, start, mid, end);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] copied = Writes.createExternalArray(length/2);
        int start = 0;
        int end = length;
        int mid = start + ((end - start) / 2);
        
        mergeRun(array, copied, start, mid, end);
        Writes.deleteExternalArray(copied);
    }
}
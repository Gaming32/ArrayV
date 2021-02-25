package sorts.insert;

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

final public class RoomSort extends Sort {
    double delay = 0.015;
    InsertionSort insertionSort;

    public RoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Room");
        this.setRunAllSortsName("Room Sort");
        this.setRunSortName("Roomsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insertTo(int[] array, int a, int b) {
        int val = array[a];
        a--;
        while (a >= b && Reads.compareValues(array[a], val) > 0) {
            Writes.write(array, a + 1, array[a], delay, true, false);
            a--;
        }
        Writes.write(array, a + 1, val, delay, true, false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort = new InsertionSort(arrayVisualizer);
        int roomLen = (int)Math.sqrt(currentLength) + 1;

        int end, i;
        for (end = currentLength; end > roomLen; end -= roomLen) {
            Highlights.clearAllMarks();
            insertionSort.customInsertSort(array, 0, roomLen, delay, false);
            for (i = roomLen; i < end; i++) {
                insertTo(array, i, i - roomLen);
            }
        }
        insertionSort.customInsertSort(array, 0, end, delay, false);
    }
}
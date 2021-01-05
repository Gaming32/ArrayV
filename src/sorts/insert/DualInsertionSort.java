package sorts.insert;

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

final public class DualInsertionSort extends Sort {
    public DualInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Dual Insertion");
        this.setRunAllSortsName("Dual Insertion Sort");
        this.setRunSortName("Dual Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void insertUp(int[] array, int left, int right, double sleep, boolean auxwrite) {
        int current = array[left];
        int pos = left + 1;
        
        while (pos < right && Reads.compareValues(array[pos], current) < 0) {
            Writes.write(array, pos - 1, array[pos], sleep, true, auxwrite);
            pos++;
        }
        Writes.write(array, pos - 1, current, sleep, true, auxwrite);
    }

    protected void insertDown(int[] array, int left, int right, double sleep, boolean auxwrite) {
        int current = array[right];
        int pos = right - 1;

        while (pos >= left && Reads.compareValues(array[pos], current) > 0) {
            Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
            pos--;
        }
        Writes.write(array, pos + 1, current, sleep, true, auxwrite);
    }

    protected void insertionSort(int[] array, int start, int end, double sleep, boolean auxwrite) {    
        int left = start + (end - start) / 2, right = left + 1;
        // int left = 0, right = 1;

        while (left >= start && right < end) {
            if (Reads.compareIndices(array, left, right, sleep, true) == 1) {
                Writes.swap(array, left, right, 0, true, auxwrite);
            }

            insertUp(array, left, right, sleep, auxwrite);
            insertDown(array, left, right, sleep, auxwrite);

            left--;
            right++;
        }

        while (left >= start) insertUp(array, left--, right, sleep, auxwrite);
        while (right < end) insertDown(array, left, right++, sleep, auxwrite);
    }

    public void customInsertSort(int[] array, int start, int end, double sleep, boolean auxwrite) {
        this.insertionSort(array, start, end, sleep, auxwrite);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.insertionSort(array, 0, currentLength, 0.015, false);
    }
}
package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32 (Josiah Glosson)

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

final public class BidirectionalInsertionSort extends Sort {
    public BidirectionalInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Bidirectional Insertion");
        this.setRunAllSortsName("Bidirectional Insertion Sort");
        this.setRunSortName("Bidirectional Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void insertFw(int[] array, int i, int current, double sleep, boolean auxwrite) {
        int pos = i - 1;
        while(Reads.compareValues(array[pos], current) > 0){
            Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
            pos--;
        }
        Writes.write(array, pos + 1, current, sleep, true, auxwrite);
    }

    protected void insertBw(int[] array, int i, int current, double sleep, boolean auxwrite) {
        int pos = i - 1;
        while(Reads.compareValues(array[pos], current) <= 0){
            Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
            pos--;
        }
        Writes.write(array, pos + 1, current, sleep, true, auxwrite);
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        boolean dir = true;
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            if (dir) {
                if (Reads.compareValues(current, array[a]) < 0) {
                    Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
                    dir = !dir;
                    Highlights.clearMark(2);
                } else {
                    insertFw(array, i, current, sleep, auxwrite);
                }
            } else {
                if (Reads.compareValues(current, array[a]) >= 0) {
                    Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
                    dir = !dir;
                    Highlights.clearMark(2);
                } else {
                    insertBw(array, i, current, sleep, auxwrite);
                }
            }
        }
        if (!dir) {
            Writes.reversal(array, a, b - 1, sleep, true, auxwrite);
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength, 0.015, false);
    }
}
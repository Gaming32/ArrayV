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

final public class ReverseLazyStableSort extends Sort {
    public ReverseLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Reverse Lazy Stable");
        this.setRunAllSortsName("Reverse Lazy Stable Sort");
        this.setRunSortName("Reverse Lazy Stable Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void rotateLeft(int[] array, int start, int dest, int size) {
        int amount = start - dest;
        if (size > 1) {
            while (amount >= size) {
                for (int i = start; i > start - size; i--) {
                    Writes.swap(array, i - 1, i + size - 1, 0.5, true, false);
                }
                start -= size;
                amount -= size;
            }
            Highlights.clearMark(2);
            if (amount > 0) {
                rotateLeft(array, start, dest, size / 2);
                rotateLeft(array, start + size / 2, dest + size / 2, size - (size / 2));
            }
        }
        else {
            int tmp = array[start];
            for (int i = start; i > dest; i--) {
                Writes.write(array, i, array[i - 1], 0.5, true, false);
            }
            Writes.write(array, dest, tmp, 0.5, true, false);
        }
    }

    private int rotateRight(int[] array, int start, int dest, int size) {
        int amount = dest - start;
        int moved = 0;
        if (size > 1) {
            while (amount >= size) {
                for (int i = start; i < start + size; i++) {
                    Writes.swap(array, i, i + size, 1, true, false);
                }
                start += size;
                amount -= size;
                moved += size;
            }
            Highlights.clearMark(2);
        }
        else {
            int tmp = array[start];
            for (int i = start; i < dest; i++) {
                Writes.write(array, i, array[i + 1], 0.5, true, false);
            }
            Writes.write(array, dest, tmp, 0.5, true, false);
            moved += dest - start;
        }
        return moved;
    }

    public void rotateSmart(int[] array, int start, int dest, int size) {
        if (size > start - dest) {
            int startDest = start - dest;
            int moved = rotateRight(array, dest, start + size - startDest, startDest);
            size -= moved;
            dest = dest + moved;
            start = dest + startDest;
        }
        if (size > 0) {
            rotateLeft(array, start, dest, size);
        }
    }

    // Copied from BinaryInsertionSorting.java (and slightly modified)
    private int binSearch(int[] array, int start, int i, int num) {
        int lo = start, hi = i;
        
        while (lo < hi) {
            int mid = lo + ((hi - lo) / 2); // avoid int overflow!
            Highlights.markArray(1, lo);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, hi);
            
            Delays.sleep(0.5);
            
            if (Reads.compareValues(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element; this maintains stability!
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }

        Highlights.clearMark(3);
        return lo;
    }

    public void merge(int[] array, int start, int mid, int end) {
        while (start < mid) {
            int size = binSearch(array, mid, end, array[start]) - mid;
            rotateSmart(array, mid, start, size);

            start += size + 1;
            mid += size;
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int gap;
        for (gap = 2; gap <= length; gap *= 2) {
            for (int i = 0; i < length; i += gap) {
                merge(array, i, i + gap / 2, i + gap);
            }
        }

        if (length - gap / 2 > 0) {
            merge(array, 0, gap / 2, length);
        }
    }
}

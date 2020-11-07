package sorts.distribute;

import java.util.Arrays;
import java.util.ArrayList;

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

final public class OptimizedCountingSort extends Sort {
    public OptimizedCountingSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized Counting");
        this.setRunAllSortsName("Optimized Counting Sort");
        this.setRunSortName("Optimized Counting Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        ArrayList<Integer> counts = new ArrayList<Integer>();

        for (int i = 0; i < sortLength; i++) {
            if (Reads.compareValues(array[i], counts.size() - 1) == 1) {
                for (int j = counts.size(); j <= array[i]; j++) {
                    Writes.arrayListAdd(counts, 0);
                    Writes.mockWrite(counts.size(), counts.size() - 1, 0, 0);
                }
            }
            Highlights.markArray(1, i);
            counts.set(array[i], counts.get(array[i]) + 1);
            Writes.mockWrite(counts.size(), array[i], counts.get(array[i]), 1);
        }

        int dest = 0;
        for (int i = 0; i < counts.size(); i++) {
            for (int j = 0; j < counts.get(i); j++) {
                Writes.write(array, dest, i, 1, true, false);
                dest++;
            }
        }

        Writes.deleteArrayList(counts);
    }
}
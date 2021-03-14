package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020-2021 Gaming32

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

final public class OptimizedIndexSort extends Sort {
    public OptimizedIndexSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Optimized Index");
        this.setRunAllSortsName("Optimized Index Sort");
        this.setRunSortName("Optimized Indexsort");
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
        int min = Reads.analyzeMin(array, sortLength, 0.5, true);

        for (int i = 0; i < sortLength; i++) {
            Highlights.markArray(2, i);
            int current = array[i];
            int cmpCount = 0;
            while (Reads.compareValues(i, current - min) != 0 && cmpCount < sortLength) {
                int tmp = array[current - min];
                Writes.write(array, current - min, current, 0.5, true, false);
                current = tmp;
                cmpCount++;
            }
            if (cmpCount >= sortLength - 1) break;
            if (cmpCount > 0)
                Writes.write(array, i, current, 0.5, true, false);
            Delays.sleep(0.5);
            Highlights.clearMark(1);
        }
    }
}
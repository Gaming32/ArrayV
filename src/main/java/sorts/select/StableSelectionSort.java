package sorts.select;

import sorts.templates.Sort;
import main.ArrayVisualizer;

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

final public class StableSelectionSort extends Sort {
    
    public StableSelectionSort(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);
        
        this.setSortListName("Stable Selection");
        this.setRunAllSortsName("Stable Selection Sort");
        this.setRunSortName("Stable Selection Sort");
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
        for (int i = 0; i < length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < length; j++) {
                Highlights.markArray(1, j);
                if (Reads.compareValues(array[j], array[min]) == -1) {
                    min = j;
                    Highlights.markArray(2, j);
                }
                Delays.sleep(1);
            }
            Highlights.clearMark(2);
            int tmp = array[min];
            int pos = min;
            while (pos > i) {
                Writes.write(array, pos, array[pos - 1], 0.5, true, false);
                pos--;
            }
            Writes.write(array, pos, tmp, 0.5, true, false);
        }
    }
}
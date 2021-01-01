package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.BlockSwapMergeSort;
import sorts.merge.ReverseLazyStableSort;
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

final public class StableThirdMergeSort extends Sort {
    ReverseLazyStableSort rotater;
    ThirdMergeSort sort;
    BlockSwapMergeSort fallbackSort;

    public StableThirdMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stable 3rd Merge");
        this.setRunAllSortsName("Stable Third Merge Sort");
        this.setRunSortName("Stable Third Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(true);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean ejectDuplicates(int[] array, int start, int mid, int end) {
        boolean success = true;
        int minbound = start;
        int compindex = start + 1;
        for (int i = 0; i < mid - start; i++) {
            int num = array[compindex];
            int l = minbound, h = compindex;
            
            while (l < h) {
                int m = l + ((h - l) / 2); // avoid int overflow!
                Highlights.markArray(1, l);
                Highlights.markArray(2, m);
                Highlights.markArray(3, h);
                
                Delays.sleep(0.0625);
                
                int comp = Reads.compareValues(num, array[m]);

                if (comp < 0) {
                    h = m;
                }
                else if (comp == 0) {
                    l = minbound;
                    minbound++;
                    i--;
                    break;
                }
                else {
                    l = m + 1;
                }
            }

            Highlights.clearMark(3);
            
            // item has to go into position lo

            int j = compindex - 1;
            
            while (j >= l)
            {
                Writes.write(array, j + 1, array[j], 0.1, true, false);
                j--;
            }
            Writes.write(array, l, num, 0.1, true, false);
            
            Highlights.clearAllMarks();
            compindex++;

            if (compindex >= end) {
                success = false;
                break;
            }
        }

        if (minbound != start) {
            rotater.rotateSmart(array, minbound, start, compindex - minbound);
        }
        return success;
    }
    
    @Override
    public void runSort(int[] array, int length, int baseCount) throws Exception {
        rotater = new ReverseLazyStableSort(arrayVisualizer);
        sort = new ThirdMergeSort(arrayVisualizer);
        fallbackSort = new BlockSwapMergeSort(arrayVisualizer);

        if (ejectDuplicates(array, 0, (int)Math.ceil(length / 4d), length))
            sort.thirdMergeSort(array, length);
        else
            fallbackSort.multiSwapMergeSort(array, 0, length);
    }
}
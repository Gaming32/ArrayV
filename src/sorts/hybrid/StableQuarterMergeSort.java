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

final public class StableQuarterMergeSort extends Sort {
    ReverseLazyStableSort rotater;
    QuarterMergeSort sort;
    BlockSwapMergeSort fallbackSort;

    public StableQuarterMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stable Quarter Merge");
        this.setRunAllSortsName("Stable Quarter Merge Sort");
        this.setRunSortName("Stable Quarter Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(true);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int ejectDuplicates(int[] array, int start, int mid, int end) {
        int minbound = start;
        int compindex = start + 1;
        int lastGood = compindex;
        int badCount = 0;
        int count;
        for (count = 1; count < mid - start; count++) {
            Highlights.markArray(4, compindex);
            int num = array[compindex];
            int l = minbound, h = lastGood;
            int lastBad = badCount;

            while (l < h) {
                int m = l + ((h - l) / 2); // avoid int overflow!
                Highlights.markArray(1, l);
                Highlights.markArray(2, m);
                Highlights.markArray(3, h);
                
                Delays.sleep(0.01);
                
                int comp = Reads.compareValues(num, array[m]);

                if (comp < 0) {
                    h = m;
                }
                else if (comp == 0) {
                    badCount++;
                    break;
                }
                else {
                    l = m + 1;
                }
            }

            Highlights.clearMark(3);

            if (badCount > 0) {
                if (badCount > lastBad) {
                    Delays.sleep(0.2);
                    count--;
                    compindex++;
                    if (compindex >= end) {
                        break;
                    }
                    continue;
                }
                rotater.rotateCommon(array, lastGood, minbound, badCount, 0.1, false);
                minbound += badCount;
                lastGood = compindex;
                l += badCount;
                badCount = 0;
            }
            
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
            lastGood++;

            if (compindex >= end) {
                break;
            }
        }

        Highlights.clearMark(4);
        if (minbound != start) {
            rotater.rotateSmart(array, minbound, start, count);
        }
        return count;
    }
    
    @Override
    public void runSort(int[] array, int length, int baseCount) throws Exception {
        rotater = new ReverseLazyStableSort(arrayVisualizer);
        sort = new QuarterMergeSort(arrayVisualizer);
        fallbackSort = new BlockSwapMergeSort(arrayVisualizer);

        int required = length / 4;
        if (ejectDuplicates(array, 0, required, length) < required)
            fallbackSort.multiSwapMergeSort(array, 0, length);
        else
            sort.quarterMergeSort(array, length);
    }
}
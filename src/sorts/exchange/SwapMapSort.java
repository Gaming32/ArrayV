package sorts.exchange;

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

final public class SwapMapSort extends Sort {
    public SwapMapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Swap Map");
        this.setRunAllSortsName("Swap Map Sort");
        this.setRunSortName("Swap Map Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        ArrayList<Integer> map = new ArrayList<>();

        while (true) {
            for (int i = 0; i < sortLength - 1; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.001, true) == 1) {
                    Writes.arrayListAdd(map, i);
                    Writes.mockWrite(map.size(), map.size() - 1, i, 0);
                }
            }

            if (map.size() == 0) {
                break;
            }

            for (int i = 0; i < map.size(); i++) {
                Writes.swap(array, map.get(i), map.get(i) + 1, 0.01, true, false);
            }
            Writes.arrayListClear(map);
        }
    }
}
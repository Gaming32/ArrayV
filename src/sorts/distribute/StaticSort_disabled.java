package sorts.distribute;

import java.util.Arrays;

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

final public class StaticSort extends Sort {
    public StaticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Static");
        this.setRunAllSortsName("Static Sort");
        this.setRunSortName("Static Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private class StaticIndex {
        public int index;
        public int length;

        public StaticIndex(int index, int length) {
            this.index = index;
            this.length = length;
        }
    }

    private void insertItem(int[] a, int dest, int start) {
        dest++;

        int dir;
        if (dest < start) {
            dir = -1;
        } else if (dest > start) {
            dir = 1;
        } else {
            return;
        }

        int j = start + dir;
        int key = a[start];
        while (j + dir != key) {
            Writes.write(a, j - dir, a[j], 0.001, true, false);
            j += dir;
            Writes.changeWrites(-1);
        }
        Writes.write(a, j - dir, key, 0.001, true, false);
    }

    @Override
    public void runSort(int[] a, int size, int bucketCount) throws Exception {
        int M = Reads.analyzeMax(a, size, 1, true);
        Delays.setSleepRatio(0.000125);
        double constant = (double)M / (size + 4);
        int counter = 0;
        int listCount = 0;

        StaticIndex[] indices = new StaticIndex[size];

        while (listCount < size) {
            if (indices[counter] != null) {
                counter++;
            } else {
                if ((int)(a[counter] * constant) == counter) {
                    indices[counter] = new StaticIndex(counter, 1);
                } else {
                    if (indices[(int)(a[counter] * constant)] != null) {
                        insertItem(a, (int)(a[counter] * constant) + 1, counter);
                        indices[(int)(a[counter] * constant)].length++;
                        indices[counter] = new StaticIndex(counter, 0);
                    } else {
                        insertItem(a, (int)(a[counter] * constant), counter);
                        indices[(int)(a[counter] * constant)] = new StaticIndex((int)(a[counter] * constant), 1);
                        a[counter] = a[(int)(a[counter] * constant)];
                        indices[counter] = indices[(int)(a[counter] * constant)];
                    }
                }
                listCount++;
            }
        }
    }
}
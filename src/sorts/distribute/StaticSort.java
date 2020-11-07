package sorts.distribute;

import java.util.ArrayList;
import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
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
    MaxHeapSort heapSorter;
    InsertionSort insertSorter;

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

    private class StaticItem {
        public boolean isSubArray;
        public int value;
        public ArrayList<Integer> subArray;

        public StaticItem(int value) {
            this.isSubArray = false;
            this.value = value;
            this.subArray = null;
        }

        public StaticItem(int[] value) {
            this.isSubArray = true;
            this.value = 0;
            this.subArray = new ArrayList<>();
            for (int i = 0; i < value.length; i++) {
                this.subArray.add(value[i]);
            }
        }
    }

    @Override
    public void runSort(int[] mainArray, int size, int bucketCount) throws Exception {
        heapSorter = new MaxHeapSort(this.arrayVisualizer);
        insertSorter = new InsertionSort(this.arrayVisualizer);

        int M = Reads.analyzeMax(mainArray, size, 1, true);
        double constant = (double)M / (size + 4);
        int counter = 0;
        int listCount = 0;

        StaticItem[] a = new StaticItem[size];
        for (int i = 0; i < size; i++) {
            a[i] = new StaticItem(mainArray[i]);
        }

        while (listCount < size) {
            if (a[counter].isSubArray) {
                counter++;
            } else {
                if ((int)(a[counter].value * constant) == counter) {
                    a[counter] = new StaticItem(new int[] {a[counter].value});
                    Writes.changeWrites(1);
                } else {
                    if (a[(int)(a[counter].value * constant)].isSubArray) {
                        a[(int)(a[counter].value * constant)].subArray.add(a[counter].value);
                        a[counter] = new StaticItem(new int[] {});
                        Writes.changeWrites(2);
                    } else {
                        StaticItem tmp = a[(int)(a[counter].value * constant)];
                        a[(int)(a[counter].value * constant)] = new StaticItem(new int[] {a[counter].value});
                        a[counter] = tmp;
                        Writes.changeWrites(2);
                    }
                }
                listCount++;
            }
            
            // Render loop
            int wcount = 0;
            for (int i = 0; i < size; i++) {
                if (a[i].isSubArray) {
                    for (int j = 0; j < a[i].subArray.size(); j++) {
                        mainArray[wcount] = a[i].subArray.get(j);
                        wcount++;
                    }
                } else {
                    mainArray[wcount] = a[i].value;
                    wcount++;
                }
            }
            
            Highlights.markArray(1, counter);
            Delays.sleep(1);
        }

        int index = 0;
        for (int i = 0; i < size; i++) {
            int lt = a[i].subArray.size();
            if (lt > 1) {
                if (lt > 16) {
                    heapSorter.customHeapSort(mainArray, index, lt, 1);
                } else {
                    insertSorter.customInsertSort(mainArray, index, index + lt, 1, false);
                }
            }
            index += lt;
        }
    }
}
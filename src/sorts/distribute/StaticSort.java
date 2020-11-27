package sorts.distribute;

import java.util.ArrayList;
import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/*
 * 
staticSort Copyright(C) 2020 thatsOven

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

final public class StaticSort extends Sort {
    MaxHeapSort heapSorter;
    InsertionSort insertSorter;

    public StaticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Static");
        this.setRunAllSortsName("Static Sort");
        this.setRunSortName("thatsOven's staticSort");
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
        double constant = (double)M / (size + 2);
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
                if (Reads.compareValues((int)(a[counter].value * constant), counter) == 0) {
                    a[counter] = new StaticItem(new int[] {a[counter].value});
                    Writes.changeWrites(1);
                } else {
                    if (a[(int)(a[counter].value * constant)].isSubArray) {
                        a[(int)(a[counter].value * constant)].subArray.add(a[counter].value);
                        Writes.mockWrite(
                            a[(int)(a[counter].value * constant)].subArray.size(),
                            a[(int)(a[counter].value * constant)].subArray.size() - 1,
                            a[counter].value, 0);
                        Writes.changeAuxWrites(-1);
                        a[counter] = new StaticItem(new int[] {});
                        Writes.changeWrites(2);
                    } else {
                        StaticItem tmp = a[(int)(a[counter].value * constant)];
                        StaticItem newItem = new StaticItem(new int[] {a[counter].value});
                        Writes.startLap();
                        a[(int)(a[counter].value * constant)] = newItem;
                        a[counter] = tmp;
                        Writes.stopLap();
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

        // Simulate staticSort's merge_arrays function
        int wcount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < a[i].subArray.size(); j++) {
                Writes.write(mainArray, wcount, a[i].subArray.get(j), 0, false, false);
                Writes.changeWrites(-1);
                wcount++;
            }
        }

        int index = 0;
        for (int i = 0; i < size; i++) {
            int lt = a[i].subArray.size();
            Highlights.markArray(3, index);
            Highlights.markArray(4, index + lt);
            if (lt > 1) {
                if (lt > 16) {
                    heapSorter.customHeapSort(mainArray, index, lt, 1);
                } else {
                    insertSorter.customInsertSort(mainArray, index, index + lt, 1, false);
                }
            }
            Delays.sleep(0.2);
            index += lt;
        }
    }
}

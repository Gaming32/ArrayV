package sorts.distribute;

import java.util.ArrayList;

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

final public class DivisorSort extends Sort {
    public DivisorSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Divisor");
        //this.setRunAllID("Most Significant Digit Radix Sort");
        this.setRunAllSortsName("Divisor Sort");
        this.setRunSortName("Divisor Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void safePush(ArrayList<ArrayList<Integer>> aList, int index, int value) {
        while (aList.size() <= index) {
            aList.add(new ArrayList<>());
        }
        Writes.arrayListAdd(aList.get(index), value, true, 1);
    }

    private void divisorLoop(int[] array, int start, int length, int base, int log) {
        int divisor = (int)Math.pow(base, log);
        ArrayList<ArrayList<Integer>> buckets = new ArrayList<>();
        
        for (int i = start; i < start + length; i++) {
            Highlights.markArray(1, i);
            safePush(buckets, (array[i] - start) / divisor, array[i]);
        }

        int current = start;
        for (int i = 0; i < buckets.size(); i++) {
            int size = buckets.get(i).size();
            for (int j = 0; j < size; j++) {
                Writes.write(array, current + j, buckets.get(i).get(j), 1, true, false);
            }
            if (size > 1) {
                divisorLoop(array, current, size, base, log / 2);
            }
            current += size;
        }

        for (ArrayList<Integer> bucket : buckets) {
            Writes.deleteArrayList(bucket);
        }
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int base) throws Exception {
        this.setRunAllSortsName("Divisor Sort, Base " + base);

        int highestLog = Reads.analyzeMaxCeilingLog(array, sortLength, base, 0.5, true);

        divisorLoop(array, 0, sortLength, base, highestLog / 2);
    }
}
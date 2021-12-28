package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import java.util.ArrayList;


/*
Copyright (c) 2020-2021 thatsOven

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
*/

final public class FeatureSort extends Sort {
    public FeatureSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Feature");
        this.setRunAllSortsName("Feature Sort");
        this.setRunSortName("Feature Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void arrayListSwap(ArrayList<Integer> arr, int a, int b, int start) {
        int temp = arr.get(a);
        arr.set(a, arr.get(b));
        arr.set(b, temp);
        Highlights.markArray(0, start+a);
        Highlights.markArray(1, start+b);
        Writes.changeAuxWrites(2);
        Delays.sleep(1);
        Highlights.clearAllMarks();
    }
    
    public void arrayListWrite(ArrayList<Integer> arr, int at, int value, int pos) {
        arr.set(at, value);
        Writes.changeAuxWrites(1);
        Highlights.markArray(1, pos+at);
        Delays.sleep(1);
        Highlights.clearAllMarks();
    }

    public void arrayListReversal(ArrayList<Integer> array, int start, int length, int pos) {
        Writes.changeReversals(1);
        
        for(int i = start; i < start + ((length - start + 1) / 2); i++) {
            this.arrayListSwap(array, i, start + length - i, pos);
        }
    }

    private void mergeUp(ArrayList<Integer> array, int leftStart, int rightStart, int end, int[] copied, int start) {
        for (int i = 0; i < rightStart - leftStart; i++) {
            Highlights.markArray(1, i + leftStart);
            Writes.write(copied, i, array.get(i + leftStart), 1, false, false);
        }

        int left = leftStart;
        int right = rightStart;
        for(int nxt = 0; nxt < end - leftStart; nxt++){
            if(left >= rightStart && right >= end) break;

            Highlights.markArray(1, nxt + leftStart);
            Highlights.markArray(2, right);

            if(left < rightStart && right >= end){
                Highlights.clearMark(2);
                this.arrayListWrite(array, nxt + leftStart, copied[(left++) - leftStart], start+leftStart);
            }
            else if(left >= rightStart && right < end){
                Highlights.clearMark(1);
                break;
            }
            else if(Reads.compareValues(copied[left - leftStart], array.get(right)) <= 0){
                this.arrayListWrite(array, nxt + leftStart, copied[(left++) - leftStart], start+leftStart);
            }
            else {
                this.arrayListWrite(array, nxt + leftStart, array.get(right++), start+leftStart);
            }
        }

        Highlights.clearAllMarks();

    }

    private void mergeDown(ArrayList<Integer> array, int leftStart, int rightStart, int end, int[] copied, int start) {
        for (int i = 0; i < end - rightStart; i++) {
            Highlights.markArray(1, i + rightStart);
            Writes.write(copied, i, array.get(i + rightStart), 1, false, false);
        }

        int left = rightStart - 1;
        int right = end;
        for (int nxt = end - leftStart - 1; nxt >= 0; nxt--) {
            if (left <= leftStart && right <= rightStart) break;

            Highlights.markArray(1, leftStart + nxt);
            Highlights.markArray(2, (int)Math.max(left, 0));

            if (left < leftStart && right >= leftStart) {
                Highlights.clearMark(2);
                this.arrayListWrite(array, leftStart + nxt, copied[(right--) - rightStart - 1], start+leftStart);
            }
            else if ((left >= leftStart && right < leftStart) || right < rightStart + 1) {
                Highlights.clearMark(1);
                break;
            }
            else if (Reads.compareValues(array.get(left), copied[right - rightStart - 1]) <= 0) {
                this.arrayListWrite(array, leftStart + nxt, copied[(right--) - rightStart - 1], start+leftStart);
            }
            else {
                this.arrayListWrite(array, leftStart + nxt, array.get(left--), start+leftStart);
            }
        }

        Highlights.clearAllMarks();
    }

    private void merge(ArrayList<Integer> array, int leftStart, int rightStart, int end, int[] aux, int start) {
        if (end - rightStart < rightStart - leftStart) {
            mergeDown(array, leftStart, rightStart, end, aux, start);
        } else {
            mergeUp(array, leftStart, rightStart, end, aux, start);
        }
    }

    private boolean compare(int a, int b) {
        return Reads.compareValues(a, b) <= 0;
    }

    private int identifyRun(ArrayList<Integer> array, int index, int maxIndex, int start) {
        int startIndex = index;

        Highlights.markArray(1, start+index);
        if (index >= maxIndex) {
            return -1;
        }

        boolean cmp = compare(array.get(index), array.get(index + 1));
        index++;
        Highlights.markArray(1, start+index);
        
        while (index < maxIndex) {
            Delays.sleep(1);
            boolean checkCmp = compare(array.get(index), array.get(index + 1));
            if (checkCmp != cmp) {
                break;
            }
            index++;
            Highlights.markArray(1, start+index);
        }
        Delays.sleep(1);
        if (cmp == false) {
            this.arrayListReversal(array, startIndex, index, start);
        }
        if (index >= maxIndex) {
            return -1;
        }
        return index + 1;
    }

    private ArrayList<Integer> findRuns(ArrayList<Integer> array, int maxIndex, int start) {
        ArrayList<Integer> runs = new ArrayList<>();

        int lastRun = 0;
        while (lastRun != -1) {
            Writes.arrayListAdd(runs, lastRun);
            Writes.mockWrite(runs.size(), runs.size() - 1, lastRun, 0);
            lastRun = identifyRun(array, lastRun, maxIndex, start);
        }

        return runs;
    }

    private void pdMergeSort(ArrayList<Integer> array, int length, int start, int[] aux) {
        ArrayList<Integer> runs = findRuns(array, length - 1, start);
        
        while (runs.size() > 1) {
            for (int i = 0; i < runs.size() - 1; i += 2) {
                int end = i + 2 >= runs.size() ? length : (runs.get(i + 2));
                merge(array, runs.get(i), runs.get(i + 1), end, aux, start);
            }
            for (int i = 1; i < runs.size(); i++) {
                Writes.arrayListRemoveAt(runs, i);
            }
        }

        Writes.deleteArrayList(runs);
    }

    public void insertionSort(ArrayList<Integer> arr, int a, int b, int start) {
        for (int i = a + 1; i < b; i++) {
            int key = arr.get(i);
            int j = i-1;
            
            while (j >= a && Reads.compareValues(key, arr.get(j)) < 0) {
                Highlights.markArray(0, start+j);
                this.arrayListWrite(arr, j+1, arr.get(j), start);
                j--;
            }
            this.arrayListWrite(arr, j+1, key, start);
        }
        Highlights.clearAllMarks();
    }

    public void sortSubList(ArrayList<Integer> subList, int start, int[] mainArray) {
        int l = subList.size();
        if (l > 1) {
            if (l <= 16) {
                this.insertionSort(subList, 0, l, start);
            } else {
                this.pdMergeSort(subList, l, start, mainArray);
            }
        }
    }

    public void featureSort(int[] array, int currentLength) {
        double max = Reads.analyzeMax(array, currentLength, 0.25, true);
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] pos = new ArrayList[currentLength];
        double posConstant = max / (currentLength + 4);
        for (int i = 0; i < currentLength; i++) {
            pos[i] = new ArrayList<>();
        }
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(0, i);
            int idx = (int)(array[i] * posConstant);
            Writes.arrayListAdd(pos[idx], array[i], true, 1);
            Writes.changeAuxWrites(1);
        }
        Highlights.clearAllMarks();
        for (int i = 0; i < pos.length; i++) {
            Highlights.markArray(0, i);
            Delays.sleep(1);
            this.sortSubList(pos[i], i, array);
            Highlights.clearAllMarks();
        }
        Highlights.clearAllMarks();
        Writes.transcribe(array, pos, 0, true, false);
        Writes.deleteExternalArray(pos);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.featureSort(array, currentLength);
    }
}
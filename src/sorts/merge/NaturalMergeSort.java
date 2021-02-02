package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class NaturalMergeSort extends Sort {
    int[] merged;

    public NaturalMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Merge");
        this.setRunAllSortsName("Natural Merge Sort");
        this.setRunSortName("Natural Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void merge(int[] arr, int left, int right, int stop) {
        boolean first = true;
        int index = 0;
        int start = 0;
        int origRight = right;
        int origLeft = left;
        while (left < origRight && right < stop)
            if (Reads.compareIndices(arr, left, right, 1, true) == 1) {
                first = false;
                Writes.write(merged, index++, arr[right++], 0, false, true);
            } else
                if (first) {
                    start++;
                    index++;
                    left++;
                } else
                    Writes.write(merged, index++, arr[left++], 0, false, true);
        while (left < origRight)
            Writes.write(merged, index++, arr[left++], 0, false, true);
        Highlights.clearMark(2);
        for (int i = start; i < index; i++)
            Writes.write(arr, i + origLeft, merged[i], 1, true, false);
    }

    @Override
    public void runSort(int[] arr, int length, int bucketCount) {
        merged = Writes.createExternalArray(length);
        boolean done = false;
        int start = 0;
        int stop = length - 1;
        while (!done) {
            int prev = 0;
            int left = -1;
            done = true;
            for (int i = start; i < stop; i++)
                if (Reads.compareIndices(arr, i, i + 1, 1, true) == 1) {
                    if (left == -1) {
                        left = prev;
                        prev = i + 1;
                    } else {
                        merge(arr, left, prev, i + 1);
                        if (done)
                            start = i;
                        prev = i + 1;
                        left = -1;
                        done = false;
                    }
                }
            if (left != -1) {
                merge(arr, left, prev, length);
                done = false;
                stop = left;
            }
        }
        Writes.deleteExternalArray(merged);
    }
}
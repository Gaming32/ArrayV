package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(name = "Bottom-Up Merge")
public final class BottomUpMergeSort extends Sort {
    private int[] scratchArray;
    private int copyLength;

    public BottomUpMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void merge(int[] array, int currentLength, int index, int mergeSize) {
        int left = index;
        int mid = left + (mergeSize / 2);
        int right = mid;
        int end = Math.min(currentLength, index + mergeSize);

        int scratchIndex = left;

        if (right < end) {
            while (left < mid && right < end) {
                this.Highlights.markArray(1, left);
                this.Highlights.markArray(2, right);
                this.Delays.sleep(1);

                if (this.Reads.compareValues(array[left], array[right]) <= 0) {
                    this.Writes.write(this.scratchArray, scratchIndex++, array[left++], 0, false, true);
                } else {
                    this.Writes.write(this.scratchArray, scratchIndex++, array[right++], 0, false, true);
                }
            }
            if (left < mid) {
                while (left < mid) {
                    this.Highlights.markArray(1, left);
                    this.Delays.sleep(1);

                    this.Writes.write(this.scratchArray, scratchIndex++, array[left++], 0, false, true);
                }
            }
            if (right < end) {
                while (right < end) {
                    this.Highlights.markArray(2, right);
                    this.Delays.sleep(1);

                    this.Writes.write(this.scratchArray, scratchIndex++, array[right++], 0, false, true);
                }
            }
        } else {
            this.copyLength = left;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.scratchArray = Writes.createExternalArray(currentLength);
        int mergeSize = 2;

        while (mergeSize <= currentLength) {
            this.copyLength = currentLength;

            for (int i = 0; i < currentLength; i += mergeSize) {
                this.merge(array, currentLength, i, mergeSize);
            }

            this.Highlights.clearMark(2);

            for (int i = 0; i < this.copyLength; i++) {
                this.Writes.write(array, i, this.scratchArray[i], 1, true, false);
            }

            mergeSize *= 2;
        }
        if ((mergeSize / 2) != currentLength) {
            this.merge(array, currentLength, 0, mergeSize);

            this.Highlights.clearMark(2);

            for (int i = 0; i < currentLength; i++) {
                this.Writes.write(array, i, this.scratchArray[i], 1, true, false);
            }
        }

        Writes.deleteExternalArray(this.scratchArray);
    }
}

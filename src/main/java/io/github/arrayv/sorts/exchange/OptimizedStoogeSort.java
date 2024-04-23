package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

// Code refactored from: https://www.ijitee.org/wp-content/uploads/papers/v8i12/L31671081219.pdf
// Written by Professors Amit Kishor and Pankaj Pratap Singh
@SortMeta(listName = "Optimized Stooge (Kishor-Singh)", runName = "Optimized Stooge Sort (Kishor-Singh)")
public final class OptimizedStoogeSort extends Sort {
    public OptimizedStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void forward(int[] array, int left, int right) {
        while (left < right) {
            int index = right;

            while (left < index) {
                Highlights.markArray(1, left);
                Highlights.markArray(2, index);
                Delays.sleep(0.05);

                if (Reads.compareValues(array[left], array[index]) > 0) {
                    Writes.swap(array, left, index, 0.075, true, false);
                }
                left++;
                index--;
            }

            left = 0;
            right--;
        }
    }

    private void backward(int[] array, int left, int right) {
        int length = right;

        while (left < right) {
            int index = left;

            while (index < right) {
                Highlights.markArray(1, index);
                Highlights.markArray(2, right);
                Delays.sleep(0.05);

                if (Reads.compareValues(array[index], array[right]) > 0) {
                    Writes.swap(array, index, right, 0.075, true, false);
                }
                index++;
                right--;
            }

            left++;
            right = length;
        }
    }

    private void exchange(int[] array, int length) {
        int left = 0;
        int right = length - 1;

        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.05);

            if (Reads.compareValues(array[left], array[right]) > 0) {
                Writes.swap(array, left, right, 0.075, true, false);
            }
            left++;
            right--;
        }

        this.forward(array, 0, length - 2);
        this.backward(array, 1, length - 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.exchange(array, sortLength);
    }
}

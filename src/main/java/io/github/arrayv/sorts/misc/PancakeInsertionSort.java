package io.github.arrayv.sorts.misc;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Pancake Insertion"
)
public final class PancakeInsertionSort extends Sort {
    public PancakeInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int monoboundFw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareValueIndex(array, value, end - mid, 0.5, true) <= 0) {
                end -= mid;
            }
            top -= mid;
        }

        if (Reads.compareValueIndex(array, value, end - 1, 0.5, true) <= 0) {
            return end - 1;
        }
        return end;
    }

    private int monoboundBw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareIndexValue(array, start + mid, value, 0.5, true) > 0) {
                start += mid;
            }
            top -= mid;
        }

        if (Reads.compareIndexValue(array, start, value, 0.5, true) > 0) {
            return start + 1;
        }
        return start;
    }

    private int compare(int[] array, int a, int b) {
        return Reads.compareIndices(array, a, b, 1, true);
    }

    private void flip(int[] array, int i) {
        Writes.reversal(array, 0, i, 0.01, true, false);
    }

    private boolean front(int[] array, int length) {
        if (length < 2) {
            return false;
        }
        boolean dir = true;
        if (compare(array, 0, 1) > 0) { // compare the first two elements
            flip(array, 1);
        }
        if (length > 2) {
            if (compare(array, 1, 2) > 0) {
                if (compare(array, 0, 2) > 0) {
                    flip(array, 1);
                    return false;
                } else {
                    flip(array, 2);
                    flip(array, 1);
                }
                return false;
            } else {
                return true;
            }
        }
        return dir;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean dir = front(array, currentLength); // Sort the first three items with a decision tree

        for (int i = 3; i < currentLength; i++) {
            if (dir) {
                if (compare(array, i - 1, i) <= 0) {
                    continue;
                } else if (compare(array, 0, i) > 0) {
                    flip(array, i - 1);
                    dir = !dir;
                } else {
                    int idx = monoboundFw(array, 0, i, array[i]);
                    flip(array, i);
                    int end = i - idx;
                    flip(array, end);
                    flip(array, end - 1);
                    dir = !dir;
                }
            } else {
                if (compare(array, i - 1, i) > 0) {
                    continue;
                } else if (compare(array, 0, i) <= 0) {
                    flip(array, i - 1);
                    dir = !dir;
                } else {
                    int idx = monoboundBw(array, 0, i, array[i]);
                    flip(array, i);
                    int end = i - idx;
                    flip(array, end);
                    flip(array, end - 1);
                    dir = !dir;
                }
            }
        }

        if (!dir) { // The array is reversed
            flip(array, currentLength - 1);
        }
    }
}

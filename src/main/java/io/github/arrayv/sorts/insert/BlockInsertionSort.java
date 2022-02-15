package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.GrailSorting;
import io.github.arrayv.utils.Rotations;

public final class BlockInsertionSort extends GrailSorting {
    public BlockInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Block Insertion");
        this.setRunAllSortsName("Block Insertion Sort");
        this.setRunSortName("Block Insertsort");
        this.setCategory("Insertion Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.holyGriesMills(array, pos, lenA, lenB, 1, true, false);
    }

    private void insert1(int[] array, int a, int l) {
        int tmp = array[l--];
        while (l >= a && Reads.compareValues(array[l], tmp) > 0) {
            Writes.write(array, l + 1, array[l], 1, true, false);
            l--;
        }
        Writes.write(array, l + 1, tmp, 1, true, false);
    }

    private void insert2(int[] array, int a, int l, int r) {
        int tmpL = array[l--];
        int tmpR = array[r];
        while (l >= a && Reads.compareValues(array[l], tmpR) > 0) {
            Writes.write(array, l + 2, array[l], 1, true, false);
            l--;
        }
        Writes.write(array, l + 2, tmpR, 1, true, false);
        while (l >= a && Reads.compareValues(array[l], tmpL) > 0) {
            Writes.write(array, l + 1, array[l], 1, true, false);
            l--;
        }
        Writes.write(array, l + 1, tmpL, 1, true, false);
    }

    private int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b)
            return i;
        if(Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1) i++;
            Writes.reversal(array, a, i - 1, 1, true, false);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    public void insertionSort(int[] array, int a, int b) {
        int i, j, len;
        i = findRun(array, a, b);
        while (i < b) {
            j = findRun(array, i, b);
            len = j - i;
            if (len < 3) {
                if (len == 2) {
                    insert2(array, a, i, i + 1);
                } else {
                    insert1(array, a, i);
                }
            } else {
                grailMergeWithoutBuffer(array, a, i - a, len);
            }
            i = j;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength);
    }
}

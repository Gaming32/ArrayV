package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
Idea made by Control#2866 in The Studio Discord Server (https://discord.com/invite/2xGkKC2)
*/

@SortMeta(
    name = "Matrix",
    runName = "Matrix Sort"
)
public final class MatrixSort extends Sort {
    private class MatrixShape {
        int width;
        boolean unbalanced, insertLast;

        public MatrixShape(int width, int height, boolean insertLast) {
            this.width = width;
            this.unbalanced = (width == 1) ^ (height == 1);
            this.insertLast = this.unbalanced || insertLast;
        }
    }

    public MatrixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void gapReverse(int[] array, int start, int end, int gap) {
        Writes.changeReversals(1);

        for (int i = start, j = end; i < j; i += gap, j -= gap) {
            Writes.swap(array, i, j - gap, 0.5, true, false);
        }

        Highlights.clearMark(2);
    }

    private int dirCompareVal(int left, int right, boolean dir) {
        int res = Reads.compareValues(left, right);
        return dir ? res : (res * -1);
    }

    private boolean insertLast(int[] array, int a, int b, int gap, boolean dir) {
        boolean did = false;
        int key = array[b];
        int j = b - gap;

        while (j >= a && dirCompareVal(key, array[j], dir) < 0) {
            Writes.write(array, j + gap, array[j], 0.5, true, false);
            did = true;
            j -= gap;
        }
        Writes.write(array, j + gap, key, 0.5, true, false);

        return did;
    }

    private MatrixShape getMatrixDims(int len) {
        int dim = (int)Math.sqrt(len);
        boolean insertLast = false;
        if (dim * dim == len - 1)
            insertLast = true;
        for (; len % dim != 0; dim--);
        return new MatrixShape(dim, len / dim, insertLast);
    }

    private boolean matrixSort(int[] array, int start, int end, int gap, boolean dir) {
        boolean did = false;
        int length = (end - start) / gap;
        if (length < 2)
            return false;
        else if (length <= 16) {
            did = false;
            for (int i = start; i < end; i += gap)
                did = insertLast(array, start, i, gap, dir) | did;
        }
        else {
            boolean newdid;
            MatrixShape matShape = getMatrixDims(length);
            if (matShape.insertLast) {
                boolean did1 = matrixSort(array, start, end - gap, gap, dir);
                boolean did2 = insertLast(array, start, end - gap, gap, dir);
                return did1 || did2;
            }
            for (int i = start + matShape.width * gap; i < end; i += 2 * matShape.width * gap) {
                gapReverse(array, i, i + matShape.width * gap, gap);
            }
            did = false;
            do {
                newdid = false;

                boolean curdir = dir;
                for (int i = start; i < end; i += matShape.width * gap) {
                    newdid = matrixSort(array, i, i + matShape.width * gap, gap, curdir) || newdid;
                    did = did || newdid;
                    curdir = !curdir;
                }

                newdid = false;

                for (int i = 0; i < matShape.width; i++) {
                    newdid = matrixSort(array, start + i * gap, end + i * gap, gap * matShape.width, dir) || newdid;
                    did = did || newdid;
                }
            } while (newdid);
            for (int i = start + matShape.width * gap; i < end; i += 2 * matShape.width * gap) {
                gapReverse(array, i, i + matShape.width * gap, gap);
            }
        }
        return did;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.matrixSort(array, 0, sortLength, 1, true);
    }
}

package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Optimized Rotate Merge",
    category = "Hybrid Sorts",
    question = "How many items should the temp array contain? (must be a power of two) (default: 64)",
    defaultAnswer = 64
)
public final class OptimizedRotateMergeSort extends Sort {
    final int MIN_RUN = 32;

    int[] tmp;

    public OptimizedRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected void rotateInPlace(int[] array, int pos, int lenA, int lenB) {
        if (lenA < 1 || lenB < 1) return;

        int a = pos,
            b = pos + lenA - 1,
            c = pos + lenA,
            d = pos + lenA + lenB - 1;
        int tmp;

        while (a < b && c < d) {
            tmp = array[b];
            Writes.write(array, b--, array[a], 0.5, true, false);
            Writes.write(array, a++, array[c], 0.5, true, false);
            Writes.write(array, c++, array[d], 0.5, true, false);
            Writes.write(array, d--, tmp,     0.5, true, false);
        }
        while (a < b) {
            tmp = array[b];
            Writes.write(array, b--, array[a], 0.5, true, false);
            Writes.write(array, a++, array[d], 0.5, true, false);
            Writes.write(array, d--, tmp,     0.5, true, false);
        }
        while (c < d) {
            tmp = array[c];
            Writes.write(array, c++, array[d], 0.5, true, false);
            Writes.write(array, d--, array[a], 0.5, true, false);
            Writes.write(array, a++, tmp,     0.5, true, false);
        }
        if (a < d) { //dont count reversals that dont do anything
            Writes.reversal(array, a, d, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void rotate(int[] array, int pos, int left, int right) {
        if (left < 1 || right < 1) return;

        int pta = pos, ptb = pos + left, ptc = pos + right, ptd = ptb + right;

        if (left < right) {
            int bridge = right - left;

            if (bridge < left) {
                int loop = left;

                if (bridge > tmp.length) {
                    rotateInPlace(array, pos, left, right);
                    return;
                }

                Writes.arraycopy(array, ptb, tmp, 0, bridge, 1, true, true);

                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], 0.5, true, false);
                    Writes.write(array,   ptd, array[--ptb], 0.5, true, false);
                }
                Writes.arraycopy(tmp, 0, array, pta, bridge, 1, true, false);
            } else {
                if (left > tmp.length) {
                    rotateInPlace(array, pos, left, right);
                    return;
                }

                Writes.arraycopy(array, pta, tmp, 0, left, 1, true, true);
                Writes.arraycopy(array, ptb, array, pta, right, 1, true, false);
                Writes.arraycopy(tmp, 0, array, ptc, left, 1, true, false);
            }
        } else if(right < left) {
            int bridge = left - right;

            if (bridge < right) {
                if (bridge > tmp.length) {
                    rotateInPlace(array, pos, left, right);
                    return;
                }

                int loop = right;

                Writes.arraycopy(array, ptc, tmp, 0, bridge, 1, true, true);

                while(loop-- > 0) {
                    Writes.write(array, ptc++, array[pta],   0.5, true, false);
                    Writes.write(array, pta++, array[ptb++], 0.5, true, false);
                }
                Writes.arraycopy(tmp, 0, array, ptd - bridge, bridge, 1, true, false);
            } else {
                if (right > tmp.length) {
                    rotateInPlace(array, pos, left, right);
                    return;
                }

                Writes.arraycopy(array, ptb, tmp, 0, right, 1, true, true);
                while (left-- > 0)
                    Writes.write(array, --ptd, array[--ptb], 1, true, false);
                Writes.arraycopy(tmp, 0, array, pta, right, 1, true, false);
            }
        }
        else {
            while (left-- > 0)
                Writes.swap(array, pta++, ptb++, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void mergeUp(int[] array, int start, int mid, int end) {
        for (int i = 0; i < mid - start; i++) {
            Highlights.markArray(1, i + start);
            Writes.write(tmp, i, array[i + start], 1, false, true);
        }

        int bufferPointer = 0;
        int left = start;
        int right = mid;

        while (left < right && right < end) {
            Highlights.markArray(2, right);
            if (Reads.compareValues(tmp[bufferPointer], array[right]) <= 0)
                Writes.write(array, left++, tmp[bufferPointer++], 1, true, false);
            else
                Writes.write(array, left++, array[right++], 1, true, false);
        }
        Highlights.clearMark(2);

        while (left < right)
            Writes.write(array, left++, tmp[bufferPointer++], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    protected void mergeDown(int[] array, int start, int mid, int end) {
        for (int i = 0; i < end - mid; i++) {
            Highlights.markArray(1, i + mid);
            Writes.write(tmp, i, array[i + mid], 1, false, true);
        }

        int bufferPointer = end - mid - 1;
        int left = mid - 1;
        int right = end - 1;

        while (right > left && left >= start) {
            Highlights.markArray(2, left);
            if (Reads.compareValues(tmp[bufferPointer], array[left]) >= 0)
                Writes.write(array, right--, tmp[bufferPointer--], 1, true, false);
            else
                Writes.write(array, right--, array[left--], 1, true, false);
        }
        Highlights.clearMark(2);

        while (right > left)
            Writes.write(array, right--, tmp[bufferPointer--], 0.5, true, false);
        Highlights.clearAllMarks();
    }

    private int monoboundLeft(int[] array, int start, int end, int value) {
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

    private int monoboundRight(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareIndexValue(array, start + mid, value, 0.5, true) <= 0) {
                start += mid;
            }
            top -= mid;
        }

        if (Reads.compareIndexValue(array, start, value, 0.5, true) <= 0) {
            return start + 1;
        }
        return start;
    }

    private int leftExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValueIndex(array, val, a - 1 + i, 0.5, true) >= 0) i *= 2;

        return this.monoboundRight(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValueIndex(array, val, b - i, 0.5, true) <= 0) i *= 2;

        return this.monoboundLeft(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected void merge(int[] array, int start, int mid, int end) {
        if (start >= mid) return;
        end = rightExpSearch(array, mid, end, array[mid - 1]);
        if (end < mid) return;
        start = leftExpSearch(array, start, mid, array[mid]);
        if (Reads.compareIndices(array, start, end - 1, 1, true) > 0) {
            rotate(array, start, mid - start, end - mid);
            return;
        }
        int llen = mid - start, rlen = end - mid;
        if (((llen < rlen) ? llen : rlen) > tmp.length) {
            int m1, m2, m3;
            if (mid - start >= end - mid) {
                m1 = start + (mid - start) / 2;
                m2 = monoboundLeft(array, mid, end, array[m1]);
                m3 = m1 + (m2 - mid);
            } else {
                m2 = mid + (end - mid) / 2;
                m1 = monoboundRight(array, start, mid, array[m2]);
                m3 = (m2++) - (mid - m1);
            }
            rotate(array, m1, mid - m1, m2 - mid);
            merge(array, m3 + 1, m2, end);
            merge(array, start, m1, m3);
        } else {
            if (end - mid < mid - start) {
                mergeDown(array, start, mid, end);
            } else {
                mergeUp(array, start, mid, end);
            }
        }
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if (Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while (i < b) {
            int dest = monoboundRight(array, a, i, array[i]);
            int tmp = array[i];
            Writes.arraycopy(array, dest, array, dest + 1, i - dest, 0.5, true, false);
            Writes.write(array, dest, tmp, 0.5, true, false);
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int tempLen) {
        this.tmp = Writes.createExternalArray(tempLen);

        int i;
        for (i = 0; i + MIN_RUN < currentLength; i += MIN_RUN) {
            insertionSort(array, i, i + MIN_RUN, 0.5, false);
        }
        if (i + 1 < currentLength) {
            insertionSort(array, i, currentLength, 0.5, false);
        }

        int gap, fullMerge;
        for (gap = MIN_RUN; gap < currentLength; gap = fullMerge) {
            fullMerge = gap * 2;
            for (i = 0; i + fullMerge < currentLength; i += fullMerge) {
                merge(array, i, i + gap, i + fullMerge);
            }
            if (i + gap < currentLength) {
                merge(array, i, i + gap, currentLength);
            }
        }

        Writes.deleteExternalArray(this.tmp);
    }
}

package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;

/*
MIT License

Copyright (c) 2021 Josiah (Gaming32) Glosson

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

public final class FifthMergeSort extends Sort {
    protected final class IndexPair {
        public int aEnd, bEnd;

        public IndexPair(int aEnd, int bEnd) {
            this.aEnd = aEnd;
            this.bEnd = bEnd;
        }
    }

    BinaryInsertionSort inserter;

    public FifthMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fifth Merge");
        this.setRunAllSortsName("Fifth Merge Sort");
        this.setRunSortName("Fifth Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void mergeInPlaceForwards(int[] array, int buffer, int start, int mid, int end) {
        int left = start, right = mid;
        while (left < mid && right < end) {
            if (Reads.compareIndices(array, left, right, 0.5, true) <= 0) {
                Writes.write(array, buffer++, array[left++], 0.5, false, false);
                Highlights.markArray(3, buffer);
            } else {
                Writes.write(array, buffer++, array[right++], 0.5, false, false);
                Highlights.markArray(3, buffer);
            }
        }
        Highlights.clearAllMarks();

        while (left < mid) {
            Writes.write(array, buffer++, array[left++], 0.5, true, false);
        }
        while (right < end) {
            Writes.write(array, buffer++, array[right++], 0.5, true, false);
        }
    }

    protected IndexPair mergeInPlaceBackwards(int[] array, int buffer, int bufferLen, int mid, int end) {
        int left = mid - 1, right = end - 1;
        while (buffer > right && right >= mid) {
            if (Reads.compareIndices(array, left, right, 0.5, true) > 0) {
                Highlights.markArray(3, buffer);
                Writes.write(array, buffer--, array[left--], 0.5, false, false);
            } else {
                Highlights.markArray(3, buffer);
                Writes.write(array, buffer--, array[right--], 0.5, false, false);
            }
        }
        Highlights.clearAllMarks();
        if (right == left) {
            while (right >= 0) {
                Writes.write(array, buffer--, array[right--], 0.5, true, false);
            }
        } else if (right < mid) {
            while (left >= 0) {
                Writes.write(array, buffer--, array[left--], 0.5, true, false);
            }
        }
        return new IndexPair(left + 1, right + 1);
    }

    protected void mergeForwardsWithBuffer(int[] array, int[] buffer, int dest, int left, int leftEnd, int mid, int end) {
        int right = mid;
        while (left < leftEnd && right < end) {
            Highlights.markArray(2, left);
            Highlights.markArray(3, right);
            Delays.sleep(0.5);
            if (Reads.compareValueIndex(array, buffer[left], right, 0, false) <= 0) {
                Writes.write(array, dest++, buffer[left++], 0.5, true, false);
            } else {
                Writes.write(array, dest++, array[right++], 0.5, true, false);
            }
        }
        Highlights.clearMark(3);

        while (left < leftEnd) {
            Highlights.markArray(2, left);
            Writes.write(array, dest++, buffer[left++], 0.5, true, false);
        }
    }

    protected void merge(int[] array, int[] buffer, int chunkOffset, int start, int mid, int end, boolean fromBuffer) {
        int[] from, to;
        int writepos;
        if (fromBuffer) {
            from = buffer;
            to = array;
            writepos = start;
            start -= chunkOffset;
            mid -= chunkOffset;
            end -= chunkOffset;
        } else {
            from = array;
            to = buffer;
            writepos = start - chunkOffset;
        }

        int left = start, right = mid;
        while (left < mid && right < end) {
            Delays.sleep(0.5);
            if (Reads.compareIndices(from, left, right, 0, !fromBuffer) <= 0) {
                Writes.write(to, writepos++, from[left++], 0.5, fromBuffer, !fromBuffer);
            } else {
                Writes.write(to, writepos++, from[right++], 0.5, fromBuffer, !fromBuffer);
            }
        }
        Highlights.clearMark(2);

        while (left < mid) {
            Writes.write(to, writepos++, from[left++], 0.5, true, !fromBuffer);
        }
        while (right < end) {
            Writes.write(to, writepos++, from[right++], 0.5, true, !fromBuffer);
        }
    }

    protected void pingPong(int[] array, int[] buffer, int start, int end) {
        int i;
        for (i = start; i + 8 < end; i += 8) {
            inserter.customBinaryInsert(array, i, i + 8, 0.5);
        }
        if (end - i > 1) {
            inserter.customBinaryInsert(array, i, end, 0.5);
        }

        int length = end - start;
        boolean fromBuffer = false;
        for (int gap = 8; gap < length; gap *= 2) {
            int fullMerge = gap * 2;
            for (i = start; i + fullMerge < end; i += fullMerge) {
                merge(array, buffer, start, i, i + gap, i + fullMerge, fromBuffer);
            }
            if (i + gap < end) {
                merge(array, buffer, start, i, i + gap, end, fromBuffer);
            } else {
                if (fromBuffer) {
                    Writes.arraycopy(buffer, i - start, array, i, end - i, 0.5, true, false);
                } else {
                    Writes.arraycopy(array, i, buffer, i - start, end - i, 0.5, true, false);
                }
            }
            fromBuffer = !fromBuffer;
        }
        if (fromBuffer) {
            Writes.arraycopy(buffer, 0, array, start, length, 0.5, true, false);
        }
    }

	public void fifthMergeSort(int[] array, int currentLength) {
        inserter = new BinaryInsertionSort(arrayVisualizer);

        int fifthLen = currentLength / 5;
        int bufferLen = currentLength - fifthLen * 4;
        int[] buffer = Writes.createExternalArray(bufferLen);

        pingPong(array, buffer, 0, bufferLen);
        for (int i = 0, start = bufferLen; i < 4; i++, start += fifthLen) {
            pingPong(array, buffer, start, start + fifthLen);
        }

        Writes.arraycopy(array, 0, buffer, 0, bufferLen, 0.5, true, true);
        int twoFifths = 2 * fifthLen;
        for (int i = 0, start = bufferLen; i < 2; i++, start += twoFifths) {
            mergeInPlaceForwards(array, start - bufferLen, start, start + fifthLen, start + twoFifths);
        }

        IndexPair finalMerge = mergeInPlaceBackwards(array, currentLength - 1, bufferLen, twoFifths, 2 * twoFifths);
        if (finalMerge.bEnd > 0) {
            mergeForwardsWithBuffer(array, array, bufferLen, 0, finalMerge.aEnd, twoFifths, currentLength);
        }

        mergeForwardsWithBuffer(array, buffer, 0, 0, bufferLen, bufferLen, currentLength);

        Writes.deleteExternalArray(buffer);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		fifthMergeSort(array, currentLength);
    }
}

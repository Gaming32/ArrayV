package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;

/*
 *
MIT License

Copyright (c) 2021 EmeraldBlock

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

/*
 * Implements https://www.sciencedirect.com/science/article/pii/S1877050910005478.
 *
 * The shuffle algorithm is at https://arxiv.org/abs/0805.1598.
 * Note that the unshuffle algorithm is not the shuffle algorithm in reverse,
 * but rather, it is a variation of the shuffle algorithm.
 *
 * See also a proof of the time complexity at https://arxiv.org/abs/1508.00292.
 * The implementation is based on the pseudocode found in this.
 */
@SortMeta(name = "New Shuffle Merge")
public class NewShuffleMergeSort extends IterativeTopDownMergeSort {

    public NewShuffleMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void rotateEqual(int[] array, int a, int b, int size, double sleep) {
        for (int i = 0; i < size; ++i)
            Writes.swap(array, a + i, b + i, sleep, true, false);
    }

    private void rotate(int[] array, int mid, int a, int b, double sleep) {
        while (a > 0 && b > 0) {
            if (a > b) {
                rotateEqual(array, mid - b, mid, b, sleep);
                mid -= b;
                a -= b;
            } else {
                rotateEqual(array, mid - a, mid, a, sleep);
                mid += a;
                b -= a;
            }
        }
    }

    private void shuffleEasy(int[] array, int start, int size) {
        Highlights.clearMark(2);
        for (int i = 1; i < size; i *= 3) {
            int val = array[start + i - 1];
            for (int j = i * 2 % size; j != i; j = j * 2 % size) {
                int nval = array[start + j - 1];
                Writes.write(array, start + j - 1, val, 1, true, false);
                val = nval;
            }
            Writes.write(array, start + i - 1, val, 1, true, false);
        }
    }

    private void shuffle(int[] array, int start, int end) {
        while (end - start > 1) {
            int n = (end - start) / 2;
            int l = 1;
            while (l * 3 - 1 <= 2 * n)
                l *= 3;
            int m = (l - 1) / 2;

            rotate(array, start + n, n - m, m, 1);
            shuffleEasy(array, start, l);
            start += l - 1;
        }
    }

    private void rotateShuffledEqual(int[] array, int a, int b, int size) {
        for (int i = 0; i < size; i += 2)
            Writes.swap(array, a + i, b + i, 0.25, true, false);
    }

    private void rotateShuffled(int[] array, int mid, int a, int b) {
        while (a > 0 && b > 0) {
            if (a > b) {
                rotateShuffledEqual(array, mid - b, mid, b);
                mid -= b;
                a -= b;
            } else {
                rotateShuffledEqual(array, mid - a, mid, a);
                mid += a;
                b -= a;
            }
        }
    }

    private void rotateShuffledOuter(int[] array, int mid, int a, int b) {
        if (a > b) {
            rotateShuffledEqual(array, mid - b, mid + 1, b);
            mid -= b;
            a -= b;
            rotateShuffled(array, mid, a, b);
        } else {
            rotateShuffledEqual(array, mid - a, mid + 1, a);
            mid += a + 1;
            b -= a;
            rotateShuffled(array, mid, a, b);
        }
    }

    private void unshuffleEasy(int[] array, int start, int size) {
        for (int i = 1; i < size; i *= 3) {
            int prev = i;
            int val = array[start + i - 1];
            for (int j = i * 2 % size; j != i; j = j * 2 % size) {
                Writes.write(array, start + prev - 1, array[start + j - 1], 0.25, true, false);
                prev = j;
            }
            Writes.write(array, start + prev - 1, val, 0.25, true, false);
        }
    }

    private void unshuffle(int[] array, int start, int end) {
        while (end - start > 1) {
            int n = (end - start) / 2;
            int l = 1;
            while (l * 3 - 1 <= 2 * n)
                l *= 3;
            int m = (l - 1) / 2;

            rotateShuffledOuter(array, start + 2 * m, 2 * m, 2 * n - 2 * m);
            unshuffleEasy(array, start, l);
            start += l - 1;
        }
    }

    private void mergeUp(int[] array, int start, int end, boolean type) {
        int i = start;
        int j = i + 1;
        while (j < end) {
            int cmp = Reads.compareIndices(array, i, j, 0, true);
            if (cmp == -1 || !type && cmp == 0) {
                ++i;
                if (i == j) {
                    ++j;
                    type = !type;
                }
            } else if (end - j == 1) {
                rotate(array, j, j - i, 1, 0.25);
                break;
            } else {
                int r = 0;
                if (type)
                    while (j + 2 * r < end && Reads.compareIndices(array, j + 2 * r, i, 0, true) != 1)
                        ++r;
                else
                    while (j + 2 * r < end && Reads.compareIndices(array, j + 2 * r, i, 0, true) == -1)
                        ++r;
                --j;
                unshuffle(array, j, j + 2 * r);
                rotate(array, j, j - i, r, 0.25);
                i += r + 1;
                j += 2 * r + 1;
            }
        }
    }

    @Override
    protected void merge(int[] array, int[] tmp, int start, int mid, int end) {
        if (mid - start <= end - mid) {
            shuffle(array, start, end);
            mergeUp(array, start, end, true);
        } else {
            shuffle(array, start + 1, end);
            mergeUp(array, start, end, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        mergeSort(array, null, length);
    }
}

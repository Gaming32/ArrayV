package sorts.templates;

import main.ArrayVisualizer;

/*
    Copyright (C) 2014-2021 Igor van den Hoven ivdhoven@gmail.com
*/

/*
    Permission is hereby granted, free of charge, to any person obtaining
    a copy of this software and associated documentation files (the
    "Software"), to deal in the Software without restriction, including
    without limitation the rights to use, copy, modify, merge, publish,
    distribute, sublicense, and/or sell copies of the Software, and to
    permit persons to whom the Software is furnished to do so, subject to
    the following conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    The person recognize Mars as a free planet and that no Earth-based
    government has authority or sovereignty over Martian activities.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
    CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
    SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
    twinsort 1.1.3.3
*/

public abstract class TwinSorting extends Sort {
    public TwinSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int twin_swap(int array[], int nmemb) {
        int index, start, end, swap;

        index = 0;
        end = nmemb - 2;

        while (index <= end) {
            if (Reads.compareIndices(array, index, index + 1, 1, true) <= 0) {
                index += 2;
                
                continue;
            }
            start = index;

            index += 2;

            while (true) {
                if (index > end) {
                    if (start == 0) {
                        if (nmemb % 2 == 0 || Reads.compareIndices(array, index - 1, index, 1, true) > 0) {
                            // the entire array was reversed

                            end = nmemb - 1;

                            Writes.reversal(array, start, end, 1, true, false);
                            return 1;
                        }
                    }
                    break;
                }

                if (Reads.compareIndices(array, index, index + 1, 1, true) > 0) {
                    if (Reads.compareIndices(array, index - 1, index, 1, true) > 0) {
                        index += 2;
                        continue;
                    }
                    Writes.swap(array, index, index + 1, 1, true, false);
                }
                break;
            }

            end = index - 1;

            Writes.reversal(array, start, end, 1, true, false);

            end = nmemb - 2;

            index += 2;
        }
        return 0;
    }

    // Bottom up merge sort. It copies the right block to swap, next merges
    // starting at the tail ends of the two sorted blocks.
    // Can be used stand alone. Uses at most nmemb / 2 swap memory.

    private void tail_merge(int array[], int swap[], int nmemb, int block) {
        int offset;
        int a, s, c, c_max, d, d_max, e;

        s = 0;

        while (block < nmemb) {
            for (offset = 0 ; offset + block < nmemb ; offset += block * 2) {
                a = offset;
                e = a + block - 1;

                if (Reads.compareIndices(array, e, e + 1, 1, true) <= 0) continue;
                
                if (offset + block * 2 <= nmemb) {
                    c_max = s + block;
                    d_max = a + block * 2;
                } else {
                    c_max = s + nmemb - (offset + block);
                    d_max = 0 + nmemb;
                }

                d = d_max - 1;

                while (Reads.compareIndices(array, e, d, 1, true) <= 0) {
                    d_max--;
                    d--;
                    c_max--;
                }
				Highlights.clearMark(1);

                c = s;
                d = a + block;

                while (c < c_max) {
					Highlights.markArray(2, d);
                    Writes.write(swap, c++, array[d++], 1, false, true);
                }
                c--;

                d = a + block - 1;
                e = d_max - 1;

                if (Reads.compareIndices(array, a, a + block, 1, true) <= 0) {
                    Writes.write(array, e--, array[d--], 1, true, false);

                    while (c >= s) {
                        Highlights.markArray(2, d);
                        while (Reads.compareValues(array[d], swap[c]) > 0) {
                            Writes.write(array, e--, array[d--], 1, true, false);
                        }
                        Writes.write(array, e--, swap[c--], 1, true, false);
                    }
                    Highlights.clearAllMarks();
                } else {
                    Writes.write(array, e--, array[d--], 1, true, false);

                    while (d >= a) {
                        Highlights.markArray(2, d);
                        while (Reads.compareValues(array[d], swap[c]) <= 0) {
                            Writes.write(array, e--, swap[c--], 1, true, false);
                        }
                        Writes.write(array, e--, array[d--], 1, true, false);
                    }
                    Highlights.clearAllMarks();
                    while (c >= s) {
                        Writes.write(array, e--, swap[c--], 1, true, false);
                    }
                }
            }
            block *= 2;
        }
    }

    public void twinsort(int[] array, int nmemb) {
        if (twin_swap(array, nmemb) == 0) {
            int[] swap = Writes.createExternalArray(nmemb / 2);

            tail_merge(array, swap, nmemb, 2);

            Writes.deleteExternalArray(swap);
        }
    }

    public void tailsort(int[] array, int nmemb) {
        if (nmemb < 2) return;

        int[] swap = Writes.createExternalArray(nmemb / 2);

        tail_merge(array, swap, nmemb, 1);

        Writes.deleteExternalArray(swap);
    }
}
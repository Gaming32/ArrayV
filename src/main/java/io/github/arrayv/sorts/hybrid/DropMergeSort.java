package io.github.arrayv.sorts.hybrid;

import java.util.List;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2020 fungamer2 and Emil Ernerfeldt
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

public final class DropMergeSort extends Sort {
    public DropMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Drop Merge");
        this.setRunAllSortsName("Drop Merge Sort");
        this.setRunSortName("Drop Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private final int RECENCY = 8;
    private final int EARLY_OUT_TEST_AT = 4;
    private final double EARLY_OUT_DISORDER_FRACTION = 0.6;

    private void truncateArrayList(List<Integer> arrayList, int len) {
    	int size = arrayList.size();
    	arrayList.subList(len, size).clear();
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if (length < 2) return;

        PDQBranchedSort pdqSort = new PDQBranchedSort(arrayVisualizer);
        List<Integer> dropped = Writes.createArrayList(length);

        int num_dropped_in_a_row = 0;
        int read = 0;
        int write = 0;

        int iteration = 0;
        int early_out_stop = length / EARLY_OUT_TEST_AT;

        while (read < length) {
            Highlights.markArray(2, read);
            iteration += 1;
            if (iteration == early_out_stop && dropped.size() > read * EARLY_OUT_DISORDER_FRACTION) {
                // We have seen a lot of the elements and dropped a lot of them.
                // This doesn't look good. Abort.
                Highlights.clearMark(2);

                for (int i = 0; i < dropped.size(); i++) {
                    Writes.write(array, write++, dropped.get(i), 1, true, false);
                }
                Writes.arrayListClear(dropped);
                pdqSort.customSort(array, 0, length);
                Writes.deleteArrayList(dropped);
                return;
            }

            if (write == 0 || Reads.compareIndices(array, read, write - 1, 0, false) >= 0) {
                // The element is order - keep it:
                Writes.write(array, write++, array[read++], 1, true, false);
                num_dropped_in_a_row = 0;
            } else {
              if (num_dropped_in_a_row == 0 && write >= 2 && Reads.compareIndices(array, read, write - 2, 0, false) >= 0) {
                  // Quick undo: drop previously accepted element, and overwrite with new one
                  Writes.arrayListAdd(dropped, array[write - 1], false, 0);
                  Writes.write(array, write - 1, array[read++], 1, true, false);
                  continue;
              }

              if (num_dropped_in_a_row < RECENCY) {
                  Writes.arrayListAdd(dropped, array[read++], false, 0);
                  Delays.sleep(1);
                  num_dropped_in_a_row++;
              } else {
                  //We accepted something num_dropped_in_row elements back that made us drop all RECENCY subsequent items.
                  //Accepting that element was obviously a mistake - so let's undo it!

                  // Undo dropping the last num_dropped_in_row elements:
                  int trunc_to_length = dropped.size() - num_dropped_in_a_row;
                  truncateArrayList(dropped, trunc_to_length);
                  read -= num_dropped_in_a_row;

                  int num_backtracked = 1;
                  write--;

                  int max_of_dropped = read;
                  for (int i = read + 1; i <= read + num_dropped_in_a_row; i++) {
                      if (Reads.compareValues(array[i], max_of_dropped) == 1) {
                          max_of_dropped = array[i];
                      }
                  }

                  while (write >= 1 && Reads.compareValues(max_of_dropped, array[write - 1]) == -1) {
                      Highlights.markArray(1, --write);
                      Delays.sleep(1);
                      num_backtracked++;
                  }

                  for (int i = write; i < write + num_backtracked; i++) {
                      Highlights.markArray(1, i);
                      Writes.arrayListAdd(dropped, array[i], true, 0);
                      Delays.sleep(1);
                  }

                  num_dropped_in_a_row = 0;
              }
            }
        }

        Highlights.clearMark(2);

        for (int i = 0; i < dropped.size(); i++) {
            Writes.write(array, write + i, dropped.get(i), 1, true, false);
        }

        pdqSort.customSort(array, write, length);


        int[] buffer = Writes.createExternalArray(dropped.size());

        Writes.arraycopy(array, write, buffer, 0, dropped.size(), 1, true, true);

        int i = buffer.length - 1;
        int j = write - 1;
        int k = length - 1;

        while (i >= 0) {
            if (j < 0 || Reads.compareValues(buffer[i], array[j]) == 1) {
                Writes.write(array, k--, buffer[i--], 1, true, false);
            } else {
                Highlights.markArray(2, j);
                Writes.write(array, k--, array[j--], 1, true, false);
            }
        }

        Writes.deleteArrayList(dropped);
        Writes.deleteExternalArray(buffer);
    }
}

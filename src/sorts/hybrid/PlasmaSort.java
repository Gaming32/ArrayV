package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.InsertionSort;
import sorts.merge.ReverseLazyStableSort;
import sorts.templates.Sort;
import utils.Highlights;
import utils.Rotations;

/*
 * 
MIT License

Copyright (c) 2020 Gaming32

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

final public class PlasmaSort extends Sort {
    int[] keys;

    private InsertionSort insertSorter;
    private LazierSort finalMerger;

    public PlasmaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Plasma");
        this.setRunAllSortsName("Plasma Sort");
        this.setRunSortName("Plasmasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void rotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.holyGriesMills(array, pos, lenA, lenB, 1, true, false);
    }

    private int collectKeys(int[] array, int start, int keyCount, int end) {
        int minbound = start;
        int compindex = start + 1;
        int lastGood = compindex;
        int badCount = 0;
        int count;
        for (count = 1; count < keyCount - start; count++) {
            Highlights.markArray(3, compindex);
            int num = array[compindex];
            int l = minbound, h = lastGood;
            int lastBad = badCount;

            while (l < h) {
                int m = l + ((h - l) / 2); // avoid int overflow!
                Highlights.markArray(2, m);
                
                Delays.sleep(0.01);
                
                int comp = Reads.compareValues(num, array[m]);

                if (comp < 0) {
                    h = m;
                }
                else if (comp == 0) {
                    badCount++;
                    break;
                }
                else {
                    l = m + 1;
                }
            }

            if (badCount > 0) {
                if (badCount > lastBad) {
                    Delays.sleep(0.2);
                    count--;
                    compindex++;
                    if (compindex >= end) {
                        break;
                    }
                    continue;
                }
                rotate(array, minbound, lastGood - minbound, badCount);
                minbound += badCount;
                lastGood = compindex;
                l += badCount;
                badCount = 0;
            }
            
            // item has to go into position lo
            int j = compindex - 1;
            
            while (j >= l)
            {
                Writes.write(array, j + 1, array[j], 1, true, false);
                j--;
            }
            Writes.write(array, l, num, 1, true, false);
            
            Highlights.clearAllMarks();
            compindex++;
            lastGood++;

            if (compindex >= end) {
                break;
            }
        }

        Highlights.clearMark(4);
        if (minbound != start) {
            rotate(array, start, minbound - start, count);
        }
        return count;
    }

    private static int getBufferSize(int length) {
        int size;
        for (size = 1; size * size < length; size *= 2);
        return size;
    }

    private static int getKeySize(int bufferSize, int length) {
        return length / bufferSize;
    }

    private void mergeUnderBuffer(int[] array, int bufferSize, int start, int mid, int end, boolean rightPriority) {
        if (rightPriority) {
            rotate(array, start, mid - start, end - mid);
        }
        for (int i = 0; i < mid - start; i++) {
            Writes.swap(array, i, start + i, 0.5, true, false);
        }

        int bufferPointer = 0;
        int left = start, right = mid;

        while (left < right && right < end) {
            if (Reads.compareIndices(array, bufferPointer, right, 0.1, true) <= 0) {
                Writes.swap(array, bufferPointer++, left++, 0.25, true, false);
            }
            else {
                Writes.swap(array, left++, right++, 0.25, true, false);
            }
        }

        while (left < right) {
            Writes.swap(array, bufferPointer++, left++, 0.25, true, false);
        }
    }

    public void blockSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, 1, true, false);
        }
    }

    // public void blockSelection(int[] array, int start, int end, int blockSize) {
    //     for (int i = start; i < end - blockSize; i += blockSize) {
    //         int lowestindex = i;
    //         int lowestkey = (lowestindex - start) / blockSize;

    //         for (int j = i + blockSize; j < end; j += blockSize) {
    //             int jkey = (j - start) / blockSize;
    //             Highlights.markArray(2, j);
    //             Delays.sleep(0.5);

    //             int comp = Reads.compareValues(array[j], array[lowestindex]);
    //             if (comp == -1 || (comp == 0 && Reads.compareOriginalIndices(keys, jkey, lowestkey, 0.25, true) == -1)) {
    //                 lowestindex = j;
    //                 lowestkey = jkey;
    //                 Highlights.markArray(1, lowestindex);
    //                 Delays.sleep(0.5);
    //             }
    //         }
    //         if (lowestindex > i) {
    //             blockSwap(array, i, lowestindex, blockSize);
    //             Writes.swap(keys, (i - start) / blockSize, lowestkey, 1, true, true);
    //         }
    //     }
    // }

    private void mergeOverBuffer(int[] array, int bufferSize, int start, int mid, int end, int keySize) {
        resetKeys(keySize);
        int midKey = keys[keySize / 2];
        int blockSize = bufferSize;
        // blockSelection(array, start, end, blockSize);

        int i;
        for (i = start; i < end - blockSize; i += blockSize) {
            int ikey = (i - start) / blockSize;
            int lowestindex = i;
            int lowestkey = ikey;

            for (int j = i + blockSize; j < end; j += blockSize) {
                int jkey = (j - start) / blockSize;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);

                int comp = Reads.compareValues(array[j], array[lowestindex]);
                if (comp == -1 || (comp == 0 && Reads.compareOriginalIndices(keys, jkey, lowestkey, 0.25, true) == -1)) {
                    lowestindex = j;
                    lowestkey = jkey;
                    Highlights.markArray(1, lowestindex);
                    Delays.sleep(0.5);
                }
            }
            if (lowestindex > i) {
                blockSwap(array, i, lowestindex, blockSize);
                Writes.swap(keys, (i - start) / blockSize, lowestkey, 1, true, true);
            }
            if (ikey > 0) {
                if (Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) {
                    int keyIndex = (i - start) / blockSize;
                    mergeUnderBuffer(array, bufferSize, i - blockSize, i, i + blockSize, keys[keyIndex - 1] > midKey && keys[keyIndex - 1] > keys[keyIndex]);
                }
            }
        }
        int keyIndex = keySize - 1;
        mergeUnderBuffer(array, bufferSize, i - blockSize, i, i + blockSize, keys[keyIndex - 1] > midKey && keys[keyIndex - 1] > keys[keyIndex]);

        // int checkStart = start;
        // while (checkStart < end - blockSize) {
        //     if (Reads.compareIndices(array, checkStart + blockSize - 1, checkStart + blockSize, 1, true) == 1) {
        //         int keyIndex = (checkStart - start) / blockSize;
        //         // int keyIndex2 = (checkStart + blockSize - start) / blockSize;
        //         // Highlights.markArray(1, keyIndex);
        //         mergeUnderBuffer(array, bufferSize, checkStart, checkStart + blockSize, checkStart + 2 * blockSize, Reads.compareOriginalValues(keys[keyIndex], keys[keyIndex + 1]) == 1);
        //     }
        //     checkStart += blockSize;
        // }
    }

    private void resetKeys(int count) {
        for (int i = 0; i < count; i++) {
            Writes.write(keys, i, i, 0.5, true, true);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        insertSorter = new InsertionSort(arrayVisualizer);
        finalMerger = new LazierSort(arrayVisualizer);
        
        if (sortLength < 24) {
            insertSorter.customInsertSort(array, 0, sortLength, 0.333, false);
            return;
        }
        int bufferSize = PlasmaSort.getBufferSize(sortLength);
        collectKeys(array, 0, bufferSize, sortLength);

        int i;
        for (i = bufferSize + 1; i < sortLength; i += 2) {
            if (Reads.compareIndices(array, i - 1, i, 0.5, true) == 1) {
                Writes.swap(array, i - 1, i, 0.5, true, false);
            }
        }

        int gap;
        for (gap = 2; gap < bufferSize * 2; gap *= 2) {
            for (i = bufferSize; i + 2 * gap <= sortLength; i += 2 * gap) {
                mergeUnderBuffer(array, bufferSize, i, i + gap, i + 2 * gap, false);
            }
            if (i + gap < sortLength) {
                mergeUnderBuffer(array, bufferSize, i, i + gap, sortLength, false);
            }
        }

        int keyCount = PlasmaSort.getKeySize(bufferSize, sortLength);
        keys = Writes.createExternalArray(keyCount);
        for (; gap <= sortLength - bufferSize; gap *= 2) {
            int keySize = 2 * gap / bufferSize;
            for (i = bufferSize; i + 2 * gap <= sortLength; i += 2 * gap) {
                mergeOverBuffer(array, bufferSize, i, i + gap, i + 2 * gap, keySize);
            }
            if (i + gap < sortLength) {
                mergeOverBuffer(array, bufferSize, i, i + gap, sortLength, keySize);
            }
        }

        Writes.deleteExternalArray(keys);
        // runSort(array, bufferSize, bucketCount);
        // finalMerger.inPlaceMerge(array, 0, bufferSize, sortLength);
    }
}
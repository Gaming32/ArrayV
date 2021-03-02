package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Rotations;

final public class PlasmaSort extends Sort {
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

    private void strategyTwo(int[] array, int keyCount, int end) {
        if (keyCount < 2) {
            return;
        }
        // implement later
    }

    // copied from grailBinSearch
    private int binSearch(int[] array, int pos, int len, int key, boolean isLeft) {
        int left = -1, right = len;
        while(left < right - 1) {
            int mid = left + ((right - left) >> 1);
            if(isLeft) {
                if(Reads.compareValues(array[pos + mid], key) >= 0) {
                    right = mid;
                } else {
                    left = mid;
                }
            } else {
                if(Reads.compareValues(array[pos + mid], key) > 0) {
                    right = mid;
                } else left = mid;
            }
            Highlights.markArray(1, pos + mid);
        }
        return right;
    }

    // copied from grailMergeWithoutBuffer
    private void mergeInPlace(int[] array, int pos, int lenA, int lenB) {
        if(lenA < lenB) {
            while(lenA != 0) {
                //Binary Search left
                int loc = binSearch(array, pos + lenA, lenB, array[pos], true);
                if(loc != 0) {
                    rotate(array, pos, lenA, loc);
                    pos += loc;
                    lenB -= loc;
                }
                if(lenB == 0) break;
                do {
                    pos++;
                    lenA--;
                } while(lenA != 0 && Reads.compareValues(array[pos], array[pos + lenA]) <= 0);
            }
        } else {
            while(lenB != 0) {
                //Binary Search right
                int loc = binSearch(array, pos, lenA, array[pos + (lenA + lenB - 1)], false);
                if(loc != lenA) {
                    rotate(array, pos + loc, lenA - loc, lenB);
                    lenA = loc;
                }
                if(lenA == 0) break;
                do {
                    lenB--;
                } while(lenB != 0 && Reads.compareValues(array[pos + lenA - 1], array[pos + lenA + lenB - 1]) <= 0);
            }
        }
    }

    private void mergeUnder(int[] array, int keyCount, int start, int mid, int end, boolean isLeft) {
        if (isLeft) {
            int buffer = end + keyCount - 1;
            int left = mid - 1, right = end - 1;
            while (left >= start && right >= mid) {
                if (Reads.compareIndices(array, left, right, 1, true) <= 0) {
                    Writes.swap(array, right--, buffer--, 1, true, false);
                }
                else {
                    Writes.swap(array, left--, buffer--, 1, true, false);
                }
            }
            while (left >= start) {
                Writes.swap(array, left--, buffer--, 1, true, false);
            }
            while (right >= mid) {
                Writes.swap(array, right--, buffer--, 1, true, false);
            }
        }
        else {
            int buffer = start - keyCount;
            int left = start, right = mid;
            while (left < mid && right < end) {
                if (Reads.compareIndices(array, left, right, 1, true) <= 0) {
                    Writes.swap(array, buffer++, left++, 1, true, false);
                }
                else {
                    Writes.swap(array, buffer++, right++, 1, true, false);
                }
            }
            while (left < mid) {
                Writes.swap(array, buffer++, left++, 1, true, false);
            }
            while (right < end) {
                Writes.swap(array, buffer++, right++, 1, true, false);
            }
        }
    }

    private void mergeOver(int[] array, int keyCount, int start, int mid, int end) {
        if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) {
            return;
        }
        else if (Reads.compareIndices(array, start, end - 1, 1, true) > 0) {
            rotate(array, start, mid - start, end - mid);
            return;
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int keys = 1;
        while (keys * keys < currentLength) {
            keys *= 2;
        }
        int keysFound = collectKeys(array, 0, keys, currentLength);
        // System.out.println(keysFound + "/" + keys + " keys");
        if (keysFound < keys) { // not enough unique keys!
            strategyTwo(array, keysFound, currentLength);
            return;
        }

        int i;
        boolean left = false;
        int lastMax = 0;
        for (int size = 1, size2 = 2 * size; size < keys; size *= 2, size2 *= 2) {
            if (left) {
                i = currentLength - size2;
                if (lastMax != 0) {
                    mergeUnder(array, size2, i - lastMax - size, i - lastMax, i, left);
                    i -= lastMax + size;
                }
                for (; i - size2 >= keys - size2; i -= size2) {
                    mergeUnder(array, size2, i - size2, i - size, i, left);
                }
            }
            else {
                int bufferSize = size2 == keys ? size2 : 2 * size2;
                for (i = keys; i + size2 < currentLength; i += size2) {
                    mergeUnder(array, bufferSize, i, i + size, i + size2, left);
                }
                if (i + size < currentLength) {
                    lastMax = currentLength - i;
                    mergeUnder(array, bufferSize, i, i + size, currentLength, left);
                }
                else {
                    lastMax = 0;
                }
            }
            left = !left;
        }

        // for (int size = keys, size2 = 2 * size; size < currentLength; size *= 2, size2 *= 2) {
        //     for (i = keys; i + size2 < currentLength; i += size2) {
        //         mergeOver(array, keys, i, i + size, i + size2);
        //     }
        //     if (i + size < currentLength) {
        //         mergeOver(array, keys, i, i + size, currentLength);
        //     }
        // }

        // mergeInPlace(array, 0, keys, currentLength - keys);
    }
}

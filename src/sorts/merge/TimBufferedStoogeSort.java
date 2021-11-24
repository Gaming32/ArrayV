package sorts.merge;

import main.ArrayVisualizer;

/**
 * @author _fluffyy
 * @author thatsOven
 * @author Gaming32
 */
public final class TimBufferedStoogeSort extends OOPBufferedStoogeSort {
    int len;
    int[] a, aux;

    private static final int MIN_GALLOP = 7;
    private int minGallop;

    public TimBufferedStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tim Buffered Stooge");
        this.setRunAllSortsName("Tim Buffered Stooge Sort");
        this.setRunSortName("Tim Buffered Stoogesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void markArray(int marker, int markPosition) {
        if(markPosition >= 0 && markPosition < this.len) {
            this.Highlights.markArray(marker, markPosition);
        }
    }

    private static int gallopLeft(TimBufferedStoogeSort ts, int key, int[] a, int base, int len, int hint) {
        int lastOfs = 0;
        int ofs = 1;

        ts.markArray(3, base + hint);
        ts.Delays.sleep(1);

        if (ts.Reads.compareValues(key, a[base + hint]) > 0) {
            // Gallop right until a[base+hint+lastOfs] < key <= a[base+hint+ofs]
            int maxOfs = len - hint;

            ts.markArray(3, base + hint + ofs);
            ts.Delays.sleep(1);

            while (ofs < maxOfs && ts.Reads.compareValues(key, a[base + hint + ofs]) > 0) {
                lastOfs = ofs;
                ofs = (ofs * 2) + 1;
                if (ofs <= 0)   // int overflow
                    ofs = maxOfs;

                ts.markArray(3, base + hint + ofs);
                ts.Delays.sleep(1);
            }
            if (ofs > maxOfs)
                ofs = maxOfs;

            // Make offsets relative to base
            lastOfs += hint;
            ofs += hint;
        } else { // key <= a[base + hint]
            // Gallop left until a[base+hint-ofs] < key <= a[base+hint-lastOfs]
            final int maxOfs = hint + 1;

            ts.markArray(3, base + hint - ofs);
            ts.Delays.sleep(1);

            while (ofs < maxOfs && ts.Reads.compareValues(key, a[base + hint - ofs]) <= 0) {
                lastOfs = ofs;
                ofs = (ofs * 2) + 1;
                if (ofs <= 0)   // int overflow
                    ofs = maxOfs;

                ts.markArray(3, base + hint - ofs);
                ts.Delays.sleep(1);
            }
            if (ofs > maxOfs)
                ofs = maxOfs;

            // Make offsets relative to base
            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        }

        /*
         * Now a[base+lastOfs] < key <= a[base+ofs], so key belongs somewhere
         * to the right of lastOfs but no farther right than ofs.  Do a binary
         * search, with invariant a[base + lastOfs - 1] < key <= a[base + ofs].
         */
        lastOfs++;
        while (lastOfs < ofs) {
            int m = lastOfs + ((ofs - lastOfs) >>> 1);

            ts.markArray(3, base + m);
            ts.Delays.sleep(1);

            if (ts.Reads.compareValues(key, a[base + m]) > 0)
                lastOfs = m + 1;  // a[base + m] < key
            else
                ofs = m;          // key <= a[base + m]
        }
        ts.Highlights.clearMark(3);
        return ofs;
    }

    private static int gallopRight(TimBufferedStoogeSort ts, int key, int[] a, int base, int len, int hint) {
        int ofs = 1;
        int lastOfs = 0;

        ts.markArray(3, base + hint);
        ts.Delays.sleep(1);

        if (ts.Reads.compareValues(key, a[base + hint]) < 0) {
            // Gallop left until a[b+hint - ofs] <= key < a[b+hint - lastOfs]
            int maxOfs = hint + 1;

            ts.markArray(3, base + hint - ofs);
            ts.Delays.sleep(1);

            while (ofs < maxOfs && ts.Reads.compareValues(key, a[base + hint - ofs]) < 0) {
                lastOfs = ofs;
                ofs = (ofs * 2) + 1;
                if (ofs <= 0)   // int overflow
                    ofs = maxOfs;

                ts.markArray(3, base + hint - ofs);
                ts.Delays.sleep(1);
            }
            if (ofs > maxOfs)
                ofs = maxOfs;

            // Make offsets relative to b
            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        } else { // a[b + hint] <= key
            // Gallop right until a[b+hint + lastOfs] <= key < a[b+hint + ofs]
            int maxOfs = len - hint;

            ts.markArray(3, base + hint + ofs);
            ts.Delays.sleep(1);

            while (ofs < maxOfs && ts.Reads.compareValues(key, a[base + hint + ofs]) >= 0) {
                lastOfs = ofs;
                ofs = (ofs * 2) + 1;
                if (ofs <= 0)   // int overflow
                    ofs = maxOfs;

                ts.markArray(3, base + hint + ofs);
                ts.Delays.sleep(1);
            }
            if (ofs > maxOfs)
                ofs = maxOfs;

            // Make offsets relative to b
            lastOfs += hint;
            ofs += hint;
        }

        /*
         * Now a[b + lastOfs] <= key < a[b + ofs], so key belongs somewhere to
         * the right of lastOfs but no farther right than ofs.  Do a binary
         * search, with invariant a[b + lastOfs - 1] <= key < a[b + ofs].
         */
        lastOfs++;
        while (lastOfs < ofs) {
            int m = lastOfs + ((ofs - lastOfs) >>> 1);

            ts.markArray(3, base + m);
            ts.Delays.sleep(1);

            if (ts.Reads.compareValues(key, a[base + m]) < 0)
                ofs = m;          // key < a[b + m]
            else
                lastOfs = m + 1;  // a[b + m] <= key
        }
        ts.Highlights.clearMark(3);
        return ofs;
    }

    private static void mergeLo(TimBufferedStoogeSort ts, int base1, int len1, int base2, int len2) {
        // Copy first run into temp array
        int[] a = ts.a; // For performance
        int[] tmp = ts.aux;
        ts.Writes.arraycopy(a, base1, tmp, 0, len1, 1, true, true);

        int cursor1 = 0;       // Indexes into tmp array
        int cursor2 = base2;   // Indexes int a
        int dest = base1;      // Indexes int a

        // Move first element of second run and deal with degenerate cases
        ts.Writes.write(a, dest++, a[cursor2++], 1, false, false);
        ts.markArray(1, dest);
        ts.markArray(2, cursor2);
        if (--len2 == 0) {
            ts.Writes.arraycopy(tmp, cursor1, a, dest, len1, 1, true, false);
            return;
        }
        if (len1 == 1) {
            ts.Writes.arraycopy(a, cursor2, a, dest, len2, 1, true, false);
            ts.Writes.write(a, dest + len2, tmp[cursor1], 1, false, false); // Last elt of run 1 to end of merge
            ts.markArray(1, dest + len2);
            return;
        }

        int minGallop = ts.minGallop;    //  "    "       "     "      "
    outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won
            /*
             * Do the straightforward thing until (if ever) one run starts
             * winning consistently.
             */
            do {
                if (ts.Reads.compareValues(a[cursor2], tmp[cursor1]) < 0) {
                    ts.Writes.write(a, dest++, a[cursor2++], 1, false, false);
                    ts.markArray(1, dest);
                    ts.markArray(2, cursor2);
                    count2++;
                    count1 = 0;
                    if (--len2 == 0)
                        break outer;
                } else {
                    ts.Writes.write(a, dest++, tmp[cursor1++], 1, false, false);
                    ts.markArray(1, dest);
                    count1++;
                    count2 = 0;
                    if (--len1 == 1)
                        break outer;
                }
            } while ((count1 | count2) < minGallop);

            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                count1 = gallopRight(ts, a[cursor2], tmp, cursor1, len1, 0);
                if (count1 != 0) {
                    ts.Writes.arraycopy(tmp, cursor1, a, dest, count1, 1, true, false);
                    dest += count1;
                    cursor1 += count1;
                    len1 -= count1;
                    if (len1 <= 1) // len1 == 1 || len1 == 0
                        break outer;
                }
                ts.Writes.write(a, dest++, a[cursor2++], 1, false, false);
                ts.markArray(1, dest);
                ts.markArray(2, cursor2);
                if (--len2 == 0)
                    break outer;

                count2 = gallopLeft(ts, tmp[cursor1], a, cursor2, len2, 0);
                if (count2 != 0) {
                    ts.Writes.arraycopy(a, cursor2, a, dest, count2, 1, true, false);
                    dest += count2;
                    cursor2 += count2;
                    len2 -= count2;
                    if (len2 == 0)
                        break outer;
                }
                ts.Writes.write(a, dest++, tmp[cursor1++], 1, false, false);
                ts.markArray(1, dest);
                if (--len1 == 1)
                    break outer;
                minGallop--;
            } while (count1 >= MIN_GALLOP | count2 >= MIN_GALLOP);
            if (minGallop < 0)
                minGallop = 0;
            minGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        ts.minGallop = minGallop < 1 ? 1 : minGallop;  // Write back to field

        if (len1 == 1) {
            ts.Writes.arraycopy(a, cursor2, a, dest, len2, 1, true, false);
            ts.Writes.write(a, dest + len2, tmp[cursor1], 1, false, false); //  Last elt of run 1 to end of merge
            ts.markArray(1, dest + len2);
        } else if (len1 == 0) {
            throw new IllegalArgumentException(
                "Comparison method violates its general contract!");
        } else {
            ts.Writes.arraycopy(tmp, cursor1, a, dest, len1, 1, true, false);
        }
    }

    private static void mergeHi(TimBufferedStoogeSort ts, int base1, int len1, int base2, int len2) {
        // Copy second run into temp array
        int[] a = ts.a; // For performance
        int[] tmp = ts.aux;
        ts.Writes.arraycopy(a, base2, tmp, 0, len2, 1, true, true);

        int cursor1 = base1 + len1 - 1;  // Indexes into a
        int cursor2 = len2 - 1;          // Indexes into tmp array
        int dest = base2 + len2 - 1;     // Indexes into a

        // Move last element of first run and deal with degenerate cases
        ts.Writes.write(a, dest--, a[cursor1--], 1, false, false);
        ts.markArray(1, dest);
        ts.markArray(2, cursor1);
        if (--len1 == 0) {
            ts.Writes.arraycopy(tmp, 0, a, dest - (len2 - 1), len2, 1, true, false);
            return;
        }
        if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            ts.Writes.arraycopy(a, cursor1 + 1, a, dest + 1, len1, 1, true, false);
            ts.Writes.write(a, dest, tmp[cursor2], 1, false, false);
            ts.markArray(1, dest);
            return;
        }

        int minGallop = ts.minGallop;    //  "    "       "     "      "
    outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won

            /*
             * Do the straightforward thing until (if ever) one run
             * appears to win consistently.
             */
            do {
                if (ts.Reads.compareValues(tmp[cursor2], a[cursor1]) < 0) {
                    ts.Writes.write(a, dest--, a[cursor1--], 1, false, false);
                    ts.markArray(1, dest);
                    ts.markArray(2, cursor1);
                    count1++;
                    count2 = 0;
                    if (--len1 == 0)
                        break outer;
                } else {
                    ts.Writes.write(a, dest--, tmp[cursor2--], 1, false, false);
                    ts.markArray(1, dest);
                    count2++;
                    count1 = 0;
                    if (--len2 == 1)
                        break outer;
                }
            } while ((count1 | count2) < minGallop);

            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                count1 = len1 - gallopRight(ts, tmp[cursor2], a, base1, len1, len1 - 1);
                if (count1 != 0) {
                    dest -= count1;
                    cursor1 -= count1;
                    len1 -= count1;
                    ts.Writes.arraycopy(a, cursor1 + 1, a, dest + 1, count1, 1, true, false);
                    if (len1 == 0)
                        break outer;
                }
                ts.Writes.write(a, dest--, tmp[cursor2--], 1, false, false);
                ts.markArray(1, dest);
                if (--len2 == 1)
                    break outer;

                count2 = len2 - gallopLeft(ts, a[cursor1], tmp, 0, len2, len2 - 1);
                if (count2 != 0) {
                    dest -= count2;
                    cursor2 -= count2;
                    len2 -= count2;
                    ts.Writes.arraycopy(tmp, cursor2 + 1, a, dest + 1, count2, 1, true, false);
                    if (len2 <= 1)  // len2 == 1 || len2 == 0
                        break outer;
                }
                ts.Writes.write(a, dest--, a[cursor1--], 1, false, false);
                ts.markArray(1, dest);
                ts.markArray(2, cursor1);
                if (--len1 == 0)
                    break outer;
                minGallop--;
            } while (count1 >= MIN_GALLOP | count2 >= MIN_GALLOP);
            if (minGallop < 0)
                minGallop = 0;
            minGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        ts.minGallop = minGallop < 1 ? 1 : minGallop;  // Write back to field

        if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            ts.Writes.arraycopy(a, cursor1 + 1, a, dest + 1, len1, 1, true, false);
            ts.Writes.write(a, dest, tmp[cursor2], 1, false, false); // Move first elt of run2 to front of merge
            ts.markArray(1, dest);
        } else if (len2 == 0) {
            throw new IllegalArgumentException(
                "Comparison method violates its general contract!");
        } else {
            ts.Writes.arraycopy(tmp, 0, a, dest - (len2 - 1), len2, 1, true, false);
        }
    }

    @Override
	protected void mergeOOP(int[] arr, int start, int mid, int end) {
		this.Highlights.clearMark(1);
        this.Highlights.clearMark(2);

        int base1 = start, base2 = mid,
            len1 = mid - start, len2 = end - mid;

        /*
         * Find where the first element of run2 goes in run1. Prior elements
         * in run1 can be ignored (because they're already in place).
         */
        int k = gallopRight(this, arr[base2], arr, base1, len1, 0);
        base1 += k;
        len1 -= k;
        if (len1 == 0)
            return;

        /*
         * Find where the last element of run1 goes in run2. Subsequent elements
         * in run2 can be ignored (because they're already in place).
         */
        len2 = gallopLeft(this, arr[base1 + len1 - 1], arr, base2, len2, len2 - 1);
        if (len2 == 0)
            return;

        // Merge remaining runs, using tmp array with min(len1, len2) elements
        if (len1 <= len2)
            mergeLo(this, base1, len1, base2, len2);
        else
            mergeHi(this, base1, len1, base2, len2);

        this.Highlights.clearMark(1);
        this.Highlights.clearMark(2);
	}

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.len = sortLength;
        this.a = array;
        minGallop = MIN_GALLOP;
        aux = Writes.createExternalArray((sortLength + 2) / 3);
        wrapper(array, 0, sortLength);
        Writes.deleteExternalArray(aux);
    }
}

package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * MIT License
 *
 * Copyright (c) 2013 Andrey Astrelin
 * Copyright (c) 2020 The Holy Grail Sort Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * The Holy Grail Sort Project
 * Project Manager:      Summer Dragonfly
 * Project Contributors: 666666t
 *                       Anonymous0726
 *                       aphitorite
 *                       dani_dlg
 *                       EilrahcF
 *                       Enver
 *                       lovebuny
 *                       MP
 *                       phoenixbound
 *                       thatsOven
 *                       Bee sort
 *
 * Special thanks to "The Studio" Discord community!
 */

/**
 * Adaptive Grail Sort
 *
 * O(1) space stable worst case O(n log n) algorithm
 * designed to take advantage of partially ordered data with constant memory
 *
 * @author aphitorite
 */
public final class AdaptiveGrailSort extends Sort {
    public AdaptiveGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Grail");
        this.setRunAllSortsName("Adaptive Grail Sort (Block Merge Sort)");
        this.setRunSortName("Adaptive Grailsort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    enum Subarray {
        LEFT,
        RIGHT;
    }

    private int minRun;

    private void multiSwap(int[] array, int a, int b, int len) {
        for(int i = 0; i < len; i++)
            Writes.swap(array, a+i, b+i, 1, true, false);
    }

    //changes len sized blocks order ABC -> BCA
    private void multiTriSwap(int[] array, int a, int b, int c, int len) {
        Highlights.clearMark(2);
        for(int i = 0; i < len; i++) {
            int temp = array[a+i];
            Writes.write(array, a+i, array[b+i], 0.333, true, false);
            Writes.write(array, b+i, array[c+i], 0.333, true, false);
            Writes.write(array, c+i, temp, 0.333, true, false);
        }
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        while(a > b) Writes.write(array, a, array[(a--)-1], 0.5, true, false);
        Writes.write(array, b, temp, 0.5, true, false);
    }

    private void insertToBW(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        while(a < b) Writes.write(array, a, array[(a++)+1], 0.5, true, false);
        Writes.write(array, a, temp, 0.5, true, false);
    }

    private void shift(int[] array, int a, int m, int b) {
        while(m < b) Writes.swap(array, a++, m++, 1, true, false);
    }

    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;

        while(l > 1 && r > 1) {
            if(r < l) {
                this.multiSwap(array, m-r, m, r);
                b -= r;
                m -= r;
                l -= r;
            }
            else {
                this.multiSwap(array, a, m, l);
                a += l;
                m += l;
                r -= l;
            }
        }

        Highlights.clearMark(2);
        if(r == 1)      this.insertTo(array, m, a);
        else if(l == 1) this.insertToBW(array, a, b-1);
    }

    private int leftBinarySearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m+1;
        }

        return a;
    }

    private int rightBinarySearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }

        return a;
    }

    private int buildUniqueRun(int[] array, int a, int n) {
        int nKeys = 1, i = a+1;

        //build run at start
        if(Reads.compareIndices(array, i-1, i, 1, true) == -1){
            i++;
            nKeys++;

            while(nKeys < n && Reads.compareIndices(array, i-1, i, 1, true) == -1) {
                i++;
                nKeys++;
            }
        }
        else if(Reads.compareIndices(array, i-1, i, 1, true) == 1) {
            i++;
            nKeys++;

            while(nKeys < n && Reads.compareIndices(array, i-1, i, 1, true) == 1) {
                i++;
                nKeys++;
            }
            Writes.reversal(array, a, i-1, 1, true, false);
        }

        return nKeys;
    }

    private int buildUniqueRunBW(int[] array, int b, int n) {
        int nKeys = 1, i = b-1;

        //build run at end
        if(Reads.compareIndices(array, i-1, i, 1, true) == -1){
            i--;
            nKeys++;

            while(nKeys < n && Reads.compareIndices(array, i-1, i, 1, true) == -1) {
                i--;
                nKeys++;
            }
        }
        else if(Reads.compareIndices(array, i-1, i, 1, true) == 1) {
            i--;
            nKeys++;

            while(nKeys < n && Reads.compareIndices(array, i-1, i, 1, true) == 1) {
                i--;
                nKeys++;
            }
            Writes.reversal(array, i, b-1, 1, true, false);
        }

        return nKeys;
    }

    private int findKeys(int[] array, int a, int b, int nKeys, int n) {
        int p = a, pEnd = a+nKeys;

        Highlights.clearMark(2);
        for(int i = pEnd; i < b && nKeys < n; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = this.leftBinarySearch(array, p, pEnd, array[i]);

            if(pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                this.rotate(array, p, pEnd, i);
                int inc = i-pEnd;
                loc  += inc;
                p    += inc;
                pEnd += inc;

                this.insertTo(array, pEnd, loc);
                nKeys++;
                pEnd++;
            }
        }
        this.rotate(array, a, p, pEnd);
        return nKeys;
    }

    //special thanks to @MP for this idea
    private int findKeysBW(int[] array, int a, int b, int nKeys, int n) {
        int p = b-nKeys, pEnd = b;

        Highlights.clearMark(2);
        for(int i = p-1; i >= a && nKeys < n; i--) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = this.leftBinarySearch(array, p, pEnd, array[i]);

            if(pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                this.rotate(array, i+1, p, pEnd);
                int inc = p-(i+1);
                loc  -= inc;
                pEnd -= inc;
                p    -= inc+1;
                nKeys++;

                this.insertToBW(array, i, loc-1);
            }
        }
        this.rotate(array, p, pEnd, b);
        return nKeys;
    }

    //instead of insertion level find & create runs divisible by minRun
    private void buildRuns(int[] array, int a, int b) {
        int i = a+1, j = a;

        while(i < b) {
            if(Reads.compareIndices(array, i-1, i++, 1, true) == 1) {
                while(i < b && Reads.compareIndices(array, i-1, i, 1, true) == 1) i++;
                Writes.reversal(array, j, i-1, 1, true, false);
            }
            else while(i < b && Reads.compareIndices(array, i-1, i, 1, true) <= 0) i++;

            if(i < b) j = i - (i-j-1)%this.minRun - 1;//a%b, if(a%b == 0) -> a = b

            while(i-j < this.minRun && i < b) {
                this.insertTo(array, i, this.rightBinarySearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
    }

    private void binaryInsertion(int[] array, int a, int b) {
        for(int i = a+1; i < b; i++)
            this.insertTo(array, i, this.rightBinarySearch(array, a, i, array[i]));
    }

    private void mergeWithBufRest(int[] array, int a, int m, int b, int p, int pLen) {
        int i = 0, j = m, k = a;

        while(i < pLen && j < b) {
            if(Reads.compareValues(array[p+i], array[j]) <= 0)
                Writes.swap(array, k++, p+(i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        }
        while(i < pLen) Writes.swap(array, k++, p+(i++), 1, true, false);
    }

    private void mergeWithBuf(int[] array, int a, int m, int b, int p) {
        int l = m-a;
        this.multiSwap(array, p, a, l);
        this.mergeWithBufRest(array, a, m, b, p, l);
    }

    private void mergeWithBufBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.multiSwap(array, m, p, pLen);

        int i = pLen-1, j = m-1, k = b-1;

        while(i >= 0 && j >= a) {
            if(Reads.compareValues(array[p+i], array[j]) >= 0)
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while(i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
    }

    private void inPlaceMerge(int[] array, int a, int m, int b) {
        int i = a, j = m, k;

        while(i < j && j < b) {
            if(Reads.compareValues(array[i], array[j]) > 0) {
                k = this.leftBinarySearch(array, j+1, b, array[i]);
                this.rotate(array, i, j, k);

                i += k-j;
                j = k;
            }
            else i++;
        }
    }

    private void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m-1, j = b-1, k;

        while(j > i && i >= a){
            if(Reads.compareValues(array[i], array[j]) > 0) {
                k = this.rightBinarySearch(array, a, i, array[j]);
                this.rotate(array, k, i+1, j+1);

                j -= (i+1)-k;
                i = k-1;
            }
            else j--;
        }
    }

    private void mergeWithoutBuf(int[] array, int a, int m, int b) {
        if(m-a > b-m) this.inPlaceMergeBW(array, a, m, b);
        else          this.inPlaceMerge(array, a, m, b);
    }

    private boolean checkSorted(int[] array, int a, int m, int b) {
        return Reads.compareValues(array[m-1], array[m]) > 0;
    }

    private boolean checkReverseBounds(int[] array, int a, int m, int b) {
        if(Reads.compareValues(array[a], array[b-1]) == 1) {
            this.rotate(array, a, m, b);
            return false;
        }

        return true;
    }

    private boolean checkBounds(int[] array, int a, int m, int b) {
        return this.checkSorted(array, a, m, b)
            && this.checkReverseBounds(array, a, m, b);
    }

    private Subarray grailGetSubarray(int[] array, int t, int mKey) {
        if(Reads.compareValues(array[t], array[mKey]) < 0)
            return Subarray.LEFT;

        else return Subarray.RIGHT;
    }

    //returns mKey final position
    private int blockSelectSort(int[] array, int p, int t, int r, int d, int lCount, int bCount, int bLen) {
        int mKey = lCount;

        for(int j = 0, k = lCount+1; j < k-1; j++) {
            int min = j;

            for(int i = Math.max(lCount-r, j+1); i < k; i++) {
                int comp = Reads.compareIndices(array, p+d + i*bLen, p+d + min*bLen, 2, true);

                if(comp < 0 || (comp == 0 && Reads.compareValues(array[t+i], array[t+min]) < 0))
                    min = i;
            }

            if(min != j) {
                this.multiSwap(array, p + j*bLen, p + min*bLen, bLen);
                Writes.swap(array, t+j, t+min, 1, true, false);

                if(k < bCount && min == k-1) k++;
            }
            if(min == mKey) mKey = j;
        }

        return t+mKey;
    }

    //special thanks to @Anonymous0726 for this idea
    private void grailSortKeys(int[] array, int b, int p, int mKey) {
        Writes.swap(array, p, mKey, 1, true, false);
        int i = mKey, j = i+1, k = p+1;

        while(j < b) {
            if(Reads.compareValues(array[j], array[p]) < 0)
                Writes.swap(array, i++, j, 1, true, false);

            else Writes.swap(array, k++, j, 1, true, false);

            j++;
        }

        this.multiSwap(array, i, p, b-i);
    }

    private void grailSortKeysWithoutBuf(int[] array, int b, int mKey) {
        int i = mKey, j = i+1;

        while(j < b) {
            if(Reads.compareValues(array[j], array[i]) < 0)
                this.insertTo(array, j, i++);

            j++;
        }
    }

    //special thanks to @Anonymous0726 for this idea
    private int grailMergeBlocks(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while(i < m && j < b) {
            if(Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);

            else Writes.swap(array, p++, j++, 1, true, false);
        }

        if(i > p) while(i < m) Writes.swap(array, p++, i++, 1, true, false);
        return j;
    }

    //same as grailMergeBlocks() except reverses equal items order
    private int grailMergeBlocksRev(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while(i < m && j < b) {
            if(Reads.compareValues(array[i], array[j]) < 0)
                Writes.swap(array, p++, i++, 1, true, false);

            else Writes.swap(array, p++, j++, 1, true, false);
        }

        if(i > p) while(i < m) Writes.swap(array, p++, i++, 1, true, false);
        return j;
    }

    //is never called if m-a || b-m <= bLen
    //should never be called if (m-a)%bLen != 0
    private void grailBlockMerge(int[] array, int a, int m, int b, int t, int p, int bLen) {
        int b1 = b - (b-m-1)%bLen - 1,
            i = a+bLen, j = a, key = t-1,
            lCount = (m-i)/bLen, bCount = (b1-i)/bLen, l = -1, r = lCount-1;

        this.multiTriSwap(array, p, m-bLen, a, bLen);
        this.insertToBW(array, t, t+lCount-1);

        int mKey = this.blockSelectSort(array, i, t, 1, bLen-1, lCount, bCount, bLen);
        Subarray frag = Subarray.LEFT;

        while(l < lCount && r < bCount) {
            if(frag == Subarray.LEFT) {
                do {
                    j += bLen;
                    l++;
                    key++;
                }
                while(l < lCount && this.grailGetSubarray(array, key, mKey) == Subarray.LEFT);

                if(l == lCount) {
                    i = this.grailMergeBlocks(array, i, j, b, i-bLen);
                    this.mergeWithBufRest(array, i-bLen, i, b, p, bLen);
                }
                else i = this.grailMergeBlocks(array, i, j, j+bLen-1, i-bLen);

                frag = Subarray.RIGHT;
            }
            else {
                do {
                    j += bLen;
                    r++;
                    key++;
                }
                while(r < bCount && this.grailGetSubarray(array, key, mKey) == Subarray.RIGHT);

                if(r == bCount) {
                    this.shift(array, i-bLen, i, b);
                    this.multiSwap(array, p, b-bLen, bLen);
                }
                else i = this.grailMergeBlocksRev(array, i, j, j+bLen-1, i-bLen);

                frag = Subarray.LEFT;
            }
        }

        this.grailSortKeys(array, t+bCount, p, mKey);
    }

    //TODO: rewrite strat 2 merge to be more adaptive

    //1024 items, 8 unique, linear, final merge pass
    //old: 5812 comps & 5762 writes
    //new: ???

    /*private int grailMergeBlocksWithoutBuf(int[] array, int a, int m, int b) {
        int i = a, j = m, k;

        while(i < j && j < b) {
            if(Reads.compareValues(array[i], array[j]) > 0) {
                k = this.leftBinarySearch(array, j+1, b, array[i]);
                this.rotate(array, i, j, k);

                i += k-j;
                j = k;
            }
            else i++;
        }

        return i;
    }

    private int grailMergeBlocksWithoutBufRev(int[] array, int a, int m, int b) {
        int i = a, j = m, k;

        while(i < j && j < b) {
            if(Reads.compareValues(array[i], array[j]) >= 0) {
                k = this.rightBinarySearch(array, j+1, b, array[i]);
                this.rotate(array, i, j, k);

                i += k-j;
                j = k;
            }
            else i++;
        }

        return i;
    }*/

    //old
    private void grailBlockMergeWithoutBuf(int[] array, int a, int m, int b, int t, int bLen) {
        int a1 = a + (m-a)%bLen, b1 = b - (b-m)%bLen,
            i = a, j = a1, key = t,
            lCount = (m-j)/bLen + 1, bCount = (b1-j)/bLen + 1, l = 0, r = lCount;

        int mKey  = this.blockSelectSort(array, j, t, 0, 0, lCount-1, bCount-1, bLen);
        Subarray frag = Subarray.LEFT;

        while(l < lCount && r < bCount) {
            Subarray next = this.grailGetSubarray(array, key++, mKey);

            if(next == frag) {
                if(frag == Subarray.LEFT) l++;
                else                      r++;
                i = j;
            }
            else {//grailMergeBlocksWithoutBuf()
                int m2 = j, b2 = j+bLen, k;

                if(frag == Subarray.LEFT) {
                    while(i < m2 && m2 < b2) {
                        if(Reads.compareValues(array[i], array[m2]) > 0) {
                            k = this.leftBinarySearch(array, m2+1, b2, array[i]);
                            this.rotate(array, i, m2, k);

                            i += k-m2;
                            m2 = k;
                        }
                        else i++;
                    }
                }
                else {
                    while(i < m2 && m2 < b2) {
                        if(Reads.compareValues(array[i], array[m2]) >= 0) {
                            k = this.rightBinarySearch(array, m2+1, b2, array[i]);
                            this.rotate(array, i, m2, k);

                            i += k-m2;
                            m2 = k;
                        }
                        else i++;
                    }
                }

                if(i < m2) {//right side is merged first
                    if(next == Subarray.LEFT) l++;
                    else                      r++;
                }
                else {
                    if(frag == Subarray.LEFT) l++;
                    else                      r++;
                    frag = next;
                }
            }

            j += bLen;
        }

        if(l < lCount) this.inPlaceMergeBW(array, a, b1, b);
        this.grailSortKeysWithoutBuf(array, t+bCount-1, mKey);
    }

    private void smartMerge(int[] array, int a, int m, int b, int p) {
        if(this.checkBounds(array, a, m, b)) {
            a = this.rightBinarySearch(array, a, m-1, array[m]);
            this.mergeWithBuf(array, a, m, b, p);
        }
    }

    private void smartMergeBW(int[] array, int a, int m, int b, int p) {
        if(this.checkBounds(array, a, m, b)) {
            b = this.leftBinarySearch(array, m+1, b, array[m-1]);
            this.mergeWithBufBW(array, a, m, b, p);
        }
    }

    private void smartBlockMerge(int[] array, int a, int m, int b, int t, int p, int bLen) {
        if(this.checkBounds(array, a, m, b)) {
            int n = this.rightBinarySearch(array, a, m-1, array[m]);
            b = this.leftBinarySearch(array, m+1, b, array[m-1]);

            if(this.checkReverseBounds(array, n, m, b)) {
                if(m-n <= bLen || b-m <= bLen) {
                    if(b-m < m-n) this.mergeWithBufBW(array, n, m, b, p);
                    else           this.mergeWithBuf(array, n, m, b, p);
                }
                else {
                    n -= (n-a)%bLen;
                    this.grailBlockMerge(array, n, m, b, t, p, bLen);
                }
            }
        }
    }

    private void smartBlockMergeWithoutBuf(int[] array, int a, int m, int b, int t, int bLen) {
        if(this.checkBounds(array, a, m, b)) {
            a = this.rightBinarySearch(array, a, m-1, array[m]);

            if(m-a <= bLen) this.inPlaceMerge(array, a, m, b);
            else this.grailBlockMergeWithoutBuf(array, a, m, b, t, bLen);
        }
    }

    private void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if(this.checkSorted(array, a, m, b))
            this.inPlaceMergeBW(array, a, m, b);
    }

    private void redistBuffer(int[] array, int a, int m, int b) {
        int rPos = this.leftBinarySearch(array, m, b, array[a]);
        this.rotate(array, a, m, rPos);

        int dist = rPos-m;
        a += dist;
        m += dist;

        int a1 = a+(m-a)/2;
        rPos = this.leftBinarySearch(array, m, b, array[a1]);
        this.rotate(array, a1, m, rPos);

        dist = rPos-m;
        a1  += dist;
        m   += dist;

        this.mergeWithoutBuf(array, a, a1-dist, a1);
        this.mergeWithoutBuf(array, a1, m, b);
    }

    private void redistBufferBW(int[] array, int a, int m, int b) {
        int rPos = this.rightBinarySearch(array, a, m, array[b-1]);
        this.rotate(array, rPos, m, b);

        int dist = m-rPos;
        b -= dist;
        m -= dist;

        int b1 = m+(b-m)/2;
        rPos = this.rightBinarySearch(array, a, m, array[b1-1]);
        this.rotate(array, rPos, m, b1);

        dist = m-rPos;
        b1  -= dist;
        m   -= dist;

        this.mergeWithoutBuf(array, b1, b1+dist, b);
        this.mergeWithoutBuf(array, a, m, b1);
    }

    private void inPlaceMergeSort(int[] array, int a, int b) {
        this.buildRuns(array, a, b);

        int len = b-a;
        for(int i, j = this.minRun; j < len; j *= 2) {
            for(i = a; i + 2*j <= b; i += 2*j)
                this.smartInPlaceMerge(array, i, i+j, i+2*j);

            if(i + j < b)
                this.smartInPlaceMerge(array, i, i+j, b);
        }
    }

    private void grailAdaptiveSortWithoutBuf(int[] array, int a, int b, int keys, int ideal, boolean bwBuf) {
        int len = b-a, bLen;
        for(bLen = Math.min(keys, this.minRun); 2*bLen <= keys; bLen *= 2);
        int tLen = keys-bLen;

        int i, j = this.minRun,
            t, p, a1, b1;

        if(bwBuf) {
            p = b-bLen; a1 = a; b1 = p-tLen; t = b1;
        }
        else {
            p = a+tLen; a1 = p+bLen; b1 = b; t = a;
        }

        //insertion level
        this.buildRuns(array, a1, b1);

        //merge with buffer level
        while(j <= bLen && j < len) {
            for(i = a1; i + 2*j <= b1; i += 2*j)
                this.smartMerge(array, i, i+j, i+2*j, p);

            if(i + j < b1)
                this.smartMergeBW(array, i, i+j, b1, p);

            j *= 2;
        }

        if(bLen/2 >= this.minRun && bLen/2 >= (keys+1)/2) {
            this.binaryInsertion(array, p, p+bLen);

            bLen /= 2;
            tLen = keys-bLen;
            p += bLen;
        }

        //block merge level
        while(tLen >= 2*j/bLen - 1 && j < len) {
            for(i = a1; i + 2*j <= b1; i += 2*j)
                this.smartBlockMerge(array, i, i+j, i+2*j, t, p, bLen);

            if(i + j < b1) {
                if(b1 - (i+j) > bLen)
                    this.smartBlockMerge(array, i, i+j, b1, t, p, bLen);

                else this.smartMergeBW(array, i, i+j, b1, p);
            }

            j *= 2;
        }

        this.binaryInsertion(array, p, p+bLen);
        tLen = keys-keys%2;

        //block merge w/o buffer level
        while(j < len) {
            bLen = 2*j / tLen;

            for(i = a1; i + 2*j <= b1; i += 2*j)
                this.smartBlockMergeWithoutBuf(array, i, i+j, i+2*j, t, bLen);

            if(i + j < b1) {
                if(b1 - (i+j) > bLen)
                    this.smartBlockMergeWithoutBuf(array, i, i+j, b1, t, bLen);

                else this.smartInPlaceMerge(array, i, i+j, b1);
            }

            j *= 2;
        }

        //buffer redistribution
        if(bwBuf) {
            a = this.rightBinarySearch(array, a, b1, array[b1]);
            if(keys >= ideal/2) this.redistBufferBW(array, a, b1, b);
            else                this.mergeWithoutBuf(array, a, b1, b);
        }
        else {
            b = this.leftBinarySearch(array, a1, b, array[a1-1]);
            if(keys >= ideal/2) this.redistBuffer(array, a, a1, b);
            else                this.mergeWithoutBuf(array, a, a1, b);
        }
    }

    protected void grailAdaptiveSort(int[] array, int a, int b) {
        int len = b-a;

        //insertion on small len
        if(len < 31) {
            this.binaryInsertion(array, a, b);
            return;
        }
        //mini adaptive grail sort
        if(len < 63) {
            this.minRun = (len+1)/2;
            this.buildRuns(array, a, b);

            int m = a+this.minRun;
            if(this.checkBounds(array, a, m, b))
                this.redistBufferBW(array, a, m, b);

            return;
        }

        //calculate optimal minRun & block len
        for(this.minRun = len; this.minRun >= 32; this.minRun = (this.minRun+1)/2);

        int bLen;
        for(bLen = this.minRun; bLen*bLen < len; bLen *= 2);

        int tLen  = len/bLen - 2,
            ideal = tLen + bLen;

        //choose direction to find keys
        boolean bwBuf;
        int rRun = this.buildUniqueRunBW(array, b, ideal), lRun = 0;

        if(rRun == ideal) bwBuf = true;
        else {
            lRun = this.buildUniqueRun(array, a, ideal);

            if(lRun == ideal) bwBuf = false;
            else bwBuf = (rRun < 16 && lRun < 16) || rRun >= lRun;
        }

        //find bLen + tLen unique buffer keys
        int keys = bwBuf ? this.findKeysBW(array, a, b, rRun, ideal)
                         : this.findKeys(array, a, b, lRun, ideal);

        if(keys < ideal) {
            if(keys == 1)       return;
            else if(keys <= 4) this.inPlaceMergeSort(array, a, b);
            else               this.grailAdaptiveSortWithoutBuf(array, a, b, keys, ideal, bwBuf);
            return;
        }

        int i, j = this.minRun,
            t, p, a1, b1;

        if(bwBuf) {
            p = b-bLen; a1 = a; b1 = p-tLen; t = b1;
        }
        else {
            p = a+tLen; a1 = p+bLen; b1 = b; t = a;
        }

        //insertion level
        this.buildRuns(array, a1, b1);

        //merge with buffer level
        while(j <= bLen && j < len) {
            for(i = a1; i + 2*j <= b1; i += 2*j)
                this.smartMerge(array, i, i+j, i+2*j, p);

            if(i + j < b1)
                this.smartMergeBW(array, i, i+j, b1, p);

            j *= 2;
        }

        //block merge level
        while(j < len) {
            for(i = a1; i + 2*j <= b1; i += 2*j)
                this.smartBlockMerge(array, i, i+j, i+2*j, t, p, bLen);

            if(i + j < b1) {
                if(b1 - (i+j) > bLen)
                    this.smartBlockMerge(array, i, i+j, b1, t, p, bLen);

                else this.smartMergeBW(array, i, i+j, b1, p);
            }

            j *= 2;
        }

        this.binaryInsertion(array, p, p+bLen);

        //buffer redistribution
        if(bwBuf) {
            a = this.rightBinarySearch(array, a, b1, array[b1]);
            this.redistBufferBW(array, a, b1, b);
        }
        else {
            b = this.leftBinarySearch(array, a1, b, array[a1-1]);
            this.redistBuffer(array, a, a1, b);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.grailAdaptiveSort(array, 0, length);
    }
}

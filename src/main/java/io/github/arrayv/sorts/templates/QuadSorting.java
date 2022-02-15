package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.*;

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
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

// re-implemented to Java, ArrayV by mg-2018, 2021
// 210704 ~ 210816

// things in quadsort.h
class QuadSortBase {
    int pta, end, ptt, key;
    // original C version uses pointer trick, had to refactor using index instead

    Reads Reads;
    Writes Writes;
    Highlights Highlights;
    Delays Delays;

    public QuadSortBase(ArrayVisualizer arrayVisualizer) {
        Reads = arrayVisualizer.getReads();
        Writes = arrayVisualizer.getWrites();
        Highlights = arrayVisualizer.getHighlights();
        Delays = arrayVisualizer.getDelays();
    }

    void swapTwo(int[] array, int start) {
        if(Reads.compareIndices(array, start, start+1, 1, true) > 0)
            Writes.swap(array, start, start+1, 1, true, false);
    }

    void swapThree(int[] array, int start) {
        if(Reads.compareIndices(array, start, start+1, 1, true) > 0) {
            if(Reads.compareIndices(array, start, start+2, 1, true) <= 0)
                Writes.swap(array, start, start+1, 1, true, false);

            else if(Reads.compareIndices(array, start+1, start+2, 1, true) > 0)
                Writes.swap(array, start, start+2, 1, true, false);

            else {
                int temp = array[start];
                Writes.write(array, start, array[start+1], 1, true, false);
                Writes.write(array, start+1, array[start+2], 1, true, false);
                Writes.write(array, start+2, temp, 1, true, false);
            }
        }

        else if(Reads.compareIndices(array, start+1, start+2, 1, true) > 0) {
            if(Reads.compareIndices(array, start, start+2, 1, true) > 0) {
                int temp = array[start+2];
                Writes.write(array, start+2, array[start+1], 1, true, false);
                Writes.write(array, start+1, array[start], 1, true, false);
                Writes.write(array, start, temp, 1, true, false);
            }

            else {
                Writes.swap(array, start+2, start+1, 1, true, false);
            }
        }
    }

    void swapFour(int[] array, int start) {
        if(Reads.compareIndices(array, start, start+1, 1, true) > 0)
            Writes.swap(array, start, start+1, 1, true, false);

        if(Reads.compareIndices(array, start+2, start+3, 1, true) > 0)
            Writes.swap(array, start+2, start+3, 1, true, false);

        if(Reads.compareIndices(array, start+1, start+2, 1, true) > 0) {
            if(Reads.compareIndices(array, start, start+2, 1, true) <= 0) {
                if(Reads.compareIndices(array, start+1, start+3, 1, true) <= 0)
                    Writes.swap(array, start+1, start+2, 1, true, false);

                else {
                    int temp = array[start+1];
                    Writes.write(array, start+1, array[start+2], 1, true, false);
                    Writes.write(array, start+2, array[start+3], 1, true, false);
                    Writes.write(array, start+3, temp, 1, true, false);
                }
            }

            else if(Reads.compareIndices(array, start, start+3, 1, true) > 0) {
                Writes.swap(array, start+1, start+3, 1, true, false);
                Writes.swap(array, start, start+2, 1, true, false);
            }

            else if(Reads.compareIndices(array, start+1, start+3, 1, true) <= 0) {
                int temp = array[start+1];
                Writes.write(array, start+1, array[start], 1, true, false);
                Writes.write(array, start, array[start+2], 1, true, false);
                Writes.write(array, start+2, temp, 1, true, false);
            }

            else {
                int temp = array[start+1];
                Writes.write(array, start+1, array[start], 1, true, false);
                Writes.write(array, start, array[start+2], 1, true, false);
                Writes.write(array, start+2, array[start+3], 1, true, false);
                Writes.write(array, start+3, temp, 1, true, false);
            }
        }
    }

    void swapFive(int[] array, int start) {
        end = start+4;
        pta = end++;
        ptt = pta--;

        if(Reads.compareIndices(array, pta, ptt, 1, true) > 0) {
            key = array[ptt];
            Writes.write(array, ptt--, array[pta--], 1, true, false);

            if(pta > start && Reads.compareValues(array[pta-1], key) > 0) {
                Writes.write(array, ptt--, array[pta--], 1, true, false);
                Writes.write(array, ptt--, array[pta--], 1, true, false);
            }

            if(pta >= start && Reads.compareValues(array[pta], key) > 0) {
                Writes.write(array, ptt--, array[pta--], 1, true, false);
            }

            array[ptt] = key;
        }
    }

    void tailSwapEight(int[] array, int start) {
        pta = end++;
        ptt = pta--;

        if(Reads.compareIndices(array, pta, ptt, 1, true) > 0) {
            key = array[ptt];
            Writes.write(array, ptt--, array[pta--], 1, true, false);

            if(Reads.compareValues(array[pta-2], key) > 0) {
                for(int i=0; i<3; i++)
                    Writes.write(array, ptt--, array[pta--], 1, true, false);
            }

            if(pta > start && Reads.compareValues(array[pta-1], key) > 0) {
                Writes.write(array, ptt--, array[pta--], 1, true, false);
                Writes.write(array, ptt--, array[pta--], 1, true, false);
            }

            if(pta >= start && Reads.compareValues(array[pta], key) > 0) {
                Writes.write(array, ptt--, array[pta--], 1, true, false);
            }

            array[ptt] = key;
        }
    }

    void swapSix(int[] array, int start) {
        this.swapFive(array, start);
        this.tailSwapEight(array, start);
    }

    void swapSeven(int[] array, int start) {
        this.swapSix(array, start);
        this.tailSwapEight(array, start);
    }

    void swapEight(int[] array, int start) {
        this.swapSeven(array, start);
        this.tailSwapEight(array, start);
    }
}

// things in quadsort.c
public abstract class QuadSorting extends Sort {
    QuadSortBase qs;

    public QuadSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        qs = new QuadSortBase(arrayVisualizer);
    }

    // ~4 items: sorting network
    // 5~ items: unguarded insertion sort
    protected void tailSwap(int[] array, int start, int nmemb) {
        int mid, top, offset;

        switch(nmemb) {
        case 0:
        case 1:
            return;

        case 2:
            qs.swapTwo(array, start);
            return;

        case 3:
            qs.swapThree(array, start);
            return;

        case 4:
            qs.swapFour(array, start);
            return;

        case 5:
            qs.swapFour(array, start);
            qs.swapFive(array, start);
            return;

        case 6:
            qs.swapFour(array, start);
            qs.swapSix(array, start);
            return;

        case 7:
            qs.swapFour(array, start);
            qs.swapSeven(array, start);
            return;

        case 8:
            qs.swapFour(array, start);
            qs.swapEight(array, start);
            return;
        }

        qs.swapFour(array, start);
        qs.swapEight(array, start);

        qs.end = start+8;
        offset = 8;

        while(offset < nmemb) {
            top = offset++;
            qs.pta = qs.end++;
            qs.ptt = qs.pta--;

            if(Reads.compareIndices(array, qs.pta, qs.ptt, 1, true) <= 0)
                continue;

            int temp = array[qs.ptt];

            while(top > 1) {
                mid = top/2;
                if(Reads.compareValues(array[qs.pta-mid], temp) > 0)
                    qs.pta -= mid;

                top -= mid;
            }

            // memmove(pta+1, pta, (ptt-pta) * sizeof(VAR));
            for(int i=qs.ptt; i > qs.pta; i--)
                Writes.write(array, i, array[i-1], 1, true, false);

            Writes.write(array, qs.pta, temp, 1, true, false);
        }

        Highlights.clearAllMarks();
    }

    // merge 4 4 into 8 from main array to aux array
    // auxOffset must be either 0 or 8
    void parityMerge4(int[] from, int start, int[] dest, int auxOffset) {
        int ptl, ptr;
        int auxP = auxOffset;

        ptl = start;
        ptr = start+4;

        for(int i=0; i<3; i++) {
            Highlights.markArray(2, ptl);
            Highlights.markArray(3, ptr);

            if(Reads.compareValues(from[ptl], from[ptr]) <= 0)
                Writes.write(dest, auxP++, from[ptl++], 1, true, true);

            else
                Writes.write(dest, auxP++, from[ptr++], 1, true, true);
        }

        Highlights.markArray(2, ptl);
        Highlights.markArray(3, ptr);

        if(Reads.compareValues(from[ptl], from[ptr]) <= 0)
            Writes.write(dest, auxP, from[ptl], 1, true, true);

        else
            Writes.write(dest, auxP, from[ptr], 1, true, true);

        ptl = start+3;
        ptr = start+7;
        auxP += 4;

        for(int i=0; i<3; i++) {
            Highlights.markArray(2, ptl);
            Highlights.markArray(3, ptr);

            if(Reads.compareValues(from[ptl], from[ptr]) > 0)
                Writes.write(dest, auxP--, from[ptl--], 1, true, true);

            else
                Writes.write(dest, auxP--, from[ptr--], 1, true, true);
        }

        Highlights.markArray(2, ptl);
        Highlights.markArray(3, ptr);

        if(Reads.compareValues(from[ptl], from[ptr]) > 0)
            Writes.write(dest, auxP, from[ptl], 1, true, true);

        else
            Writes.write(dest, auxP, from[ptr], 1, true, true);

        Highlights.clearAllMarks();
    }

    // merge 8 8 into 16 from aux array to main array
    void parityMerge8(int[] from, int start, int[] dest) {
        int ptl, ptr;
        int mainP = start;

        ptl = 0;
        ptr = 8;

        for(int i=0; i<7; i++) {
            Highlights.markArray(2, ptl);
            Highlights.markArray(3, ptr);

            if(Reads.compareValues(from[ptl], from[ptr]) <= 0)
                Writes.write(dest, mainP++, from[ptl++], 1, true, false);

            else
                Writes.write(dest, mainP++, from[ptr++], 1, true, false);
        }

        Highlights.markArray(2, ptl);
        Highlights.markArray(3, ptr);

        if(Reads.compareValues(from[ptl], from[ptr]) <= 0)
            Writes.write(dest, mainP, from[ptl], 1, true, false);

        else
            Writes.write(dest, mainP, from[ptr], 1, true, false);

        ptl = 7;
        ptr = 15;
        mainP += 8;

        for(int i=0; i<7; i++) {
            Highlights.markArray(2, ptl);
            Highlights.markArray(3, ptr);

            if(Reads.compareValues(from[ptl], from[ptr]) > 0)
                Writes.write(dest, mainP--, from[ptl--], 1, true, false);

            else
                Writes.write(dest, mainP--, from[ptr--], 1, true, false);
        }

        Highlights.markArray(2, ptl);
        Highlights.markArray(3, ptr);

        if(Reads.compareValues(from[ptl], from[ptr]) > 0)
            Writes.write(dest, mainP, from[ptl], 1, true, false);

        else
            Writes.write(dest, mainP, from[ptr], 1, true, false);

        Highlights.clearAllMarks();
    }

    // merge 4 4 4 4 into 16 using the above two methods
    // with analyzing already sorted runs
    void parityMerge16(int[] array, int start, int[] aux) {
        if(Reads.compareIndices(array, start+3, start+4, 4, true) <= 0 &&
            Reads.compareIndices(array, start+7, start+8, 4, true) <= 0 &&
            Reads.compareIndices(array, start+11, start+12, 4, true) <= 0)
            return;

        this.parityMerge4(array, start, aux, 0);
        this.parityMerge4(array, start+8, aux, 8);

        this.parityMerge8(aux, start, array);
    }

    // (partially) writes second block into aux array and then merge with first block
    void partialBackwardMerge(int[] array, int[] aux, int start, int nmemb, int block) {
        int r, m, e, s;
        // right, middle, end, swap

        m = start + block;
        e = start + nmemb - 1;
        r = m--;

        if(Reads.compareIndices(array, m, r, 1, true) <= 0)
            return;

        while(Reads.compareIndices(array, m, e, 1, true) <= 0)
            e--;

        Highlights.clearAllMarks();
        for(int i=r; i < r+(e-m); i++) {
            Writes.write(aux, i-r, array[i], 1, false, true);
            Highlights.markArray(1, i);
        }

        s = e-r;
        Writes.write(array, e--, array[m--], 1, true, false);

        if(Reads.compareValues(array[start], aux[0]) <= 0) {
            do {
                while(Reads.compareValues(array[m], aux[s]) > 0) {
                    Highlights.markArray(2, m);
                    Writes.write(array, e--, array[m--], 1, true, false);
                }

                Highlights.markArray(2, m);
                Writes.write(array, e--, aux[s--], 1, true, false);
            } while(s >= 0);
        }

        else {
            do {
                while(Reads.compareValues(array[m], aux[s]) <= 0) {
                    Highlights.markArray(2, m);
                    Writes.write(array, e--, aux[s--], 1, true, false);
                }

                Highlights.markArray(2, m);
                Writes.write(array, e--, array[m--], 1, true, false);
            } while(m >= start);

            do Writes.write(array, e--, aux[s--], 1, true, false); while (s >= 0);
        }
    }

    // bottom up merge sort, performs on array size from 16 to 255 after parity merge
    // or performs after quad merge on array size from 256
    void tailMerge(int[] array, int[] aux, int start, int nmemb, int block) {
        int pta, pte;
        // different from qs.pta

        pte = start + nmemb;
        while(block < nmemb) {
            pta = start;

            for (pta = start ; pta + block < pte ; pta += block * 2)
            {
                if (pta + block * 2 < pte)
                {
                    this.partialBackwardMerge(array, aux, pta, block*2, block);

                    continue;
                }
                this.partialBackwardMerge(array, aux, pta, pte - pta, block);

                break;
            }
            block *= 2;
        }
    }

    // normal merge method with some optimizations
    // like analyzing already sorted runs
    void forwardMerge(int[] dest, int[] from, int start, int auxStart, int block, boolean toAux) {
        int l, r, m, e;
        // left, right, middle, end
        int mergeP = toAux ? auxStart : start;

        l = toAux ? start : auxStart;
        r = toAux ? (start+block) : (auxStart+block);
        m = r;
        e = r + block;

        Highlights.clearAllMarks();
        if(toAux) {
            Highlights.markArray(1, r-1);
            Highlights.markArray(2, e-1);
            Delays.sleep(1);
        }

        if(Reads.compareValues(from[r-1], from[e-1]) <= 0) {
            leftFirst:
            do {
                for(int i=0; i<3; i++) {
                    if(Reads.compareValues(from[l], from[r]) <= 0) {
                        if(toAux) {
                            Highlights.markArray(1, l);
                            Highlights.markArray(2, r);
                            Delays.sleep(1);
                        }
                        Writes.write(dest, mergeP++, from[l++], toAux ? 0 : 1, !toAux, toAux);
                        continue leftFirst;
                    }

                    if(toAux) {
                        Highlights.markArray(1, l);
                        Highlights.markArray(2, r);
                        Delays.sleep(1);
                    }
                    Writes.write(dest, mergeP++, from[r++], toAux ? 0 : 1, !toAux, toAux);
                }
            } while(l < m);

            do {
                if(toAux) {
                    Highlights.markArray(1, l-1);
                    Highlights.markArray(2, r);
                    Delays.sleep(1);
                }
                Writes.write(dest, mergeP++, from[r++], toAux ? 0 : 1, !toAux, toAux);
            } while(r < e);
        }

        else {
            rightFirst:
            do {
                for(int i=0; i<3; i++) {
                    if(Reads.compareValues(from[l], from[r]) > 0) {
                        if(toAux) {
                            Highlights.markArray(1, l);
                            Highlights.markArray(2, r);
                            Delays.sleep(1);
                        }
                        Writes.write(dest, mergeP++, from[r++], toAux ? 0 : 1, !toAux, toAux);
                        continue rightFirst;
                    }

                    if(toAux) {
                        Highlights.markArray(1, l);
                        Highlights.markArray(2, r);
                        Delays.sleep(1);
                    }
                    Writes.write(dest, mergeP++, from[l++], toAux ? 0 : 1, !toAux, toAux);
                }
            } while(r < e);

            do {
                if(toAux) {
                    Highlights.markArray(1, l);
                    Highlights.markArray(2, r-1);
                    Delays.sleep(1);
                }
                Writes.write(dest, mergeP++, from[l++], toAux ? 0 : 1, !toAux, toAux);
            } while(l < m);
        }
    }

    // main memory: [A][B][C][D]
    // swap memory: [A  B]       step 1
    // swap memory: [A  B][C  D] step 2
    // main memory: [A  B  C  D] step 3

    // merge 4 blocks into 1 block like above comment
    void quadMergeBlock(int[] array, int start, int[] aux, int block) {
        int pts, c, cMax;
        int blockX2 = block*2;

        cMax = start + block;

        // if first 2 blocks are sorted
        if(Reads.compareIndices(array, cMax-1, cMax, 20, true) <= 0) {
            cMax += blockX2;

            // if second 2 blocks are sorted
            if(Reads.compareIndices(array, cMax-1, cMax, 20, true) <= 0) {
                cMax -= block;

                // ...and entire 4 blocks are sorted
                if(Reads.compareIndices(array, cMax-1, cMax, 20, true) <= 0) {
                    return;
                }

                pts = 0;
                c = start;

                do {
                    Writes.write(aux, pts, array[c++], 1, false, true);
                    Highlights.markArray(1, pts+start);
                    pts++;
                } while(c < cMax); // step 1

                cMax = c + blockX2;
                do {
                    Writes.write(aux, pts, array[c++], 1, false, true);
                    Highlights.markArray(1, pts+start);
                    pts++;
                } while(c < cMax); // step 2

                this.forwardMerge(array, aux, start, 0, blockX2, false); // step 3
                return;
            }

            pts = 0;
            c = start;
            cMax = start + blockX2;

            do {
                Writes.write(aux, pts, array[c++], 1, false, true);
                Highlights.markArray(1, pts+start);
                pts++;
            } while(c < cMax); // step 1
        }

        else
            this.forwardMerge(aux, array, start, 0, block, true); // step 1

        this.forwardMerge(aux, array, start+blockX2, blockX2, block, true); // step 2
        this.forwardMerge(array, aux, start, 0, blockX2, false); // step 3
    }

    // quad merges the entire array
    // this fallbacks to tail merge if (current block size)*2 is greater than array size
    void quadMerge(int[] array, int[] aux, int start, int nmemb, int block) {
        int pta, pte;
        // different from qs.pta

        pte = start + nmemb;
        block *= 4;

        while(block*2 <= nmemb) {
            pta = start;
            do {
                this.quadMergeBlock(array, pta, aux, block/4);

                pta += block;
            } while(pta+block <= pte);

            this.tailMerge(array, aux, pta, pte-pta, block/4);

            block *= 4;
        }
        this.tailMerge(array, aux, start, nmemb, block/4);
    }

    // pre-sorting process; basically 4-item sorting network
    // detects strictly decreasing runs and reverses them
    // if entire array is decreasing this performs only 1 reversal and returns
    // else this performs parity merge (and tail merge / tail swap)
    int quadSwap(int[] array, int start, int nmemb) {
        int[] swap = Writes.createExternalArray(16);
        int count, reverse;
        int pta, pts = 0, ptt = 0, temp = 0;

        pta = start;
        count = nmemb/4;

        swapper:
        while(count-- > 0) {
            while(true) {
                if(Reads.compareIndices(array, pta, pta+1, 1, true) > 0) {
                    if(Reads.compareIndices(array, pta+2, pta+3, 1, true) > 0) {
                        if(Reads.compareIndices(array, pta+1, pta+2, 1, true) > 0) {
                            pts = pta;
                            pta += 4;
                            break;
                        }

                        Writes.swap(array, pta+2, pta+3, 1, true, false);
                    }
                    Writes.swap(array, pta, pta+1, 1, true, false);
                }

                else if(Reads.compareIndices(array, pta+2, pta+3, 1, true) > 0)
                    Writes.swap(array, pta+2, pta+3, 1, true, false);

                if(Reads.compareIndices(array, pta+1, pta+2, 1, true) > 0) {
                    if(Reads.compareIndices(array, pta, pta+2, 1, true) <= 0) {
                        if(Reads.compareIndices(array, pta+1, pta+3, 1, true) <= 0)
                            Writes.swap(array, pta+1, pta+2, 1, true, false);

                        else {
                            temp = array[pta+1];
                            Writes.write(array, pta+1, array[pta+2], 1, true, false);
                            Writes.write(array, pta+2, array[pta+3], 1, true, false);
                            Writes.write(array, pta+3, temp, 1, true, false);
                        }
                    }

                    else if(Reads.compareIndices(array, pta, pta+3, 1, true) > 0) {
                        Writes.swap(array, pta+1, pta+3, 1, true, false);
                        Writes.swap(array, pta, pta+2, 1, true, false);
                    }

                    else if(Reads.compareIndices(array, pta+1, pta+3, 1, true) <= 0) {
                        temp = array[pta+1];
                        Writes.write(array, pta+1, array[pta], 1, true, false);
                        Writes.write(array, pta, array[pta+2], 1, true, false);
                        Writes.write(array, pta+2, temp, 1, true, false);
                    }

                    else {
                        temp = array[pta+1];
                        Writes.write(array, pta+1, array[pta], 1, true, false);
                        Writes.write(array, pta, array[pta+2], 1, true, false);
                        Writes.write(array, pta+2, array[pta+3], 1, true, false);
                        Writes.write(array, pta+3, temp, 1, true, false);
                    }
                }
                pta += 4;
                continue swapper;
            }

            while(true) {
                if(count-- > 0) {
                    if(Reads.compareIndices(array, pta, pta+1, 1, true) > 0) {
                        if(Reads.compareIndices(array, pta+2, pta+3, 1, true) > 0) {
                            if(Reads.compareIndices(array, pta+1, pta+2, 1, true) > 0) {
                                if(Reads.compareIndices(array, pta-1, pta, 1, true) > 0) {
                                    pta += 4;
                                    continue;
                                }
                            }
                            Writes.swap(array, pta+2, pta+3, 1, true, false);
                        }
                        Writes.swap(array, pta, pta+1, 1, true, false);
                    }

                    else if(Reads.compareIndices(array, pta+2, pta+3, 1, true) > 0)
                        Writes.swap(array, pta+2, pta+3, 1, true, false);

                    if(Reads.compareIndices(array, pta+1, pta+2, 1, true) > 0) {
                        if(Reads.compareIndices(array, pta, pta+2, 1, true) <= 0) {
                            if(Reads.compareIndices(array, pta+1, pta+3, 1, true) <= 0)
                                Writes.swap(array, pta+1, pta+2, 1, true, false);

                            else {
                                temp = array[pta+1];
                                Writes.write(array, pta+1, array[pta+2], 1, true, false);
                                Writes.write(array, pta+2, array[pta+3], 1, true, false);
                                Writes.write(array, pta+3, temp, 1, true, false);
                            }
                        }

                        else if(Reads.compareIndices(array, pta, pta+3, 1, true) > 0) {
                            Writes.swap(array, pta, pta+2, 1, true, false);
                            Writes.swap(array, pta+1, pta+3, 1, true, false);
                        }

                        else if(Reads.compareIndices(array, pta+1, pta+3, 1, true) <= 0) {
                            temp = array[pta];
                            Writes.write(array, pta, array[pta+2], 1, true, false);
                            Writes.write(array, pta+2, array[pta+1], 1, true, false);
                            Writes.write(array, pta+1, temp, 1, true, false);
                        }

                        else {
                            temp = array[pta];
                            Writes.write(array, pta, array[pta+2], 1, true, false);
                            Writes.write(array, pta+2, array[pta+3], 1, true, false);
                            Writes.write(array, pta+3, array[pta+1], 1, true, false);
                            Writes.write(array, pta+1, temp, 1, true, false);
                        }
                    }

                    ptt = pta-1;
                    reverse = (ptt-pts)/2;

                    Writes.changeReversals(1);
                    do {
                        Writes.swap(array, pts++, ptt--, 1, true, false);
                    } while(reverse-- > 0);

                    pta += 4;
                    continue swapper;
                }

                if(pts == start) {
                    switch(nmemb % 4) {
                    case 3:
                        if(Reads.compareIndices(array, pta+1, pta+2, 1, true) <= 0)
                            break;

                    case 2:
                        if(Reads.compareIndices(array, pta, pta+1, 1, true) <= 0)
                            break;

                    case 1:
                        if(Reads.compareIndices(array, pta-1, pta, 1, true) <= 0)
                            break;

                    case 0:
                        ptt = pts+nmemb-1;
                        reverse = (ptt-pts)/2;

                        Writes.changeReversals(1);
                        do {
                            Writes.swap(array, pts++, ptt--, 1, true, false);
                        } while(reverse-- > 0);

                        Writes.deleteExternalArray(swap);
                        return 1;
                    }
                }

                ptt = pta - 1;
                reverse = (ptt-pts)/2;
                Writes.changeReversals(1);
                do {
                    Writes.swap(array, pts++, ptt--, 1, true, false);
                } while(reverse-- > 0);
                break swapper;
            }
        }
        this.tailSwap(array, pta, nmemb%4);

        pta = start;
        count = nmemb/16;
        while(count-- > 0) {
            this.parityMerge16(array, pta, swap);
            pta += 16;
        }

        if(nmemb%16 > 4)
            this.tailMerge(array, swap, pta, nmemb%16, 4);

        Writes.deleteExternalArray(swap);
        return 0;
    }

    // main sorting method
    protected void quadSort(int[] array, int start, int length) {
        if(length < 16) {
            this.tailSwap(array, start, length);
        }

        else if(length < 256) {
            if(this.quadSwap(array, start, length) == 0) {
                int[] swap = Writes.createExternalArray(128);
                this.tailMerge(array, swap, start, length, 16);
                Writes.deleteExternalArray(swap);
            }
        }

        else {
            if(this.quadSwap(array, start, length) == 0) {
                int[] swap = Writes.createExternalArray(length/2);
                this.quadMerge(array, swap, start, length, 16);
                Writes.deleteExternalArray(swap);
            }
        }
    }

    // main sorting method with given swap array
    protected void quadSortSwap(int[] array, int[] swap, int start, int length) {
        if(length < 16) {
            this.tailSwap(array, start, length);
        }

        else if(length < 256) {
            if(this.quadSwap(array, start, length) == 0)
                this.tailMerge(array, swap, start, length, 16);
        }

        else {
            if(this.quadSwap(array, start, length) == 0)
                this.quadMerge(array, swap, start, length, 16);
        }
    }
}

package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.GrailSorting;

/*
 *
The MIT License (MIT)

Copyright (c) 2013 Andrey Astrelin

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

/********* Lazy stable sorting ***********/
/*                                       */
/* (c) 2013 by Andrey Astrelin           */
/* Refactored by MusicTheorist           */
/*                                       */
/* Simple in-place stable sorting,       */
/* methods copied from Grail Sort        */
/*                                       */
/*****************************************/

public final class OptimizedLazyStableSort extends GrailSorting {
    public OptimizedLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Lazy Stable");
        this.setRunAllSortsName("Optimized Lazy Stable Sort");
        this.setRunSortName("Optimized Lazy Stable Sort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while(i < b) {
            int current = array[i];
            int pos = i - 1;
            while(pos >= a && Reads.compareValues(array[pos], current) > 0){
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
            }
            Writes.write(array, pos + 1, current, sleep, true, auxwrite);

            i++;
        }
    }

    protected void grailLazyStableSort(int[] arr, int pos, int len) {
        int dist;
        for (dist = 0; dist + 16 < len; dist += 16)
            insertionSort(arr, pos + dist, pos + dist + 16, 1, false);
        if (dist < len)
            insertionSort(arr, pos + dist, pos + len, 1, false);

        for(int part = 16; part < len; part *= 2) {
            int left = 0;
            int right = len - 2 * part;

            while(left <= right) {
                this.grailMergeWithoutBuffer(arr, pos + left, part, part);
                left += 2 * part;
            }

            int rest = len - left;
            if(rest > part) {
                this.grailMergeWithoutBuffer(arr, pos + left, part, rest - part);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.grailLazyStableSort(array, 0, length);
    }
}

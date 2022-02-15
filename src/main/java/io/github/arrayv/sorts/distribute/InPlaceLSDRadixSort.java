package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

public final class InPlaceLSDRadixSort extends Sort {
    public InPlaceLSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place LSD Radix");
        //this.setRunAllID("In-Place LSD Radix Sort, Base 2");
        this.setRunAllSortsName("In-Place LSD Radix Sort, Base 10");
        this.setRunSortName("In-Place LSD Radix Sort");
        this.setCategory("Distribution Sorts");
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.setRunAllSortsName("In-Place LSD Radix Sort, Base " + bucketCount);

        int pos = 0;
        int[] vregs = new int[bucketCount - 1];
        Writes.changeAllocAmount(vregs.length);

        int maxpower = Reads.analyzeMaxLog(array, sortLength, bucketCount, 0.5, true);

        for(int p = 0; p <= maxpower; p++){
            for(int i = 0; i < vregs.length; i++) {
                Writes.write(vregs, i, sortLength - 1, 0, false, true);
            }

            pos = 0;

            for(int i = 0; i < sortLength; i++){
                int digit = Reads.getDigit(array[pos], p, bucketCount);

                if(digit == 0) {
                    pos++;
                    Highlights.markArray(0, pos);
                }
                else {
                    for(int j = 0; j < vregs.length;j++)
                        Highlights.markArray(j + 1, vregs[j]);

                    Writes.multiSwap(array, pos, vregs[digit - 1], bucketCount / 10000d, false, false);

                    for(int j = digit - 1; j > 0; j--) {
                        Writes.write(vregs, j - 1, vregs[j - 1] - 1, 0, false, true);
                    }
                }
            }
        }

        Writes.changeAllocAmount(-vregs.length);
    }
}

package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

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
@SortMeta(name = "Simple Static (Index)")
public final class IndexSort extends Sort {
    public IndexSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int min = Reads.analyzeMin(array, sortLength, 0.5, true);

        for (int i = 0; i < sortLength; i++) {
            Highlights.markArray(3, i);
            int cmpCount = 0;
            while (Reads.compareValues(i, array[i] - min) != 0 && cmpCount < sortLength) {
                Writes.swap(array, i, array[i] - min, 0.5, true, false);
                cmpCount++;
            }
            if (cmpCount >= sortLength - 1)
                break;
            Highlights.clearMark(1);
            Highlights.clearMark(2);
            Delays.sleep(1);
        }
    }
}

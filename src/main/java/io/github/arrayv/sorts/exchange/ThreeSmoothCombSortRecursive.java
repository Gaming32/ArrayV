package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2019 PiotrGrochowski
Copyright (c) 2020 aphitorite
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
@SortMeta(listName = "3-Smooth Comb (Recursive)", runName = "Recursive 3-Smooth Comb")
public final class ThreeSmoothCombSortRecursive extends Sort {
    public ThreeSmoothCombSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void recursiveComb(int[] array, int pos, int gap, int end) {
        if (pos + gap > end)
            return;

        this.recursiveComb(array, pos, gap * 2, end);
        this.recursiveComb(array, pos + gap, gap * 2, end);

        this.powerOfThree(array, pos, gap, end);
    }

    private void powerOfThree(int[] array, int pos, int gap, int end) {
        if (pos + gap > end)
            return;

        this.powerOfThree(array, pos, gap * 3, end);
        this.powerOfThree(array, pos + gap, gap * 3, end);
        this.powerOfThree(array, pos + 2 * gap, gap * 3, end);

        for (int i = pos; i + gap < end; i += gap)
            if (Reads.compareIndices(array, i, i + gap, 0.5, true) == 1)
                Writes.swap(array, i, i + gap, 0.5, false, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.recursiveComb(array, 0, 1, length);
    }
}

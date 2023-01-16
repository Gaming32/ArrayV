package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2019 PiotrGrochowski
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

@SortMeta(
    listName = "Pairwise (Recursive)",
    showcaseName = "Recursive Pairwise Sorting Network",
    runName = "Recursive Pairwise Sort"
)
public final class PairwiseSortRecursive extends Sort {
    public PairwiseSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void pairwiserecursive(int[] array, int start, int end, int gap, double sleep) {
        if (start == end - gap){
            return;
        }
        int b = start + gap;
        while (b < end){
            if(Reads.compareIndices(array, b - gap, b, sleep, true) == 1) {
                Writes.swap(array, b - gap, b, sleep, true, false);
            }
            b += (2 * gap);
        }
        if (((end - start) / gap)%2 == 0){
            this.pairwiserecursive(array, start, end, gap * 2, sleep);
            this.pairwiserecursive(array, start + gap, end + gap, gap * 2, sleep);
        }
        else{
            this.pairwiserecursive(array, start, end + gap, gap * 2, sleep);
            this.pairwiserecursive(array, start + gap, end, gap * 2, sleep);
        }
        int a = 1;
        while (a < ((end - start) / gap)){
            a = (a * 2) + 1;
        }
        b = start + gap;
        while (b + gap < end){
            int c = a;
            while (c > 1){
                c /= 2;
                if (b + (c * gap) < end){
                    if(Reads.compareIndices(array, b, b + (c * gap), sleep, true) == 1) {
                        Writes.swap(array, b, b + (c * gap), sleep, true, false);
                    }
                }
            }
            b += (2 * gap);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.pairwiserecursive(array, 0, sortLength, 1, 0.5);
    }
}

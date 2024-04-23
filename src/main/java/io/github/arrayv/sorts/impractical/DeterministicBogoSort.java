package io.github.arrayv.sorts.impractical;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

/* MIT License
Copyright (c) 2020 Walker Gray
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
*/

/**
 * Deterministic Bogosort cycles through
 * every permutation of the array until it is sorted.
 * <p>
 * Uses Heap's algorithm.
 */
@SortMeta(name = "Deterministic Bogo", slowSort = true, unreasonableLimit = 11)
public final class DeterministicBogoSort extends BogoSorting {

    public DeterministicBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private boolean permutationSort(int[] array, int depth, int length) {
        if (depth >= length - 1)
            return this.isArraySorted(array, length);

        for (int i = length - 1; i > depth; --i) {
            if (permutationSort(array, depth + 1, length))
                return true;

            if ((length - depth) % 2 == 0)
                Writes.swap(array, depth, i, this.delay, true, false);
            else
                Writes.swap(array, depth, length - 1, this.delay, true, false);
        }
        return permutationSort(array, depth + 1, length);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        permutationSort(array, 0, length);
    }
}

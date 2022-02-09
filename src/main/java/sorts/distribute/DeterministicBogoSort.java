package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

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
public final class DeterministicBogoSort extends BogoSorting {

    public DeterministicBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Deterministic Bogo");
        this.setRunAllSortsName("Deterministic Bogo Sort");
        this.setRunSortName("Deterministic Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    private boolean permutationSort(int[] array, int depth, int length) {
        if (depth >= length-1)
            return this.isArraySorted(array, length);

        for (int i = length-1; i > depth; --i) {
            if (permutationSort(array, depth+1, length))
                return true;

            if ((length-depth)%2 == 0)
                Writes.swap(array, depth, i, this.delay, true, false);
            else
                Writes.swap(array, depth, length-1, this.delay, true, false);
        }
        return permutationSort(array, depth+1, length);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        permutationSort(array, 0, length);
    }
}

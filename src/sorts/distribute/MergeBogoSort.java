package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*
 *
MIT License

Copyright (c) 2021 EmeraldBlock

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

public final class MergeBogoSort extends BogoSorting {
    public MergeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Merge Bogo");
        this.setRunAllSortsName("Merge Bogo Sort");
        this.setRunSortName("Merge Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(22);
        this.setBogoSort(true);
    }

    private void bogoWeave(int[] array, int[] tmp, int start, int mid, int end) {
        int size = mid-start;
        this.bogoCombo(array, start, end, size, false);

        int low = start;
        int high = mid;
        for (int i = start; i < end; ++i) {
            if (Reads.compareValues(array[i], 0) == 0)
                Writes.write(array, i, tmp[low++], 0, false, true);
            else
                Writes.write(array, i, tmp[high++], 0, false, true);
        }
    }

    private void mergeBogo(int[] array, int[] tmp, int start, int end) {
        if (start >= end-1) return;

        int mid = (start+end)/2;
        mergeBogo(array, tmp, start, mid);
        mergeBogo(array, tmp, mid, end);

        for (int i = start; i < end; ++i)
            Writes.write(tmp, i, array[i], 0, true, false);

        while (!this.isRangeSorted(array, start, end))
            bogoWeave(array, tmp, start, mid, end);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] tmp = Writes.createExternalArray(sortLength);

        mergeBogo(array, tmp, 0, sortLength);

        Writes.deleteExternalArray(tmp);
    }
}

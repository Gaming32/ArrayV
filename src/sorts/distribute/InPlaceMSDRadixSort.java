package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class InPlaceMSDRadixSort extends Sort {
    public InPlaceMSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("In-Place MSD Radix");
        this.setRunAllSortsName("In-Place MSD Radix Sort, Base 4");
        this.setRunSortName("In-Place MSD Radixsort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void radixMSD(int[] array, int length, int min, int max, int radix, int pow, double sleep) {
        if(min >= max || pow < 0)
            return;
        
        Highlights.markArray(3, max - 1);
        Highlights.markArray(4, min);
        
        int[] indices = new int[radix];
        Writes.changeAllocAmount(indices.length);
        for (int i = 0; i < radix; i++) {
            indices[i] = min;
        }

        for (int i = min; i < max; i++) {
            Highlights.markArray(1, i);
            int temp = array[i];
            int digit = Reads.getDigit(temp, pow, radix);
            for (int j = radix - 1; j > digit; j--) {
                if (indices[j] != indices[j - 1])
                    Writes.write(array, indices[j], array[indices[j - 1]], sleep, true, false);
                Writes.write(indices, j, indices[j] + 1, 0, false, true);
            }
            Writes.write(array, indices[digit], temp, sleep, true, false);
            Writes.write(indices, digit, indices[digit] + 1, 0, false, true);
        }

        for (int i = 0; i < radix; i++) {
            int subMin = i == 0 ? min : indices[i - 1];
            this.radixMSD(array, length, subMin, indices[i], radix, pow - 1, sleep);
        }

        Writes.changeAllocAmount(-indices.length);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int highestpower = Reads.analyzeMaxLog(array, sortLength, bucketCount, 0.5, true);
        
        radixMSD(array, sortLength, 0, sortLength, bucketCount, highestpower, 1);
    }
}
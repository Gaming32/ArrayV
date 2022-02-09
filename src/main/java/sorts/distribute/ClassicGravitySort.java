package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

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

final public class ClassicGravitySort extends Sort {
    public ClassicGravitySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Classic Gravity");
        this.setRunAllSortsName("Classic Gravity (Bead) Sort");
        this.setRunSortName("Classic Beadsort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int max = Reads.analyzeMax(array, length, 0.5, true);
		int[] transpose = Writes.createExternalArray(max);

		for (int i = 0; i < length; i++) {
			Highlights.markArray(2, i);
			int num = array[i];
			for (int j = 0; j < num; j++) {
				Writes.write(transpose, j, transpose[j] + 1, 0.01, true, true);
			}
		}
		Highlights.clearMark(2);

		for (int i = 0; i < length; i++) {
			Highlights.markArray(2, i);
			int sum = 0;
			for (int j = 0; j < max; j++) {
				if (transpose[j] > 0) {
					sum++;
				}
			}
			Writes.write(array, length - i - 1, sum, 0.01, true, false);
			for (int j = 0; j < max; j++) {
				Writes.write(transpose, j, transpose[j] - 1, 0.01, true, true);
			}
		}

		Writes.deleteExternalArray(transpose);
    }
}
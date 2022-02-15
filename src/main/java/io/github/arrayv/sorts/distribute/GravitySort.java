package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

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

public final class GravitySort extends Sort {
    public GravitySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gravity");
        this.setRunAllSortsName("Gravity (Bead) Sort");
        this.setRunSortName("Beadsort");
        this.setCategory("Distribution Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int min = array[0], max = array[0];

        for(int i = 1; i < length; i++) {
            if(array[i] < min) min = array[i];
            if(array[i] > max) max = array[i];
        }

		int[] x = Writes.createExternalArray(length);
    	int[] y = Writes.createExternalArray(max - min + 1);

		double delay = Math.max(2d / length, 0.001);

		//save a copy of array-min in x
		//increase count of the array-min value in y
    	for(int i = 0; i < length; i++) {
			Writes.write(x, i, array[i]-min, 0, true, true);
			Writes.write(y, array[i]-min, y[array[i]-min]+1, 1, false, true);
		}

		//do a partial sum backwards to determine how many elements are greater than a value
    	for(int i = y.length-1; i > 0; i--)
			Writes.write(y, i-1, y[i-1]+=y[i], 1, true, true);

		//iterate for every integer value in the array range
    	for(int j = y.length-1; j >= 0; j--) {
			Highlights.markArray(2, length-y[j]);

			//iterate for every item in array and x
    		for(int i = 0; i < length; i++) {
				Highlights.markArray(1, i);
				Delays.sleep(delay);

				int inc = (i >= length-y[j] ? 1 : 0) - (x[i] >= j ? 1 : 0);

				//update the main array
				Writes.write(array, i, array[i]+inc, delay, true, false);
    		}
		}

		Writes.deleteExternalArray(x);
		Writes.deleteExternalArray(y);
    }
}

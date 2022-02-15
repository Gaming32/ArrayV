package io.github.arrayv.sorts.distribute;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

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

public final class StacklessAmericanFlagSort extends Sort {
    public StacklessAmericanFlagSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stackless American Flag");
        this.setRunAllSortsName("Stackless American Flag Sort");
        this.setRunSortName("Stackless American Flag Sort");
        this.setCategory("Distribution Sorts");
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int stabVal(int idx) {
		if(arrayVisualizer.doingStabilityCheck())
			return arrayVisualizer.getStabilityValue(idx);
		else
			return idx;
	}

	private static int shift(int n, int q, int r) {
		while(q > 0) {
			n /= r;
			q--;
		}
		return n;
	}

	private int dist(int[] array, int[] cnts, int[] offs, int a, int b, int q, int r) {
		for(int i = 1; i < r; i++) {
			Writes.write(cnts, i, cnts[i]+cnts[i-1], 0, false, true);
			Writes.write(offs, i, cnts[i-1], 0, false, true);
		}
		for(int i = 0; i < r-1; i++) {
			int pos = a+offs[i];

			if(cnts[i] > offs[i]) {
				Highlights.markArray(2, pos);
				int t = array[pos];

				do {
					int digit = Reads.getDigit(t, q, r);
					Writes.write(cnts, digit, cnts[digit]-1, 0, false, true);

					int t1 = array[a+cnts[digit]];
					Writes.write(array, a+cnts[digit], t, 0.5, true, false);
					t = t1;
				}
				while(cnts[i] > offs[i]);

				Highlights.clearMark(2);
			}
		}
		int p = a+offs[1];

		for(int i = 0; i < r; i++) {
			Writes.write(cnts, i, 0, 0, false, true);
			Writes.write(offs, i, 0, 0, false, true);
		}
		return p;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int r = bucketCount, q = Reads.analyzeMaxLog(array, length, r, 0.5, true), m = 0,
			i = 0, b = length;

		int[] cnts = new int[r],
			  offs = new int[r];
		Writes.changeAllocAmount(2 * r);

		for(int j = i; j < b; j++) {
			Highlights.markArray(1, j);
			int digit = Reads.getDigit(array[j], q, r);
			Writes.write(cnts, digit, cnts[digit]+1, 0.5, false, true);
		}

		while(i < length) {
			int p = b-i < 1 ? i : this.dist(array, cnts, offs, i, b, q, r);

			if(q == 0) {
				m += r;
				int t = m/r;

				while(t%r == 0) {
					t /= r;
					q++;
				}

				i = b;
				while(b < length && shift(this.stabVal(array[b]), q+1, r) == shift(m, q+1, r)) {
					Highlights.markArray(1, b);
					int digit = Reads.getDigit(array[b], q, r);
					Writes.write(cnts, digit, cnts[digit]+1, 0.5, false, true);
					b++;
				}
			}
			else {
				b = p;
				q--;

				for(int j = i; j < b; j++) {
					Highlights.markArray(1, j);
					int digit = Reads.getDigit(array[j], q, r);
					Writes.write(cnts, digit, cnts[digit]+1, 0.5, false, true);
				}
			}
		}
		Writes.changeAllocAmount(-2 * r);
    }
}

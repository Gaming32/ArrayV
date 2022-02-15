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

public final class RotateMSDRadixSort extends Sort {
    public RotateMSDRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rotate MSD Radix");
        this.setRunAllSortsName("Rotate MSD Radix Sort, Base 4");
        this.setRunSortName("Rotate MSD Radixsort");
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

	private int base;

	private int shift(int n, int q) {
		while(q > 0) {
			n /= this.base;
			q--;
		}
		return n;
	}

	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 0.5, true, false);
	}

    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;

        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(array, m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }

	private int binSearch(int[] array, int a, int b, int d, int p) {
		while(a < b) {
			int m = (a+b)/2;

			if(Reads.getDigit(array[m], p, this.base) >= d)
				b = m;

			else a = m+1;
		}
		return a;
	}

	private void merge(int[] array, int a, int m, int b, int da, int db, int p) {
		if(b-a < 2 || db-da < 2) return;

		int dm = (da+db)/2;
		int m1 = this.binSearch(array, a, m, dm, p);
		int m2 = this.binSearch(array, m, b, dm, p);

		this.rotate(array, m1, m, m2);
		m = m1+(m2-m);

		this.merge(array, m, m2, b, dm, db, p);
		this.merge(array, a, m1, m, da, dm, p);
	}

	private void mergeSort(int[] array, int a, int b, int p) {
		if(b-a < 2) return;

		int m = (a+b)/2;

		this.mergeSort(array, a, m, p);
		this.mergeSort(array, m, b, p);

		this.merge(array, a, m, b, 0, this.base, p);
	}

	private int dist(int[] array, int a, int b, int p) {
		this.mergeSort(array, a, b, p);

		return this.binSearch(array, a, b, 1, p);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.base = bucketCount;
		int q = Reads.analyzeMaxLog(array, length, this.base, 0.5, true);
		int m = 0, i = 0, b = length;

		while(i < length) {
			int p = b-i < 1 ? i : this.dist(array, i, b, q);

			if(q == 0) {
				m += this.base;
				int t = m/this.base;
				while(t%this.base == 0) {
					t /= this.base;
					q++;
				}

				i = b;
				Highlights.clearMark(2);
				arrayVisualizer.toggleAnalysis(true);
				while(b < length && this.shift(this.stabVal(array[b]), q+1) == this.shift(m, q+1)) {
					Highlights.markArray(1, b);
					Delays.sleep(0.5);
					b++;
				}
				arrayVisualizer.toggleAnalysis(false);
			}
			else {
				b = p;
				q--;
			}
		}
    }
}

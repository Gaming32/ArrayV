package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 Piotr Grochowski
Copyright (c) 2021 aphitorite

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
    name = "Parallel Odd-Even Merge",
    listName = "Odd-Even Merge (Parallel)",
    unreasonableLimit = 4096
)
public final class OddEvenMergeSortParallel extends Sort {
    public OddEvenMergeSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	private int[] array;

	private class OddEvenMerge extends Thread {
        private int lo, m2, n, r;

        OddEvenMerge(int lo, int m2, int n, int r) {
            this.lo = lo;
			this.m2 = m2;
			this.n  = n;
			this.r  = r;
        }
        public void run() {
            OddEvenMergeSortParallel.this.oddEvenMerge(lo, m2, n, r);
        }
    }

	private class OddEvenMergeSort extends Thread {
        private int lo, n;

        OddEvenMergeSort(int lo, int n) {
            this.lo = lo;
			this.n  = n;
        }
        public void run() {
            OddEvenMergeSortParallel.this.oddEvenMergeSort(lo, n);
        }
    }

	private void compSwap(int a, int b) {
		if(Reads.compareIndices(array, a, b, 1, true) == 1)
			Writes.swap(array, a, b, 1, true, false);
	}

	private void oddEvenMerge(int lo, int m2, int n, int r) {
        int m = r * 2;
        if(m < n) {
			OddEvenMerge left, right;

            if((n/r)%2 != 0) {
                left  = new OddEvenMerge(lo, (m2+1)/2, n+r, m);
                right = new OddEvenMerge(lo+r, m2/2, n-r, m);
            }
            else {
                left  = new OddEvenMerge(lo, (m2+1)/2, n, m);
                right = new OddEvenMerge(lo+r, m2/2, n, m);
            }

			left.start();
			right.start();

			try {
                left.join();
                right.join();
            }
			catch(InterruptedException e) {
                Thread.currentThread().interrupt();
			}

            if(m2%2 != 0)
                for(int i = lo; i + r < lo + n; i += m)
                    this.compSwap(i, i + r);

            else
                for(int i = lo + r; i + r < lo + n; i += m)
                    this.compSwap(i, i + r);
        }
        else if(n > r) this.compSwap(lo, lo+r);
    }

    private void oddEvenMergeSort(int lo, int n) {
        if (n > 1) {
            int m = n / 2;

            OddEvenMergeSort left  = new OddEvenMergeSort(lo, m);
            OddEvenMergeSort right = new OddEvenMergeSort(lo + m, n-m);

			left.start();
			right.start();

			try {
                left.join();
                right.join();
            }
			catch(InterruptedException e) {
                Thread.currentThread().interrupt();
			}

            this.oddEvenMerge(lo, m, n, 1);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
		this.oddEvenMergeSort(0, length);
    }
}

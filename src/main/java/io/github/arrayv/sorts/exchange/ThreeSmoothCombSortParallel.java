package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2019 PiotrGrochowski
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

public final class ThreeSmoothCombSortParallel extends Sort {
    public ThreeSmoothCombSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("3-Smooth Comb (Parallel)");
        this.setRunAllSortsName("Parallel 3-Smooth Comb Sort");
        this.setRunSortName("Parallel 3-Smooth Combsort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int[] array;

	private class RecursiveComb extends Thread {
		private int pos, gap, end;
		RecursiveComb(int pos, int gap, int end) {
			this.pos = pos;
			this.gap = gap;
			this.end = end;
		}
		public void run() {
			ThreeSmoothCombSortParallel.this.recursiveComb(pos, gap, end);
		}
	}

	private class PowerOfThree extends Thread {
		private int pos, gap, end;
		PowerOfThree(int pos, int gap, int end) {
			this.pos = pos;
			this.gap = gap;
			this.end = end;
		}
		public void run() {
			ThreeSmoothCombSortParallel.this.powerOfThree(pos, gap, end);
		}
	}

    private void recursiveComb(int pos, int gap, int end) {
		if(pos+gap > end) return;

		RecursiveComb a = new RecursiveComb(pos, gap*2, end);
		RecursiveComb b = new RecursiveComb(pos+gap, gap*2, end);
		a.start();
		b.start();

		try {
			a.join();
			b.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		this.powerOfThree(pos, gap, end);
	}

	private void powerOfThree(int pos, int gap, int end) {
		if(pos+gap > end) return;

		PowerOfThree a = new PowerOfThree(pos, gap*3, end);
		PowerOfThree b = new PowerOfThree(pos+gap, gap*3, end);
		PowerOfThree c = new PowerOfThree(pos+2*gap, gap*3, end);
		a.start();
		b.start();
		c.start();

		try {
			a.join();
			b.join();
			c.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		for(int i = pos; i+gap < end; i+=gap)
			if(Reads.compareIndices(this.array, i, i+gap, 0.5, true) == 1)
				Writes.swap(this.array, i, i+gap, 0.5, false, false);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
        this.recursiveComb(0, 1, length);
    }
}

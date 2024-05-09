package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.QuadSorting;

/*
	Copyright (C) 2014-2021 Igor van den Hoven ivdhoven@gmail.com
*/

/*
	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
	CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
	TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
	SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

	Ported to arrayV by mg-2018 and aphitorite, 2021
*/

/*
	fluxsort 1.1.3.3
*/
@SortMeta(name = "Flux")
public final class FluxSort extends QuadSorting {
	public FluxSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private final int FLUX_OUT = 24;

	private boolean fluxAnalyze(int[] array, int nmemb) {
		int cnt, balance = 0;
		int pta;

		pta = 0;
		cnt = nmemb;

		while (--cnt > 0)
			if (Reads.compareIndices(array, pta, ++pta, 0.5, true) > 0)
				balance++;

		if (balance == 0)
			return false;

		if (balance == nmemb - 1) {
			Writes.reversal(array, 0, nmemb - 1, 1, true, false);
			return false;
		}

		if (balance <= nmemb / 6 || balance >= nmemb / 6 * 5) {
			this.quadSort(array, 0, nmemb);
			return false;
		}

		return true;
	}

	private int medianOfFive(int[] array, int v0, int v1, int v2, int v3, int v4) {
		int[] t = new int[4];
		int val;

		val = (Reads.compareIndices(array, v0, v1, 1, true) + 1) / 2;
		t[0] = val;
		t[1] = val ^ 1;
		val = (Reads.compareIndices(array, v0, v2, 1, true) + 1) / 2;
		t[0] += val;
		t[2] = val ^ 1;
		val = (Reads.compareIndices(array, v0, v3, 1, true) + 1) / 2;
		t[0] += val;
		t[3] = val ^ 1;
		val = (Reads.compareIndices(array, v0, v4, 1, true) + 1) / 2;
		t[0] += val;

		if (t[0] == 2)
			return v0;

		val = (Reads.compareIndices(array, v1, v2, 1, true) + 1) / 2;
		t[1] += val;
		t[2] += val ^ 1;
		val = (Reads.compareIndices(array, v1, v3, 1, true) + 1) / 2;
		t[1] += val;
		t[3] += val ^ 1;
		val = (Reads.compareIndices(array, v1, v4, 1, true) + 1) / 2;
		t[1] += val;

		if (t[1] == 2)
			return v1;

		val = (Reads.compareIndices(array, v2, v3, 1, true) + 1) / 2;
		t[2] += val;
		t[3] += val ^ 1;
		val = (Reads.compareIndices(array, v2, v4, 1, true) + 1) / 2;
		t[2] += val;

		if (t[2] == 2)
			return v2;

		val = (Reads.compareIndices(array, v3, v4, 1, true) + 1) / 2;
		t[3] += val;

		return t[3] == 2 ? v3 : v4;
	}

	private int medianOfThree(int[] array, int v0, int v1, int v2) {
		int[] t = new int[2];
		int val;

		val = (Reads.compareIndices(array, v0, v1, 1, true) + 1) / 2;
		t[0] = val;
		t[1] = val ^ 1;
		val = (Reads.compareIndices(array, v0, v2, 1, true) + 1) / 2;
		t[0] += val;

		if (t[0] == 1)
			return v0;

		val = (Reads.compareIndices(array, v1, v2, 1, true) + 1) / 2;
		t[1] += val;

		return t[1] == 1 ? v1 : v2;
	}

	private int medianOfFifteen(int[] array, int ptx, int nmemb) {
		int v0, v1, v2, v3, v4, div = nmemb / 16;

		v0 = this.medianOfThree(array, ptx + div * 2, ptx + div * 1, ptx + div * 3);
		v1 = this.medianOfThree(array, ptx + div * 5, ptx + div * 4, ptx + div * 6);
		v2 = this.medianOfThree(array, ptx + div * 8, ptx + div * 7, ptx + div * 9);
		v3 = this.medianOfThree(array, ptx + div * 11, ptx + div * 10, ptx + div * 12);
		v4 = this.medianOfThree(array, ptx + div * 14, ptx + div * 13, ptx + div * 15);

		return this.medianOfFive(array, v2, v0, v1, v3, v4);
	}

	private int medianOfNine(int[] array, int ptx, int nmemb) {
		int v0, v1, v2, div = nmemb / 16;

		v0 = this.medianOfThree(array, ptx + div * 2, ptx + div * 1, ptx + div * 4);
		v1 = this.medianOfThree(array, ptx + div * 8, ptx + div * 6, ptx + div * 10);
		v2 = this.medianOfThree(array, ptx + div * 14, ptx + div * 12, ptx + div * 15);

		return this.medianOfThree(array, v0, v1, v2);
	}

	private void fluxPartition(int[] array, int[] swap, int[] main, int start, int nmemb) {
		int val;
		int aSize, sSize;
		int pta, pts, ptx, pte, piv;

		ptx = main == array ? start : 0;

		piv = main[nmemb > 1024 ? this.medianOfFifteen(main, ptx, nmemb) : this.medianOfNine(main, ptx, nmemb)];

		pte = ptx + nmemb;

		pta = start;
		pts = 0;

		while (ptx < pte) {
			Highlights.markArray(3, ptx);
			val = (Reads.compareValues(main[ptx], piv) + 1) / 2;
			Delays.sleep(0.25);

			Writes.write(array, pta, main[ptx], 0.25, true, false);
			pta += val ^ 1;
			Highlights.markArray(2, pts);
			Writes.write(swap, pts, main[ptx], 0.25, false, true);
			pts += val;

			ptx++;
		}
		Highlights.clearMark(3);

		sSize = pts;
		aSize = nmemb - sSize;

		if (aSize <= sSize / 16 || sSize <= FLUX_OUT) {
			Writes.arraycopy(swap, 0, array, pta, sSize, 0.5, true, false);
			this.quadSortSwap(array, swap, pta, sSize);
		} else
			this.fluxPartition(array, swap, swap, pta, sSize);

		if (sSize <= aSize / 16 || aSize <= FLUX_OUT)
			this.quadSortSwap(array, swap, start, aSize);
		else
			this.fluxPartition(array, swap, array, start, aSize);
	}

	public void fluxsort(int[] array, int nmemb) {
		if (nmemb < 32)
			this.quadSort(array, 0, nmemb);

		else if (this.fluxAnalyze(array, nmemb)) {
			int[] swap = Writes.createExternalArray(nmemb);

			this.fluxPartition(array, swap, array, 0, nmemb);

			Writes.deleteExternalArray(swap);
		}
	}

	@Override
	public void runSort(int[] array, int nmemb, int bucketCount) {
		this.fluxsort(array, nmemb);
	}
}

package io.github.arrayv.sorts.hybrid;

import java.util.Random;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.MultiWayMergeSorting;

/*
 *
MIT License

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

public final class FlanSort extends MultiWayMergeSorting {
    public FlanSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Flan");
        this.setRunAllSortsName("Flan Sort");
        this.setRunSortName("Flansort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	//unstable sorting algorithm performing an average of
	//O(n log n) comparisons and O(n) moves in O(1) memory

	private final int G = 14;
	private final int R = 4;

	private int medianOfThree(int[] array, int a, int m, int b) {
		if(Reads.compareValues(array[m], array[a]) > 0) {
			if(Reads.compareValues(array[m], array[b]) < 0)
				return m;
			if(Reads.compareValues(array[a], array[b]) > 0)
				return a;
			else
				return b;
		}
		else {
			if(Reads.compareValues(array[m], array[b]) > 0)
				return m;
			if(Reads.compareValues(array[a], array[b]) < 0)
				return a;
			else
				return b;
		}
	}
	//when shuffled the first 9 and 27 items will be accessed instead respectively
	private int ninther(int[] array, int a, int b) {
		int s = (b-a)/9;

		int a1 = this.medianOfThree(array, a,     a+  s, a+2*s);
		int m1 = this.medianOfThree(array, a+3*s, a+4*s, a+5*s);
		int b1 = this.medianOfThree(array, a+6*s, a+7*s, a+8*s);

		return this.medianOfThree(array, a1, m1, b1);
	}
	private int medianOfThreeNinthers(int[] array, int a, int b) {
		int s = (b-a)/3;

		int a1 = this.ninther(array, a, a+s);
		int m1 = this.ninther(array, a+s, a+2*s);
		int b1 = this.ninther(array, a+2*s, b);

		return this.medianOfThree(array, a1, m1, b1);
	}

	private void shiftBW(int[] array, int a, int m, int b) {
		while(m > a) Writes.swap(array, --b, --m, 1, true, false);
	}

	private int leftBlockSearch(int[] array, int a, int b, int val) {
		int s = G+1;

		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			Highlights.markArray(3, m);
			Delays.sleep(0.25);

			if(Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m+s;
		}

		Highlights.clearMark(3);
		return a;
	}
	private int rightBlockSearch(int[] array, int a, int b, int val) {
		int s = G+1;

		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			Highlights.markArray(3, m);
			Delays.sleep(0.25);

			if(Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+s;
		}

		Highlights.clearMark(3);
		return a;
	}

    private int rightBinSearch(int[] array, int a, int b, int val, boolean bw) {
		int cmp = bw ? 1 : -1;

		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(3, m);
			Delays.sleep(0.25);

			if(Reads.compareValues(val, array[m]) == cmp)
				b = m;
			else
				a = m+1;
		}

		Highlights.clearMark(3);
		return a;
	}

	private void insertTo(int[] array, int tmp, int a, int b) {
		Highlights.clearMark(2);
		while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, tmp, 0.5, true, false);
	}

	private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, array[i], i, this.rightBinSearch(array, a, i, array[i], false));
    }

	private void kWayMerge(int[] array, int[] heap, int[] pa, int s, int b, int p, int size) {
		if(size < 2) {
			if(size == 1) while(pa[0] < b) Writes.swap(array, p++, pa[0]++, 1, true, false);
			return;
		}
		int a = pa[0];

		for(int i = 0; i < size; i++)
			Writes.write(heap, i, i, 0, false, true);

		for(int i = (size-1)/2; i >= 0; i--)
			this.siftDown(array, heap, pa, heap[i], i, size);

		while(size > 0) {
			int min = heap[0];

			Writes.swap(array, p++, pa[min], 0, true, false);
			Writes.write(pa, min, pa[min]+1, 1, false, true);

			if(pa[min] == Math.min(a+(min+1)*s, b))
				this.siftDown(array, heap, pa, heap[--size], 0, size);
			else
				this.siftDown(array, heap, pa, heap[0], 0, size);
		}
	}

	private void retrieve(int[] array, int i, int p, int pEnd, int bsv, boolean bw) {
		int j = i-1, m;

		for(int k = pEnd-(G+1); k > p+G;) {
			m = this.rightBinSearch(array, k-G, k, bsv, bw)-1;
			k -= G+1;

			while(m >= k) Writes.swap(array, j--, m--, 1, true, false);
		}

		m = this.rightBinSearch(array, p, p+G, bsv, bw)-1;
		while(m >= p) Writes.swap(array, j--, m--, 1, true, false);
	}

	//buffer length is at least sortLength*(G+1)-1
	private void librarySort(int[] array, int a, int b, int p, int bsv, boolean bw) {
		int len = b-a;

		if(len < 32) {
			this.binaryInsertion(array, a, b);
			return;
		}

		Random rng = new Random();

		int s = len;
		while(s >= 32) s = (s-1)/R + 1;

		int i = a+s, j = a+R*s, pEnd = p + (s+1)*(G+1)+G;
		this.binaryInsertion(array, a, i);
		for(int k = 0; k < s; k++) //scatter elements to make G sized gaps b/w them
			Writes.swap(array, a+k, p + k*(G+1)+G, 1, true, false);

		while(i < b) {
			if(i == j) { //rebalancing (retrieve from buffer & rescatter)
				this.retrieve(array, i, p, pEnd, bsv, bw);

				s = i-a;
				pEnd = p + (s+1)*(G+1)+G;
				j = a+(j-a)*R;

				for(int k = 0; k < s; k++)
					Writes.swap(array, a+k, p + k*(G+1)+G, 1, true, false);
			}

			int bLoc = this.leftBlockSearch(array, p+G, pEnd-(G+1), array[i]); //search gap location

			if(Reads.compareValues(array[i], array[bLoc]) == 0) { //handle equal values to prevent worst case O(n^2)
				int eqEnd = this.rightBlockSearch(array, bLoc+(G+1), pEnd-(G+1), array[i]); //find the endpoint of the gaps with equal head element
				bLoc += rng.nextInt((eqEnd-bLoc)/(G+1))*(G+1);                              //choose a random gap from the range of gaps
			}

			int loc  = this.rightBinSearch(array, bLoc-G, bLoc, bsv, bw); //search next empty space in gap

			if(loc == bLoc) { //if there is no empty space filled elements in gap are split
				do bLoc += G+1;
				while(bLoc < pEnd && this.rightBinSearch(array, bLoc-G, bLoc, bsv, bw) == bLoc);

				if(bLoc == pEnd) { //rebalancing
					this.retrieve(array, i, p, pEnd, bsv, bw);

					s = i-a;
					pEnd = p + (s+1)*(G+1)+G;
					j = a+(j-a)*R;

					for(int k = 0; k < s; k++)
						Writes.swap(array, a+k, p + k*(G+1)+G, 1, true, false);
				}
				else { //if a gap is full find next non full gap to the right & shift the space down
					int rotP = this.rightBinSearch(array, bLoc-G, bLoc, bsv, bw);
					int rotS = bLoc - Math.max(rotP, bLoc - G/2); //for odd G whether its floor or ceil(G/2) doesnt matter
					this.shiftBW(array, loc-rotS, bLoc-rotS, bLoc);
				}
			}
			else {
				int t = array[i];
				Writes.write(array, i++, array[loc], 1, true, false);
				this.insertTo(array, t, loc, this.rightBinSearch(array, bLoc-G, loc, t, false));
			}
		}
		this.retrieve(array, b, p, pEnd, bsv, bw);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) { //to benefit from worst case O(n log n) comparisons & O(n) moves
																	//we would normally shuffle the array before sorting
																	//but for the sake of demonstration this step is omitted
		int[] pa   = new int[G+2];
		int[] heap = new int[G+2];

		int alloc = pa.length + heap.length;
		Writes.changeAllocAmount(alloc);

		int a = 0, b = length;

		while(b-a >= 32) {
			int piv = array[this.medianOfThreeNinthers(array, a, b)];

			//partition -> [a][E > piv][i][E == piv][j][E < piv][b]
			int i1 = a, i = a-1, j = b, j1 = b;

			for(;;) {
				while(++i < j) {
					int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
					if(cmp == 0) Writes.swap(array, i1++, i, 1, true, false);
					else if(cmp < 0) break;
				}
				Highlights.clearMark(2);

				while(--j > i) {
					int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
					if(cmp == 0) Writes.swap(array, --j1, j, 1, true, false);
					else if(cmp > 0) break;
				}
				Highlights.clearMark(2);

				if(i < j) {
					Writes.swap(array, i, j, 1, true, false);
					Highlights.clearMark(2);
				}
				else {
					if(i1 == b) return;
					else if(j < i) j++;

					while(i1 > a) Writes.swap(array, --i, --i1, 1, true, false);
					while(j1 < b) Writes.swap(array, j++, j1++, 1, true, false);

					break;
				}
			}

			int left = i-a, right = b-j, m, kCnt = 0;

			if(left <= right) { //sort the smaller partition using larger partition as space
				m = b-left;
				left = Math.max((right+1)/(G+1), 16);

				for(int k = a; k < i; k += left) {
					this.librarySort(array, k, Math.min(k+left, i), j, piv, true);
					Writes.write(pa, kCnt++, k, 0, false, true);
				}

				this.kWayMerge(array, heap, pa, left, i, m, kCnt);

				//swap items eq to pivot next to sorted area
				//eq items zone: [i][E == piv][j][E < piv][m][sorted area]
				if(j-i < m-j) {
					while(i < j) Writes.swap(array, i++, --m, 1, true, false);
					b = m;
				}
				else {
					while(m > j) Writes.swap(array, i++, --m, 1, true, false);
					b = i;
				}
			}
			else {
				m = a+right;
				right = Math.max((left+1)/(G+1), 16);

				for(int k = j; k < b; k += right) {
					this.librarySort(array, k, Math.min(k+right, b), a, piv, false);
					Writes.write(pa, kCnt++, k, 0, false, true);
				}

				this.kWayMerge(array, heap, pa, right, b, a, kCnt);

				//eq items zone: [sorted area][m][E > piv][i][E == piv][j]
				if(i-m < j-i) {
					while(m < i) Writes.swap(array, m++, --j, 1, true, false);
					a = j;
				}
				else {
					while(j > i) Writes.swap(array, m++, --j, 1, true, false);
					a = m;
				}
			}
		}
		this.binaryInsertion(array, a, b);
		Writes.changeAllocAmount(-alloc);
    }
}

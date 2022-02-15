package io.github.arrayv.sorts.hybrid;

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

public final class RemiSort extends MultiWayMergeSorting {
	public RemiSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Remi");
		this.setRunAllSortsName("Remi Sort");
		this.setRunSortName("Remisort");
		this.setCategory("Hybrid Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	//stable sorting algorithm that guarantees worst case performance of
	//O(n log n) comparisons and O(n) moves in O(n^2/3) memory

	private int ceilCbrt(int n) {
		int a = 0, b = Math.min(1291, n);

		while(a < b) {
			int m = (a+b)/2;

			if(m*m*m >= n) b = m;
			else           a = m+1;
		}

		return a;
	}

	private void siftDown(int[] array, int[] keys, int r, int len, int a, int t) {
		int j = r;

		while(2*j + 1 < len) {
			j = 2*j + 1;

			if(j+1 < len) {
				int cmp = Reads.compareIndices(array, a+keys[j+1], a+keys[j], 0.2, true);

				if(cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j+1], keys[j]) > 0)) j++;
			}
		}
		for(int cmp = Reads.compareIndices(array, a+t, a+keys[j], 0.2, true);

			cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0);

			j = (j-1)/2,
			cmp = Reads.compareIndices(array, a+t, a+keys[j], 0.2, true));

		for(int t2; j > r; j = (j-1)/2) {
			t2 = keys[j];
			Highlights.markArray(3, j);
			Writes.write(keys, j, t, 0.2, false, true);
			t = t2;
		}
		Highlights.markArray(3, r);
		Writes.write(keys, r, t, 0.2, false, true);
	}

	private void tableSort(int[] array, int[] keys, int a, int b) {
		int len = b-a;

		for(int i = (len-1)/2; i >= 0; i--)
			this.siftDown(array, keys, i, len, a, keys[i]);

		for(int i = len-1; i > 0; i--) {
			int t = keys[i];
			Highlights.markArray(3, i);
			Writes.write(keys, i, keys[0], 1, false, true);
			this.siftDown(array, keys, 0, i, a, t);
		}
		Highlights.clearMark(3);

		for(int i = 0; i < len; i++) {
			Highlights.markArray(2, i);
			if(Reads.compareOriginalValues(i, keys[i]) != 0) {
				int t = array[a+i];
				int j = i, next = keys[i];

				do {
					Writes.write(array, a+j, array[a+next], 1, true, false);
					Writes.write(keys, j, j, 1, true, true);

					j = next;
					next = keys[next];
				}
				while(Reads.compareOriginalValues(next, i) != 0);

				Writes.write(array, a+j, t, 1, true, false);
				Writes.write(keys, j, j, 1, true, true);
			}
		}
		Highlights.clearMark(2);
	}

	private void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
		for(int i = 0; i < bCnt; i++) {
			if(Reads.compareOriginalValues(i, keys[i]) != 0) {
				Writes.arraycopy(array, a + i*bLen, buf, 0, bLen, 1, true, true);
				int j = i, next = keys[i];

				do {
					Writes.arraycopy(array, a + next*bLen, array, a + j*bLen, bLen, 1, true, false);
					Writes.write(keys, j, j, 1, true, true);

					j = next;
					next = keys[next];
				}
				while(Reads.compareOriginalValues(next, i) != 0);

				Writes.arraycopy(buf, 0, array, a + j*bLen, bLen, 1, true, false);
				Writes.write(keys, j, j, 1, true, true);
			}
		}
	}
	private void kWayMerge(int[] array, int[] buf, int[] keys, int[] heap, int b, int[] pa, int[] p, int bLen, int rLen) {
		int k = p.length, size = k, a = pa[0], a1 = pa[1];

		for(int i = 0; i < k; i++)
			Writes.write(heap, i, i, 0, false, true);

		for(int i = (k-1)/2; i >= 0; i--)
			this.siftDown(array, heap, pa, heap[i], i, k);

		for(int i = 0; i < rLen; i++) {
			int min = heap[0];

			Highlights.markArray(2, pa[min]);

			Writes.write(buf, i, array[pa[min]], 0, false, true);
			Writes.write(pa, min, pa[min]+1, 1, false, true);

			if(pa[min] == Math.min(a + (min+1)*rLen, b))
				this.siftDown(array, heap, pa, heap[--size], 0, size);
			else
				this.siftDown(array, heap, pa, heap[0], 0, size);
		}
		int t = 0, cnt = 0, c = 0;
		while(pa[c]-p[c] < bLen) c++;

		do {
			int min = heap[0];

			Highlights.markArray(2, pa[min]);
			Highlights.markArray(3, p[c]);

			Writes.write(array, p[c], array[pa[min]], 0, false, false);
			Writes.write(pa, min, pa[min]+1, 0, false, true);
			Writes.write(p, c, p[c]+1, 1, false, true);

			if(pa[min] == Math.min(a + (min+1)*rLen, b))
				this.siftDown(array, heap, pa, heap[--size], 0, size);
			else
				this.siftDown(array, heap, pa, heap[0], 0, size);

			if(++cnt == bLen) {
				Writes.write(keys, t++, (c > 0) ? p[c]/bLen-bLen-1 : -1, 0, false, true);

				c = cnt = 0;
				while(pa[c]-p[c] < bLen) c++;
			}
		}
		while(size > 0);

		Highlights.clearAllMarks();

		while(cnt-- > 0) {
			Writes.write(p, c, p[c]-1, 0, false, true);
			Writes.write(array, --b, array[p[c]], 1, true, false);
		}
		Writes.write(pa, k-1, b, 0, false, true);
		Writes.write(keys, keys.length-1, -1, 0, false, true);

		t = 0;
		while(keys[t] != -1) t++;

		for(int i = 1, j = a; j < p[0]; i++) {
			while(p[i] < pa[i]) {
				Writes.write(keys, t++, p[i]/bLen-bLen, 0, false, true);
				while(keys[t] != -1) t++;

				Writes.arraycopy(array, j, array, p[i], bLen, 1, true, false);
				Writes.write(p, i, p[i]+bLen, 0, false, true);

				j += bLen;
			}
		}
		Writes.arraycopy(buf, 0, array, a, rLen, 1, true, false);

		this.blockCycle(array, buf, keys, a1, bLen, (b-a1)/bLen);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;

		int bLen = this.ceilCbrt(length);
		int rLen = bLen*bLen;
		int rCnt = (length-1)/rLen + 1;

		if(rCnt < 2) {
			int[] keys = Writes.createExternalArray(length);

			for(int i = 0; i < keys.length; i++)
				Writes.write(keys, i, i, 1, true, true);

			this.tableSort(array, keys, a, b);

			Writes.deleteExternalArray(keys);
			return;
		}

		int[] keys = Writes.createExternalArray(rLen);
		int[] buf  = Writes.createExternalArray(rLen);

		int[] heap = new int[rCnt];
		int[] p    = new int[rCnt];
		int[] pa   = new int[rCnt];

		int alloc = 3*rCnt;
		Writes.changeAllocAmount(alloc);

		for(int i = 0; i < keys.length; i++)
			Writes.write(keys, i, i, 1, true, true);

		for(int i = a, j = 0; i < b; i += rLen, j++) {
			this.tableSort(array, keys, i, Math.min(i+rLen, b));
			Writes.write(pa, j, i, 0, false, true);
		}
		Writes.arraycopy(pa, 0, p, 0, rCnt, 0, false, true);

		this.kWayMerge(array, buf, keys, heap, b, pa, p, bLen, rLen);

		Writes.deleteExternalArray(keys);
		Writes.deleteExternalArray(buf);
		Writes.changeAllocAmount(-alloc);
	}
}

package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;

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

public abstract class MultiWayMergeSorting extends Sort {
    protected MultiWayMergeSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	protected boolean keyLessThan(int[] src, int[] pa, int a, int b) {
		int cmp = Reads.compareValues(src[pa[a]], src[pa[b]]);
		return cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(a, b) < 0);
	}

	protected void siftDown(int[] src, int[] heap, int[] pa, int t, int r, int size) {
		while(2*r+2 < size) {
			int nxt = 2*r+1;
			int min = nxt + (this.keyLessThan(src, pa, heap[nxt], heap[nxt+1]) ? 0 : 1);

			if(this.keyLessThan(src, pa, heap[min], t)) {
				Writes.write(heap, r, heap[min], 0.25, true, true);
				r = min;
			}
			else break;
		}
		int min = 2*r+1;

		if(min < size && this.keyLessThan(src, pa, heap[min], t)) {
			Writes.write(heap, r, heap[min], 0.25, true, true);
			r = min;
		}
		Writes.write(heap, r, t, 0.25, true, true);
	}

	protected void kWayMerge(int[] src, int[] dest, int[] heap, int[] pa, int[] pb, int size, boolean auxWrite) {
		for(int i = 0; i < size; i++)
			Writes.write(heap, i, i, 0, false, true);

		for(int i = (size-1)/2; i >= 0; i--)
			this.siftDown(src, heap, pa, heap[i], i, size);

		for(int i = 0; size > 0; i++) {
			int min = heap[0];

			Highlights.markArray(2, pa[min]);

			Writes.write(dest, i, src[pa[min]], 0.5, !auxWrite, auxWrite);
			Writes.write(pa, min, pa[min]+1, 0, false, true);

			if(pa[min] == pb[min])
				this.siftDown(src, heap, pa, heap[--size], 0, size);
			else
				this.siftDown(src, heap, pa, heap[0], 0, size);
		}
	}
}

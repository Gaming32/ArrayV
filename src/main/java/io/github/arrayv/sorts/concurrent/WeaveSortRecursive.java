package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
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

@SortMeta(
	listName = "Weave (Recursive)",
	showcaseName = "Recursive Weave Sorting Network",
	runName = "Recursive Weave Sort"
)
public final class WeaveSortRecursive extends Sort {
    public WeaveSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private int end;

	private void compSwap(int[] array, int a, int b) {
		if(b < this.end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	private void circle(int[] array, int pos, int len, int gap) {
		if(len < 2) return;

		for(int i = 0; 2*i < (len-1)*gap; i+=gap)
			this.compSwap(array, pos+i, pos+(len-1)*gap-i);

		this.circle(array, pos, len/2, gap);
		if(pos+len*gap/2 < this.end) this.circle(array, pos+len*gap/2, len/2, gap);
	}

	private void weaveCircle(int[] array, int pos, int len, int gap) {
		if(len < 2) return;

		this.weaveCircle(array, pos, len/2, 2*gap);
		this.weaveCircle(array, pos+gap, len/2, 2*gap);

		this.circle(array, pos, len, gap);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	this.end = length;
    	int n = 1;
    	for(; n < length; n*=2);

		this.weaveCircle(array, 0, n, 1);
    }
}

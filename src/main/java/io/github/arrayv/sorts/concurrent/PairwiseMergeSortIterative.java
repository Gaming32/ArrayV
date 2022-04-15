package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

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

@SortMeta(
	name = "Iterative Pairwise Merge",
	listName = "Pairwise Merge (Iterative)"
)
public final class PairwiseMergeSortIterative extends Sort {
    public PairwiseMergeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	private int end;

	private void compSwap(int[] array, int a, int b) {
		if(b < this.end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
    	this.end = length;
    	int n = 1;
    	for(; n < length; n <<= 1);

		for(int k = n >> 1; k > 0; k >>= 1)
			for(int j = 0; j < length; j += k << 1)
				for(int i = 0; i < k; i++)
					this.compSwap(array, j+i, j+k+i);

		for(int k = 2; k < n; k <<= 1)
			for(int m = k >> 1; m > 0; m >>= 1)
				for(int j = 0; j < length; j += k<<1)
					for(int p = m; p < ((k-m)<<1); p += m<<1)
						for(int i = 0; i < m; i++)
							this.compSwap(array, j+p+i, j+p+m+i);
    }
}

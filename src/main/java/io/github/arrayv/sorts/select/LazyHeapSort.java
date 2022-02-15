package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
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

public final class LazyHeapSort extends Sort {
	public LazyHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Lazy Heap");
		this.setRunAllSortsName("Lazy Heap Sort");
		this.setRunSortName("Lazy Heapsort");
		this.setCategory("Selection Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private void maxToFront(int[] array, int a, int b) {
		int max = a;

		for(int i = a+1; i < b; i++)
			if(Reads.compareIndices(array, i, max, 0.1, true) > 0)
				max = i;

		Writes.swap(array, max, a, 1, true, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int s = (int)Math.sqrt(length-1)+1;

		for(int i = 0; i < length; i += s)
			this.maxToFront(array, i, Math.min(i+s, length));

		for(int j = length; j > 0;) {
			int max = 0;

			for(int i = max+s; i < j; i += s)
				if(Reads.compareIndices(array, i, max, 0.1, true) >= 0)
					max = i;

			Writes.swap(array, max, --j, 1, true, false);
			this.maxToFront(array, max, Math.min(max+s, j));
		}
	}
}

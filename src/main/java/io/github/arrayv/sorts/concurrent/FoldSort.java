package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 Marcel Pi Nacy

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
    name = "Fold",
    showcaseName = "Fold Sorting Network",
    runName = "Fold Sort"
)
public final class FoldSort extends Sort {
    public FoldSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    int end;

	void compSwap(int[] array, int a, int b) {
		if(b < end && Reads.compareIndices(array, a, b, 0.5, true) == 1)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	void halver(int[] array, int low, int high)
    {
        while (low < high)
        {
            this.compSwap(array, low++, high--);
        }
    }

    @Override
    public void runSort(int[] array, int size, int bucketCount) {
    	int ceilLog = 1;
    	for (; (1 << ceilLog) < size; ceilLog++);

    	end  = size;
    	size = 1 << ceilLog;

        for (int k = size >> 1; k > 0; k >>= 1)        //log(N)
        {
            for (int i = size; i >= k; i >>= 1)        //log(N)
            {
                for (int j = 0; j < end; j += i)    //N
                {
                    halver(array, j, j + i - 1);
                }
            }
        }
    }
}

package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
	listName = "Bose-Nelson (Iterative)",
	showcaseName = "Iterative Bose-Nelson Sorting Network",
	runName = "Iterative Bose-Nelson Sort"
)
public final class BoseNelsonSortIterative extends Sort {
    public BoseNelsonSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

	private int end;

	private void compSwap(int[] array, int a, int b) {
		if(b >= this.end) return;

		if(Reads.compareIndices(array, a, b, 0.25, true) == 1)
			Writes.swap(array, a, b, 0.5, false, false);
	}

	private void rangeComp(int[] array, int a, int b, int offset) {
		int half = (b-a)/2, m = a+half;
		a += offset;

		for(int i = 0; i < half - offset; i++)
			if((i & ~offset) == i) this.compSwap(array, a+i, m+i);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		this.end = currentLength;
		currentLength = 1 << (int)(Math.ceil(Math.log(currentLength)/Math.log(2)));

		for(int k = 2; k <= currentLength; k*=2)
			for(int j = 0; j < k/2; j++)
				for(int i = 0; i+j < this.end; i+=k)
					this.rangeComp(array, i, i+k, j);
    }
}

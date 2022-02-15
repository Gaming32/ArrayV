package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class BinomialHeapSort extends Sort {
	public BinomialHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Binomial Heap");
		this.setRunAllSortsName("Binomial Heap Sort");
		this.setRunSortName("Binomial Heapsort");
		this.setCategory("Selection Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int maxNode, focus, index, depth;
		for (index = 2; index <= length; index += 2) {
			maxNode = index;
			do {
				focus = maxNode;
				for (depth = 1; (focus & depth) == 0; depth *= 2) {
					if (Reads.compareValues(array[focus - depth - 1], array[maxNode - 1]) > 0)
						maxNode = (focus - depth);
				}
				if (focus != maxNode) {
					Writes.swap(array, focus - 1, maxNode - 1, 1, true, false);
				}
			} while (focus != maxNode);
		}
		for (index = length; index > 2; index--) {
			maxNode = index;
			focus = index;
			for (depth = 1; focus != 0; depth *= 2) {
				if ((focus & depth) != 0) {
					if (Reads.compareValues(array[focus - 1], array[maxNode - 1]) > 0)
						maxNode = focus;
					focus -= depth;
				}
			}
			if (maxNode != index) {
				focus = index;
				do {
					Writes.swap(array, focus - 1, maxNode - 1, 1, true, false);
					focus = maxNode;
					for (depth = 1; (focus & depth) == 0; depth *= 2) {
						if (Reads.compareValues(array[focus - depth - 1], array[maxNode - 1]) > 0)
							maxNode = (focus - depth);
					}
				} while (focus != maxNode);
			}
		}
	}
}

package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(listName = "Quick (Parallel, Left/Right)", runName = "Parallel Quick Sort, Left/Right Pointers", unreasonableLimit = 4096)
public final class LRQuickSortParallel extends Sort {
	public LRQuickSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}

	private int[] a;

	private class QuickSort extends Thread {
		private int p, r;

		QuickSort(int p, int r) {
			this.p = p;
			this.r = r;
		}

		@Override
		public void run() {
			LRQuickSortParallel.this.quickSort(p, r);
		}
	}

	// Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR
	// pointers.
	private void quickSort(int p, int r) {
		if (p < r) {
			int pivot = p + (r - p + 1) / 2;
			int x = this.a[pivot];

			int i = p;
			int j = r;

			Highlights.markArray(3, pivot);

			while (i <= j) {
				while (Reads.compareValues(this.a[i], x) == -1) {
					i++;
					Highlights.markArray(1, i);
					Delays.sleep(0.5);
				}
				while (Reads.compareValues(this.a[j], x) == 1) {
					j--;
					Highlights.markArray(2, j);
					Delays.sleep(0.5);
				}

				if (i <= j) {
					// Follow the pivot and highlight it.
					if (i == pivot) {
						Highlights.markArray(3, j);
					}
					if (j == pivot) {
						Highlights.markArray(3, i);
					}

					Writes.swap(this.a, i, j, 1, true, false);

					i++;
					j--;
				}
			}

			QuickSort left = new QuickSort(p, j);
			QuickSort right = new QuickSort(i, r);
			left.start();
			right.start();

			try {
				left.join();
				right.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.a = array;
		this.quickSort(0, currentLength - 1);
	}
}

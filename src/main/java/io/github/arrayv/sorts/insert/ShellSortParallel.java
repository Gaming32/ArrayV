package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.ShellSorting;

public final class ShellSortParallel extends ShellSorting {
	public ShellSortParallel(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Shell (Parallel)");
		this.setRunAllSortsName("Parallel Shell Sort");
		this.setRunSortName("Parallel Shellsort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int[] array;
	private int[] gaps;

	private class GappedInsertionSort extends Thread {
		private int a, b, g;
		GappedInsertionSort(int a, int b, int g) {
			this.a = a;
			this.b = b;
			this.g = g;
		}
		public void run() {
			ShellSortParallel.this.gappedInsertion(a, b, g);
		}
	}

	private void gappedInsertion(int a, int b, int g) {
		for(int i = a+g; i < b; i+=g) {
			if(Reads.compareIndices(this.array, i-g, i, 1, true) > 0) {
				int tmp = this.array[i], j = i;
				Highlights.clearMark(2);

				do {
					Writes.write(this.array, j, this.array[j-g], 1, true, false);
					j -= g;
				}
				while(j-g >= a && Reads.compareValues(this.array[j-g], tmp) > 0);

				Writes.write(this.array, j, tmp, 1, true, false);
			}
		}
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.array = array;
		this.gaps = this.ExtendedCiuraGaps;

		int k = 0;

		for(; this.gaps[k] >= currentLength; k++);
		for(; k < this.gaps.length; k++) {
			int g = this.gaps[k];
			int t = Math.min(g, currentLength-g);

			GappedInsertionSort[] ins = new GappedInsertionSort[t];
			for(int i = 0; i < t; i++)
				ins[i] = new GappedInsertionSort(i, currentLength, g);

			for(GappedInsertionSort s : ins) s.start();
			for(GappedInsertionSort s : ins) {
				try {
					s.join();
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}

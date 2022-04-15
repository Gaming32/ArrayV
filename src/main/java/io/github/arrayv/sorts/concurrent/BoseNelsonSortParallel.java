package io.github.arrayv.sorts.concurrent;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
	listName = "Bose-Nelson (Parallel)",
	showcaseName = "Parallel Bose-Nelson Sorting Network",
	runName = "Parallel Bose-Nelson Sort",
    unreasonableLimit = 4096
)
public final class BoseNelsonSortParallel extends Sort {

    public BoseNelsonSortParallel(ArrayVisualizer arrayVisualizer) {
    	super(arrayVisualizer);
    }

	private int[] array;

	private class BoseNelson extends Thread {
		private int start, length;
		private double sleep;
		BoseNelson(int start, int length, double sleep) {
			this.start = start;
			this.length = length;
			this.sleep = sleep;
		}
		public void run() {
			BoseNelsonSortParallel.this.boseNelson(start, length, sleep);
		}
	}

	private class BoseNelsonMerge extends Thread {
		private int start1, len1, start2, len2;
		private double sleep;
		BoseNelsonMerge(int start1, int len1, int start2, int len2, double sleep) {
			this.start1 = start1;
			this.len1 = len1;
			this.start2 = start2;
			this.len2 = len2;
			this.sleep = sleep;
		}
		public void run() {
			BoseNelsonSortParallel.this.boseNelsonMerge(start1, len1, start2, len2, sleep);
		}
	}

    private void compareSwap(int start, int end, double sleep) {
    	if (Reads.compareIndices(array, start, end, sleep, true) == 1) {
    	    Writes.swap(array, start, end, 2*sleep, true, false);
        }
    }

    private void boseNelson(int start, int length, double sleep) {
        if (length > 1) {
        	int mid = length / 2;
        	BoseNelson left  = new BoseNelson(start, mid, sleep);
            BoseNelson right = new BoseNelson(start + mid, length - mid, sleep);
			left.start();
			right.start();

			try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            boseNelsonMerge(start, mid, start + mid, length - mid, sleep);
        }
    }

    private void boseNelsonMerge(int start1, int len1, int start2, int len2, double sleep) {
    	if (len1 == 1 && len2 == 1) {
    	    compareSwap(start1, start2, sleep);
        } else if (len1 == 1 && len2 == 2) {
        	compareSwap(start1, start2 + 1, sleep);
            compareSwap(start1, start2, sleep);
        } else if (len1 == 2 && len2 == 1) {
            compareSwap(start1, start2, sleep);
            compareSwap(start1 + 1, start2, sleep);
        } else {
            int mid1 = len1 / 2;
            int mid2 = len1 % 2 == 1 ? len2 / 2 : (len2 + 1) / 2;

            BoseNelsonMerge left  = new BoseNelsonMerge(start1, mid1, start2, mid2, sleep);
            BoseNelsonMerge right = new BoseNelsonMerge(start1 + mid1, len1 - mid1, start2 + mid2, len2 - mid2, sleep);
			left.start();
			right.start();

			try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            boseNelsonMerge(start1 + mid1, len1 - mid1, start2, mid2, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.array = array;
        this.boseNelson(0, length, 0.25);
    }
}

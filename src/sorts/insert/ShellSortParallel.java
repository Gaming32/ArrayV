package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.ShellSorting;

final public class ShellSortParallel extends ShellSorting {
    public ShellSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Shell (Parallel)");
        this.setRunAllSortsName("Parallel Shell Sort");
        this.setRunSortName("Parallel Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
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
			int tmp = this.array[i], j = i;
			
			for(; j-g >= a && Reads.compareValues(this.array[j-g], tmp) > 0; j-=g)
				Writes.write(this.array, j, this.array[j-g], 1, true, false);
			Writes.write(this.array, j, tmp, 1, true, false);
		}
	}
	
	private void parallelShellSort(int len) {
		int k = 0;
		
		for(; this.gaps[k] >= len; k++);
		for(; k < this.gaps.length; k++) {
			int g = this.gaps[k];
			int t = Math.min(g, len-g);
			
			GappedInsertionSort[] ins = new GappedInsertionSort[t];
			for(int i = 0; i < t; i++)
				ins[i] = new GappedInsertionSort(i, len, g);
			
			for(GappedInsertionSort s : ins) s.start();
			for(GappedInsertionSort s : ins) {
				try {
					s.join();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
		this.array = array;
		this.gaps = this.ExtendedCiuraGaps;
		this.parallelShellSort(currentLength);
    }
}
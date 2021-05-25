package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
Idea made by Control#2866 in The Studio Discord Server (https://discord.com/invite/2xGkKC2)
*/

final public class MatrixSortParallel extends Sort {

    public MatrixSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Matrix (Parallel)");
        this.setRunAllSortsName("Parallel Matrix Sort");
        this.setRunSortName("Parallel Matrix Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int[] array;
	
	private volatile boolean did;
	
	private class Insert extends Thread {
		private int a, b, g;
		private boolean bw;
		
		Insert(int a, int b, int g, boolean bw) {
			this.a  = a;
			this.b  = b;
			this.g  = g;
			this.bw = bw;
		}
		public void run() {
			MatrixSortParallel.this.insert(a, b, g, bw);
		}
	}
	
	private class Reversal extends Thread {
		private int a, b;
		
		Reversal(int a, int b) {
			this.a = a;
			this.b = b;
		}
		public void run() {
			Writes.reversal(array, a, b-1, 1, true, false);
		}
	}
	
	private int sqrt(int n) {
		int a = 0, b = Math.min(46341, n);
		
		while(a < b) {
			int m = (a+b)/2;
			
			if(m*m >= n) b = m;
			else         a = m+1;
		}
		
		return a;
	}
	
	private void insert(int a, int b, int g, boolean bw) {
		for(int i = a+g, j; i < b; i += g) {
			int t = array[i];
			
			for(j = i; j-g >= a && Reads.compareValues(array[j-g], t) == (bw ? -1 : 1); j -= g) {
				this.did = true;
				Writes.write(array, j, array[j-g], 1, true, false);
			}
			Writes.write(array, j, t, 1, true, false);
		}
	}
	
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		this.array = array;
		
		int g = sqrt(sortLength);
		Insert[] ins = new Insert[g];
		
		int tCnt = (sortLength-1)/g + 1;
		this.did = true;
		
		while(this.did) {
			this.did = false;
			
			int i = 0, j = 0;
			boolean bw = false;
			
			for(; i+g < sortLength; i += g, j++, bw = !bw)
				ins[j] = new Insert(i, i+g, 1, bw);
			ins[j] = new Insert(i, sortLength, 1, bw);
			
			for(j = 0; j < tCnt; j++) ins[j].start();
				
			for(j = 0; j < tCnt; j++) {
				try {
					ins[j].join();
				} 
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			
			for(i = 0, j = 0; i < g; i++, j++)
				ins[j] = new Insert(i, sortLength, g, false);
			
			for(j = 0; j < g; j++) ins[j].start();
				
			for(j = 0; j < g; j++) {
				try {
					ins[j].join();
				} 
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		
		tCnt = (tCnt+1)/2;
		Reversal[] revs = new Reversal[tCnt];
		
		int i = g, j = 0;
		for(; i+g < sortLength; i += 2*g, j++)
			revs[j] = new Reversal(i, i+g);
		revs[j] = new Reversal(i, sortLength);
		
		for(j = 0; j < tCnt; j++) revs[j].start();
				
		for(j = 0; j < tCnt; j++) {
			try {
				revs[j].join();
			} 
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
    }
}
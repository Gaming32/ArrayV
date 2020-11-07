package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * This version of Bitonic Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/bitonic/oddn.htm
 */

final public class RecursiveBitonicSort extends Sort {
    private boolean direction = true;
    
    public RecursiveBitonicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Recursive Bitonic");
        this.setRunAllSortsName("Batcher's Bitonic Sort");
        this.setRunSortName("Recursive Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private static int greatestPowerOfTwoLessThan(int n){
        int k = 1;
        while (k < n) {
            k = k << 1;
        }
        return k >> 1;      
    }
    
    private void compare(int[] A, int i, int j, boolean dir)
	{
    	Highlights.markArray(1, i);
    	Highlights.markArray(2, j);
    	
    	Delays.sleep(0.5);
    	
    	int cmp = Reads.compareValues(A[i], A[j]);
    	
	    if (dir == (cmp == 1)) Writes.swap(A, i, j, 1, true, false);
	}

	private void bitonicMerge(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = RecursiveBitonicSort.greatestPowerOfTwoLessThan(n);

	        for (int i = lo; i < lo + n - m; i++) {
	            this.compare(A, i, i+m, dir);
	        }

	        this.bitonicMerge(A, lo, m, dir);
	        this.bitonicMerge(A, lo + m, n - m, dir);
	    }
	}

	private void bitonicSort(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = n / 2;
	        this.bitonicSort(A, lo, m, !dir);
	        this.bitonicSort(A, lo + m, n - m, dir);
	        this.bitonicMerge(A, lo, n, dir);
	    }
	}

	public void changeDirection(String choice) throws Exception {
	    if(choice.equals("forward")) this.direction = true;
	    else if(choice.equals("backward")) this.direction = false;
	    else throw new Exception("Invalid direction for Bitonic Sort!");
	}
	
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.bitonicSort(array, 0, sortLength, this.direction);
    }
}
package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * This version of Bitonic Sort was taken from here, written by Nikos Pitsianis:
 * https://www2.cs.duke.edu/courses/fall08/cps196.1/Pthreads/bitonic.c
 * 
 * Thanks to Piotr Grochowski for rewriting code to allow this implementation to
 * work for array lengths other than powers of two!
 */

final public class IterativeBitonicSort extends Sort {
    public IterativeBitonicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Iterative Bitonic");
        this.setRunAllSortsName("Iterative Bitonic Sort");
        this.setRunSortName("Iterative Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    	
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int i, j, k;

        for(k = 2; k < sortLength*2; k = 2 * k) {
            boolean m = (((sortLength + (k - 1)) / k) % 2) != 0;
            
            for(j = k >> 1; j > 0; j = j >> 1) {
                for(i = 0; i < sortLength; i++) {
                    int ij = i ^ j;

                    if((ij) > i && ij < sortLength) {
                        if((((i & k) == 0) == m) && Reads.compareValues(array[i], array[ij]) > 0)
                            Writes.swap(array, i, ij, 1, true, false);
                        if((((i & k) != 0) == m) && Reads.compareValues(array[i], array[ij]) < 0)
                            Writes.swap(array, i, ij, 1, true, false);
                    }
                }
            }
        }
    }
}
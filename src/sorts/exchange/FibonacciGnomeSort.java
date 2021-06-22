package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * The Fibonacci Insertion Sort was made by fungamer2.
 * This variant replaced overwrites with swaps.
 *  
 */

/**
 * @author mingyue12
 *
 */
public final class FibonacciGnomeSort extends Sort {

	public FibonacciGnomeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
        this.setSortListName("Fibonacci Gnome");
        this.setRunAllSortsName("Fibonacci Gnome Sort");
        this.setRunSortName("Fibonacci Gnome Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}
	
    public int fibonacciSearch(int[] array, int start, int end, int item) {
    	int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }
        
        int offset = start - 1;
        
        while (fibM > 1) {
        	
            int i = Math.min(offset + fibM2, end);
            
            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);
            
            if (Reads.compareValues(array[i], item) <= 0) {
            	fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
            	fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
            Delays.sleep(0.6);
        }
        int position = ++offset;
        if (Reads.compareValues(array[position], item) <= 0) {
            ++position;
        }
        return position;
    }
    
    public void fibonacciGnomeSort(int[] array, int length) {
    	for (int i = 1; i < length; i++) {
    	    int tmp = array[i];
            int position = this.fibonacciSearch(array, 0, i - 1, tmp);
            int j = i;
            while (j > position) {
            	Writes.swap(array, j, j - 1, 0, true, false);
            	j--;
            }

    	}
    }

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		fibonacciGnomeSort(array, sortLength);

	}

}

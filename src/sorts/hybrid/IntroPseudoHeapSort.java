package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*
 * The original Pseudo-Heapsort was made by yuji and fungamer2.
 * This variant uses Insertion Sort when it reaches
 * the predefined threshold (sqrt(n)/2).
 *  
 */

/**
 * @author mingyue12
 *
 */
public final class IntroPseudoHeapSort extends Sort {


	public IntroPseudoHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Iterative Intro Pseudo-Heap");
		setRunAllSortsName("Iterative Introspective Pseudo-Heap Sort");
		setRunSortName("Iterative Introspective Pseudo-Heapsort");
		setCategory("Hybrid Sorts");
		setComparisonBased(true);
		setBucketSort(false);
		setRadixSort(false);
		setUnreasonablySlow(false);
		setUnreasonableLimit(0);
		setBogoSort(false);

	}
	
	private boolean sift_down(int[] array, int start, int length, int root) {
		boolean swapped = false;
		int j = root;
		while (2 * j < length) {
			int k = 2 * j;
			if (k < length && this.Reads.compareValues(array[start + k - 1], array[start + k]) == 1) {
				k++;
			}
			if (this.Reads.compareIndices(array, start + j - 1, start + k - 1, 1.0D, true) == 1) {
				this.Writes.swap(array, start + j - 1, start + k - 1, 1.0D, true, false);
				j = k;
				swapped = true;
				continue;
			}
			break;
		}
		return swapped;
	}

	private boolean sift(int[] array, int start, int end) {
		return sift_down(array, start, end - start + 1, 1);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
    	int threshold = 0, n = 1;
    	for(; n < length; n*=2, threshold++);
		
		threshold /= 2;
        int iterations = 0;
		boolean swapped = true;
		while (swapped) {
			swapped = false;
			iterations++;
            if(iterations >= threshold) {
            	InsertionSort ins = new InsertionSort(arrayVisualizer);
            	ins.customInsertSort(array, 0, length, 0.5, false);
            	break;
            }
			for (int i = length - 2; i >= 0; i--) {
				if (sift(array, i, length))
					swapped = true;
			}
		}

	}

}

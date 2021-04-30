/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author yuji
 * @author fungamer2
 *
 */
public final class PseudoHeapSort extends Sort {

	/**
	 * @param arrayVisualizer
	 */
	public PseudoHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		setSortListName("Iterative Pseudo-Heap");
		setRunAllSortsName("Iterative Pseudo-Heap Sort");
		setRunSortName("Iterative Pseudo-Heapsort");
		setCategory("Exchange Sorts");
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
		boolean swapped = true;
		while (swapped) {
		  swapped = false;
		  for (int i = length - 2; i >= 0; i--) {
		    if (sift(array, i, length))
		      swapped = true; 
		  } 
		} 

	}

}

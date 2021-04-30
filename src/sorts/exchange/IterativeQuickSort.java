/**
 * 
 */
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author McDude_73
 *
 */
public final class IterativeQuickSort extends Sort {



	/**
	 * @param arrayVisualizer
	 */
	public IterativeQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	     setSortListName("Iterative Quick");
	     setRunAllSortsName("Iterative Quick Sort");
	     setRunSortName("Iterative Quicksort");
	     setCategory("Exchange Sorts");
	     setComparisonBased(true);
	     setBucketSort(false);
	     setRadixSort(false);
	     setUnreasonablySlow(false);
	     setUnreasonableLimit(0);
	     setBogoSort(false);

	}
	

    
    private int partition(int[] array, int lowValue, int highValue) {
        int pivot = array[highValue];
        
        int i = lowValue - 1;
        for (int j = lowValue; j <= highValue - 1; j++) {
          if (this.Reads.compareValues(array[j], pivot) <= 0) {
            i++;
            this.Writes.swap(array, i, j, 1.0D, true, false);
          } 
        } 
        this.Writes.swap(array, i + 1, highValue, 1.0D, true, false);
        return i + 1;
      }
    
    private void quickSort(int[] array, int startIndex, int endIndex) {
    	int len = endIndex - startIndex + 1;
        int[] stack = new int[len];
        Writes.changeAllocAmount(len);
        int top = -1;
        
        this.Writes.write(stack, ++top, startIndex, 0.0D, true, true);
        this.Writes.write(stack, ++top, endIndex, 0.0D, true, true);
        
        while (top >= 0) {
          endIndex = stack[top--];
          startIndex = stack[top--];
          
          int p = partition(array, startIndex, endIndex);
          
          if (this.Reads.compareValues(p - 1, startIndex) == 1) {
            this.Writes.write(stack, ++top, startIndex, 0.0D, false, true);
            this.Writes.write(stack, ++top, p - 1, 0.0D, false, true);
          } 
          
          if (this.Reads.compareValues(p + 1, endIndex) == -1) {
            this.Writes.write(stack, ++top, p + 1, 0.0D, false, true);
            this.Writes.write(stack, ++top, endIndex, 0.0D, false, true);
          } 
        }
        Writes.changeAllocAmount(-len);
      }


	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		quickSort(array, 0, sortLength - 1);

	}

}

package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/**
 * 
 * @author aphitorite
 * @author thatsOven
 */
public final class BubblescanQuickSort extends Sort {

	public BubblescanQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		// TODO Auto-generated constructor stub
        this.setSortListName("Bubblescan Quick");
        this.setRunAllSortsName("Bubblescan Quick Sort");
        this.setRunSortName("Bubblescan Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
	}
	private InsertionSort insertSorter;
	private MaxHeapSort heapSorter;

	private int partition(int[] array, int a, int b, int val) {
      int i = a, j = b - 1;
      while (i <= j) {
        while (this.Reads.compareValues(array[i], val) < 0) {
          i++;
          this.Highlights.markArray(1, i);
          this.Delays.sleep(0.5D);
        } 
        while (this.Reads.compareValues(array[j], val) > 0) {
          j--;
          this.Highlights.markArray(2, j);
          this.Delays.sleep(0.5D);
        } 
       
        if (i <= j) this.Writes.swap(array, i++, j--, 1.0D, true, false);
     
      } 
      return i;
   }
   
   private void sort(int[] array, int a, int b, int depthLimit) {
     int end = b, length = b - a;
     
     while (length > 16) {
       if (depthLimit == 0) {
         this.heapSorter.customHeapSort(array, a, end, 1.0D);
         
         return;
       } 
       double sum = 0.0D;
       boolean swapped = false;
       
       for (int i = a + 1; i < end; i++) {
         this.Highlights.markArray(1, i - 1);
         this.Highlights.markArray(2, i);
         this.Delays.sleep(0.25D);
         
         if (this.Reads.compareValues(array[i - 1], array[i]) == 1) {
           this.Writes.swap(array, i - 1, i, 0.5D, false, false);
           swapped = true;
         } 
         
         sum += array[i - 1];
       } 
       
       if (!swapped)
         return; 
       int p = partition(array, a, end - 1, (int)(sum / (length - 1)));
       depthLimit--;
       sort(array, p, end - 1, depthLimit);
       
       end = p;
       length = end - a;
     } 
     
     this.Highlights.clearMark(2);
     this.insertSorter.customInsertSort(array, a, end, 0.5D, false);
   }

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		// TODO Auto-generated method stub
		this.insertSorter = new InsertionSort(this.arrayVisualizer);
		this.heapSorter = new MaxHeapSort(this.arrayVisualizer);
		sort(array, 0, sortLength, 2 * (int)(Math.log(sortLength) / Math.log(2.0D)));


	}

}

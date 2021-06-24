package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author thatsOven
 *
 */
public final class RoomStoogeSort extends Sort {

    public RoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Room Stooge");
        setRunAllSortsName("Room Stooge Sort");
        setRunSortName("Room Stoogesort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(8192);
        setBogoSort(false);

    }
    
    public boolean isSorted(int[] a, int n) {
        for (int i = 0; i < n - 1; i++) {
          if (this.Reads.compareValues(a[i], a[i + 1]) > 0) {
            return false;
          }
        } 
        return true;
      }
      
      public void stoogeBubble(int[] A, int i, int j) {
        if (this.Reads.compareValues(A[i], A[j]) == 1) {
          this.Writes.swap(A, i, j, 0.1, true, false);
        }
        
        this.Delays.sleep(0.05);
        
        this.Highlights.markArray(1, i);
        this.Highlights.markArray(2, j);
        
        if (j - i + 1 >= 3) {
          int t = (j - i + 1) / 3;
          
          this.Highlights.markArray(3, j - t);
          this.Highlights.markArray(4, i + t);
          
          stoogeBubble(A, i, j - t);
          stoogeBubble(A, i + t, j);
        } 
      }


    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isSorted(array, currentLength))
            stoogeBubble(array, 0, currentLength - 1); 


    }

}

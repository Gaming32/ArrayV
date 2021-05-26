package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * This example of an O(n^3) sorting algorithm may be found here, written by James Jensen (StriplingWarrayior on StackOverflow):
 * https://stackoverflow.com/questions/27389344/is-there-a-sorting-algorithm-with-a-worst-case-time-complexity-of-n3
 */
 
final public class BadSort extends Sort {
    public BadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Bad");
        this.setRunAllSortsName("Bad Sort");
        this.setRunSortName("Badsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        for (int i = 0; i < currentLen; i++) {
            int shortest = i;
            Delays.sleep(0.05);
            
            for (int j = i; j < currentLen; j++) {
                Highlights.markArray(1, j);
                Delays.sleep(0.05);
                
                boolean isShortest = true;
                for (int k = j + 1; k < currentLen; k++) {
                    Highlights.markArray(2, k);
                    Delays.sleep(0.05);
                    
                    if (Reads.compareValues(array[j], array[k]) == 1) {
                        isShortest = false;
                        break;
                    }
                }
                if(isShortest) {
                    shortest = j;
                    break;
                }
            }
            Writes.swap(array, i, shortest, 0.05, true, false);
        }
    }
}
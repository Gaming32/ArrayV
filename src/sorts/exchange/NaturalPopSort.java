package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan2007
 * @implNote This sorting algorithm is based on Dandelion Sort, 
 *           which is written by yuji and McDude_73.
 *
 */
public final class NaturalPopSort extends Sort {

    public NaturalPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Pop");
        this.setRunAllSortsName("Natural Pop Sort");
        this.setRunSortName("Natural Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void naturalSort(int[] array, int start, int end, boolean fw) {
        int cmp = fw ? 1 : -1;
        for (int foo = start; foo < end; ) {
            this.Highlights.markArray(1, foo);
            int pointer = foo;
            boolean bar = false;
            
            while (pointer < end - 1 && this.Reads.compareIndices(array, pointer, pointer + 1, 0.125D, true) == cmp) {
              this.Writes.swap(array, pointer, pointer + 1, 0.25D, true, false);
              bar = true;
              pointer++;
            } 
            
            if (bar) {
              if (foo > start) foo--;  continue;
            }  foo++;
          } 
    }

    public void popSort(int[] array, int start, int end) {
        naturalSort(array, start, start + ((end - start) / 4), false);
        naturalSort(array, start + ((end - start) / 4), start + ((end - start) / 2), true);
        naturalSort(array, start + ((end - start) / 2), start + (3 * (end - start) / 4), false);
        naturalSort(array, start + (3 * (end - start) / 4), end, true);
        naturalSort(array, start, start + ((end - start) / 2), false);
        naturalSort(array, start + ((end - start) / 2), end, true);
        naturalSort(array, start, end, true);

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        popSort(array, 0, sortLength);

    }

}

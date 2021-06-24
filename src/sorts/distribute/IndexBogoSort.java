package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Index Bogosort is like Bogosort, but it uses an array of indices to
 * determine the location of items. 
 * 
 * @author Yuri-chan2007
 *
 */
public final class IndexBogoSort extends BogoSorting {

    public IndexBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Index Bogo");
        this.setRunAllSortsName("Index Bogo Sort");
        this.setRunSortName("Index Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }
    
    private void indexSort(int[] array, int[] idx, int a, int b) {
        while(a < b) {
            Highlights.markArray(2, a);
            
            if(Reads.compareOriginalValues(a, idx[a]) != 0) {
                int t = array[a];
                int i = a, nxt = idx[a];
                
                do {
                    Writes.write(array, i, array[nxt], 0, true, false);
                    Writes.write(idx, i, i, this.delay, false, true);
                    
                    i = nxt;
                    nxt = idx[nxt];
                }
                while(Reads.compareOriginalValues(nxt, a) != 0);
                
                Writes.write(array, i, t, 0, true, false);
                Writes.write(idx, i, i, this.delay, false, true);
            }
            a++;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int[] indices = Writes.createExternalArray(sortLength);
        for(int i = 0; i<sortLength; i++) {
            Writes.write(indices, i, i, this.delay, true, true);
        }
        while(!this.isArraySorted(array, sortLength)) {
            this.bogoSwap(indices, 0, sortLength, true);
            indexSort(array, indices, 0, sortLength);
        }
        Writes.deleteExternalArray(indices);

    }

}

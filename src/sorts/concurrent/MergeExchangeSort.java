package sorts.concurrent;

import sorts.templates.Sort;
import main.ArrayVisualizer;

final public class MergeExchangeSort extends Sort {
	
    public MergeExchangeSort(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);
        
        this.setSortListName("Merge-Exchange");
        this.setRunAllSortsName("Batcher's Merge-Exchange Sort");
        this.setRunSortName("Merge-Exchange Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int t = (int)(Math.log(length) / Math.log(2));
        int p0 = 1 << (t - 1);
        for (int p = p0; p > 0; p >>= 1) {
        	int q = p0;
            int r = 0;
            int d = p;
            while (true) {
            	for (int i = 0; i < length - d; i++) {
            	    if ((i & p) == r && Reads.compareIndices(array, i, i + d, 1, true) == 1) {
            	        Writes.swap(array, i, i + d, 1, true, false);
                    }
                }
                if (q == p) break;
                d = q - p;
                q >>= 1;
                r = p;
            }
        }
    }
}
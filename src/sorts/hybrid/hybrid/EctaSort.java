package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.KotaSorting;

final public class EctaSort extends KotaSorting {
	public EctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Ecta");
        this.setRunAllSortsName("Ecta Sort [Block Merge Sort]");
        this.setRunSortName("Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.ectaSort(array, 0, length);
    }
}
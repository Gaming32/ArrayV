package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Pancake Bogosort is like Pancake Sort, but it 
 * randomly reverses the unsorted portion of the array
 * until the largest unsorted element is at the
 * end of the unsorted portion of the array.
 *
 * @author Yuri-chan2007
 *
 */
public final class PancakeBogoSort extends BogoSorting {

    public PancakeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pancake Bogo");
        this.setRunAllSortsName("Pancake Bogo Sort");
        this.setRunSortName("Pancake Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }
    
    private void flip(int[] array, int i) {
        Writes.reversal(array, 0, i, this.delay, true, false);
    }
    
    private void pancake(int[] array, int length) {
        for(int i = length - 1; i>=0; i--) {
            while(!this.isMaxSorted(array, 0, i + 1)) {
                int pos = BogoSorting.randInt(0, i + 1);
                flip(array, pos);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        pancake(array, sortLength);

    }

}

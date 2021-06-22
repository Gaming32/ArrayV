package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author aphitorite
 *
 */
public final class TinyGnomeSort extends Sort {

    public TinyGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tiny Gnome");
        this.setRunAllSortsName("Tiny Gnome Sort");
        this.setRunSortName("Tiny Gnomesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        for(int i = 1; i < sortLength; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.05, true) > 0) {
                for (int j = i; j > 0; j--) {
                    boolean foobar = true;
                    for (int k = 0; k < j; k++) {
                        if (Reads.compareIndices(array, k, k + 1, 0.05, true) > 0) {
                            Writes.swap(array, k, k + 1, 0.1, true, false);
                            foobar = false;
                        }
                    }
                    if (foobar)
                        break;
                } 
            }
        }

    }

}

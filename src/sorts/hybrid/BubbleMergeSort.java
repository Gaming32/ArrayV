/**
 * 
 */
package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.templates.TimSorting;

/**
 * @author Yuri-chan2007
 *
 */
public class BubbleMergeSort extends Sort {

    /*
     * TimSort cannot be simply written off as an abstract class, as it creates an
     * instance of itself in order to track its state. Plus, it contains both
     * instance and static methods, requiring even more refactoring, which would be
     * just doing unnecessary busy work. Instead of what we've done for the rest of
     * the algorithms, we'll favor composition over inheritance here and pass "util"
     * objects to it.
     */
    private TimSorting timSortInstance;

    /**
     * @param arrayVisualizer
     */
    public BubbleMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bubble Merge");
        this.setRunAllSortsName("Bubble Merge Sort");
        this.setRunSortName("Bubble Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void bubbleSort(int[] a, int start, int end) {
        for (int i = end - 1; i > start; i--) {
            boolean sorted = true;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(a, j, j + 1, 0.5, true) == 1) {
                    Writes.swap(a, j, j + 1, 1.0, true, false);
                    sorted = false;
                }
            }
            if(sorted) break;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int minRunLen = TimSorting.minRunLength(sortLength);
        if (sortLength == minRunLen) {
            bubbleSort(array, 0, sortLength);
        } else {
            int i = 0;
            for (; i <= (sortLength - minRunLen); i += minRunLen) {
                bubbleSort(array, i, i + minRunLen);
            }
            if (i + minRunLen > sortLength) {
                bubbleSort(array, i, sortLength);
            }
            this.timSortInstance = new TimSorting(array, sortLength, this.arrayVisualizer);
            TimSorting.sort(this.timSortInstance, array, sortLength);
        }

    }

}

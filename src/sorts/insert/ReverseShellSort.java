package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.ShellSorting;

/**
 * @author Yuri-chan2007
 *
 */
public final class ReverseShellSort extends ShellSorting {

    public ReverseShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Reverse Shell");
        this.setRunAllSortsName("Reverse Shell Sort");
        this.setRunSortName("Reverse Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void reverseShellSort(int[] array, int sortLength) {
        int[] gaps = this.ExtendedCiuraGaps;
        for (int k = 0; k < gaps.length; k++) {
            if(gaps[k] < sortLength) {
                int gap = gaps[k];
                for (int i = sortLength - gap; i >= 0; i--) {
                    int tmp = array[i];
                    int j = i;
                    while (j < sortLength - gap && Reads.compareValues(array[j + gap], tmp) == -1) {
                        Highlights.markArray(2, j + gap);
                        Writes.write(array, j, array[j + gap], 0.7, true, false);
                        j += gap;
                    }

                    if (j + gap < sortLength) {
                        Highlights.markArray(2, j + gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            }
            
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        reverseShellSort(array, sortLength);

    }

}

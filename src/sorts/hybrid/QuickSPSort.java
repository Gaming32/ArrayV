package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;

final public class QuickSPSort extends GrailSorting {
    public QuickSPSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Quick Sandpaper");
        this.setRunAllSortsName("Quick Sandpaper Sort");
        this.setRunSortName("Quick Sandpapersort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b) {
        a--; b--;
        if (Reads.compareIndices(array, a, b, 0.0125, true) > 0) {
            Writes.swap(array, a, b, 0.0125, true, false);
        }
    }

    private void ipMerge(int[] array, int a, int m, int b) {
        int len1 = m - a, len2 = b - m;
        this.grailMergeWithoutBuffer(array, a, len1, len2);
    }

    private void quickSPSort2(int[] array, int l, int r) {
        if (r - l < 12) {
            for (int i = l; i <= r; i++) {
                for (int j = i; j <= r; j++) {
                    compSwap(array, i, j);
                }
            }
        }
        else {
            int rb, min, j;
            rb = l + (int)Math.ceil(Math.sqrt(1 + (r - l)));
            for (int i = l; i <= rb; i++) {
                for (j = i; j <= r; j++) {
                    compSwap(array, i, j);
                }
            }
            min = array[rb];
            Highlights.markArray(2, rb);
            j = 1 + rb;
            for (int i = 1 + rb; i <= r; i++) {
                Highlights.markArray(1, i - 1);
                Delays.sleep(0.0125);
                if (Reads.compareValues(array[i - 1], min) < 0) {
                    min = array[i - 1];
                    Highlights.markArray(2, i - 1);
                    j++;
                    Writes.swap(array, i - 1, j - 1, 0.125, true, false);
                }
            }
            Writes.reversal(array, rb, j - 1, 1, true, false);
            quickSPSort2(array, 1 + j, r);
            ipMerge(array, rb, j, r);
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        quickSPSort2(array, 1, currentLength);
    }
}
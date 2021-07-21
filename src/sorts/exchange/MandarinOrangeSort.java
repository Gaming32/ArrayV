package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class MandarinOrangeSort extends Sort {

    public MandarinOrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Mandarin Orange");
        setRunAllSortsName("Mandarin Orange Sort (By yuji & McDude_73)");
        setRunSortName("Mandarin Orange Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(2048);
        setBogoSort(false);

    }

    private void moSorter(int[] array, int length) {
        boolean done = false;
        int gap = 1;

        while (!done) {
            int i = 0;
            done = true;
            while (i + gap < length) {
                if (this.Reads.compareValues(array[i], array[i + gap]) == 1) {
                    done = false;
                    this.Writes.multiSwap(array, i + gap, i, 0.02D, true, false);
                    gap++;
                } else if (gap >= 2) {
                    gap--;
                }

                i++;
            }
            while (i - gap > 0) {
                if (this.Reads.compareValues(array[i - gap], array[i]) == 1) {
                    done = false;
                    this.Writes.multiSwap(array, i, i - gap, 0.2D, true, false);
                    gap++;
                } else if (gap >= 2) {
                    gap--;
                }

                i--;
            }
        }
    }

    private boolean moIsSorted(int[] array, int length) {
        for (int i = 0; i < length - 1; i++) {
            if (this.Reads.compareValues(array[i], array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        for (; !moIsSorted(array, length); moSorter(array, length))
            ;

    }

}

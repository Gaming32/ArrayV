package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class KiyomiSort extends Sort {

    public KiyomiSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Kiyomi");
        setRunAllSortsName("Kiyomi Sort (By yuji & McDude_73)");
        setRunSortName("Kiyomisort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(1024);
        setBogoSort(false);

    }

    private void kiyomiSorter(int[] array, int length) {
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
                    this.Writes.multiSwap(array, i - gap, i, 0.02D, true, false);
                    gap++;
                } else if (gap >= 2) {
                    gap--;
                }

                i--;
            }
        }
    }

    private boolean kiyomiIsSorted(int[] array, int length) {
        for (int i = 0; i < length - 1; i++) {
            if (this.Reads.compareValues(array[i], array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
        for (; !kiyomiIsSorted(array, length); kiyomiSorter(array, length))
            ;

    }

}

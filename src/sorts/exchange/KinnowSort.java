package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class KinnowSort extends Sort {

    public KinnowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Kinnow");
        setRunAllSortsName("Kinnow Sort (By yuji & McDude_73)");
        setRunSortName("Kinnowsort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(2048);
        setBogoSort(false);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean done = false;
        int gap = 1;

        while (!done) {
            int i = 0;
            done = true;
            while (i + gap < length) {
                if (this.Reads.compareValues(array[i], array[i + gap]) == 1) {
                    done = false;
                    this.Writes.multiSwap(array, i, i + gap, 0.2D, true, false);
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

}

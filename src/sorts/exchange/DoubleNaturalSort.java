package sorts.exchange;

import main.ArrayVisualizer;
import sorts.exchange.OptimizedGnomeSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class DoubleNaturalSort extends Sort {
    private OptimizedGnomeSort optignome;

    public DoubleNaturalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Double Natural");
        this.setRunAllSortsName("Double Natural Sort");
        this.setRunSortName("Double Naturalsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        optignome = new OptimizedGnomeSort(arrayVisualizer);
        int lefti = 1;
        int leftverifyi = 0;
        int righti = 1;
        int rightverifyi = currentLength;
        boolean anyswaps = true;
        boolean leftverifypass = false;
        boolean rightverifypass = false;
        while (!leftverifypass && !rightverifypass) {
            lefti = leftverifyi + 1;
            anyswaps = true;
            while (lefti + 1 <= currentLength && anyswaps) {
                Highlights.markArray(1, lefti - 1);
                Highlights.markArray(2, lefti);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[lefti - 1], array[lefti]) > 0) {
                    Writes.swap(array, lefti - 1, lefti, 0.05, true, false);
                } else {
                    anyswaps = false;
                }
                lefti++;
            }
            if (leftverifyi > 1) {
                leftverifyi--;
            } else {
                leftverifyi = 1;
            }
            leftverifypass = true;
            while (leftverifyi != currentLength && leftverifypass) {
                Highlights.markArray(1, leftverifyi - 1);
                Highlights.markArray(2, leftverifyi);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[leftverifyi - 1], array[leftverifyi]) <= 0) {
                    leftverifyi++;
                } else {
                    leftverifypass = false;
                    Writes.swap(array, leftverifyi - 1, leftverifyi, 0.05, true, false);
                }
            }
            righti = rightverifyi - 1;
            anyswaps = true;
            while (righti > 0 && anyswaps) {
                Highlights.markArray(1, righti - 1);
                Highlights.markArray(2, righti);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[righti - 1], array[righti]) > 0) {
                    Writes.swap(array, righti - 1, righti, 0.05, true, false);
                } else {
                    anyswaps = false;
                }
                righti--;
            }
            if (rightverifyi < currentLength - 1) {
                rightverifyi++;
            } else {
                rightverifyi = currentLength - 1;
            }
            rightverifypass = true;
            while (rightverifyi > 0 && rightverifypass) {
                Highlights.markArray(1, rightverifyi - 1);
                Highlights.markArray(2, rightverifyi);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[rightverifyi - 1], array[rightverifyi]) <= 0) {
                    rightverifyi--;
                } else {
                    rightverifypass = false;
                    Writes.swap(array, rightverifyi - 1, rightverifyi, 0.05, true, false);
                }
            }
        }
        optignome.customSort(array, 0, currentLength - 1, 0.05);
    }
}
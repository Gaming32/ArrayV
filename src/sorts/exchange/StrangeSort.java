package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StrangeSort extends Sort {
    public StrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strange");
        this.setRunAllSortsName("Strange Sort");
        this.setRunSortName("Strangesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2)
            return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int offset = 1;
        double mult = 1.0;
        int bound = 1;
        int base = bucketCount;
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            offset = 1;
            while (offset != currentLength) {
                mult = 1;
                bound = 1;
                while (offset + mult <= currentLength) {
                    Highlights.markArray(1, (int) (offset + mult / base) - 1);
                    Highlights.markArray(2, (int) (offset + mult) - 1);
                    Delays.sleep(0.1);
                    if (Reads.compareValues(array[(int) (offset + mult / base) - 1], array[(int) (offset + mult) - 1]) > 0) {
                        Writes.swap(array, (int) (offset + mult / base) - 1, (int) (offset + mult) - 1, 0.1, true, false);
                        if (mult == 1 / base) {
                            bound *= base;
                            mult = bound;
                        } else {
                            mult /= base;
                        }
                        anyswaps = true;
                    } else {
                        bound *= base;
                        mult = bound;
                    }
                }
                offset++;
            }
        }
    }
}
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class NoisySort extends Sort {
    public NoisySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Noisy");
        this.setRunAllSortsName("Noisy Sort");
        this.setRunSortName("Noisesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
        this.setQuestion("Enter the noise intensity for this sort:", 16);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1)
            return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int right = 1;
        int verifyi = 1;
        boolean verifypass = false;
        int base = bucketCount;
        while (!verifypass) {
            right = verifyi + 1;
            while (right <= currentLength) {
                left = verifyi;
                while (left <= right && right <= currentLength) {
                    Highlights.markArray(1, left - 1);
                    Highlights.markArray(2, right - 1);
                    Delays.sleep(0.005);
                    if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                        Writes.swap(array, left - 1, right - 1, 0.005, true, false);
                        if (right - 1 > verifyi) {
                            right--;
                        }
                        left = verifyi;
                    } else {
                        left++;
                    }
                }
                right += base;
            }
            if (verifyi - 1 > 0) {
                verifyi--;
            }
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                Highlights.markArray(1, verifyi - 1);
                Highlights.markArray(2, verifyi);
                Delays.sleep(0.005);
                if (Reads.compareValues(array[verifyi - 1], array[verifyi]) <= 0) {
                    verifyi++;
                } else {
                    verifypass = false;
                }
            }
        }
        
        
        //Highlights.markArray(1, (int) (offset + mult / base) - 1);
        //Highlights.markArray(2, (int) (offset + mult) - 1);
        //Delays.sleep(0.1);
        //if (Reads.compareValues(array[(int) (offset + mult / base) - 1], array[(int) (offset + mult) - 1]) > 0) {
        //Writes.swap(array, (int) (offset + mult / base) - 1, (int) (offset + mult) - 1, 0.1, true, false);
    }
}
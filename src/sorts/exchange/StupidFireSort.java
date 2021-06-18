package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Stupid Fire Sort is a variant of Fire Sort, in turn variant to Gnome Sort.

Where Fire Sort increments the swap check value by the length of the list,
Stupid Fire Sort only changes it by 1. This tiny difference lands the total
number of needed swaps, variable S, to reverse a list of N count values at
an unimaginable S = 0.125N^4 - 0.25N^3 + 1.125N^2 - N. Supposing we use 128
items, that's over a hundred times worse than regular Fire Sort, and four
thousand times worse than Gnome Sort! Now that's impractical!

RE8gTk9UIFVTRSBUSElTIFNPUlQsIEVWRVIh

*/
final public class StupidFireSort extends Sort {
    public StupidFireSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stupid Fire");
        this.setRunAllSortsName("Stupid Fire Sort");
        this.setRunSortName("Stupid Firesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1;
        int twistcheck = 0;
        int twistwait = 0;
        int twist = -1;
        int testi = 1;
        boolean testpass = false;
        boolean testreverse = false;
        boolean anyswaps = false;
        while (!testpass) {
            if (twistwait < 1) {
                twistcheck++;
                twistwait = twistcheck;
                twist *= -1;
            } else {
                twistwait--;
            }
            anyswaps = false;
            while (i + 1 <= currentLength && i >= 1 && !anyswaps) {
                if (Reads.compareValues(array[i - 1], array[i]) * twist > 0) {
                    Writes.swap(array, i - 1, i, 0.001, true, false);
                    i -= twist;
                    anyswaps = true;
                } else {
                    i += twist;
                }
            }
            if (i < 1) {
                i = currentLength - 1;
                testi = 1;
                testpass = true;
                while (testi != currentLength && testpass) {
                    if (Reads.compareValues(array[testi - 1], array[testi]) <= 0) {
                        testi++;
                    } else {
                        testpass = false;
                        testi = 1;
                        testreverse = true;
                        while (testi != currentLength && testreverse) {
                            if (Reads.compareValues(array[testi - 1], array[testi]) >= 0) {
                                testi++;
                            } else {
                                testreverse = false;
                            }
                        }
                    }
                }
                if (testreverse) {
                    i = 1;
                    twistwait = 0;
                }
            }
            if (i + 1 > currentLength) {
                i = 1;
                testi = 1;
                testpass = true;
                while (testi != currentLength && testpass) {
                    if (Reads.compareValues(array[testi - 1], array[testi]) <= 0) {
                        testi++;
                    } else {
                        testpass = false;
                        testi = 1;
                        testreverse = true;
                        while (testi != currentLength && testreverse) {
                            if (Reads.compareValues(array[testi - 1], array[testi]) >= 0) {
                                testi++;
                            } else {
                                testreverse = false;
                            }
                        }
                    }
                }
                if (testreverse) {
                    i = currentLength - 1;
                    twistwait = 0;
                }
            }
        }
    }
}
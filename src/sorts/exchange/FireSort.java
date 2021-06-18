package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Fire Sort is an impractical variant of Gnome Sort that forces a worst case.

Consider that the equation S = 0.5N^2 - 0.5N represents the number of swaps
needed, variable S, for the Gnome process to reverse any list of length N
values. Fire abuses this case by tracking the number of swaps that have
been made. When this count reaches T, an incrementing check number, then it
changes this limit by N. The Gnome process will continue, but in reverse.
Once the swap tracking reaches the new T value, the limit changes by N, and
the Gnome direction reverses again.

There are some additional situations that this algorithm could face. If the
process target reaches either bound of the list, an internal verification
system will run starting from the left. If the list is sorted, then there's
nothing more the algorithm needs to do. If it is not, the system will run
again for a reversed order. When it finds the list is sorted backward, the
limit trigger will iterate, and the Gnome direction will twist immediately.
If it is not, the verification system will fail out, the target will move
to the opposite side of the list, and continue sorting as normal.

Reversing a list of N values with Fire Sort will do S = 0.125N^3 + 0.5N^2
swaps, which is (0.25N(N + 4))/(N - 1) times more swaps than Gnome Sort.

*/
final public class FireSort extends Sort {
    public FireSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fire");
        this.setRunAllSortsName("Fire Sort");
        this.setRunSortName("Firesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
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
                twistcheck += currentLength;
                twistwait = twistcheck;
                twist *= -1;
            } else {
                twistwait--;
            }
            anyswaps = false;
            while (i + 1 <= currentLength && i >= 1 && !anyswaps) {
                if (Reads.compareValues(array[i - 1], array[i]) * twist > 0) {
                    Writes.swap(array, i - 1, i, 0.005, true, false);
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
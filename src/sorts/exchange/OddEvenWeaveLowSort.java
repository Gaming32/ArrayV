package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OddEvenWeaveLowSort extends Sort {
    public OddEvenWeaveLowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Odd-Even Weave (Low Prime)");
        this.setRunAllSortsName("Odd-Even Weave Sort (Low Prime)");
        this.setRunSortName("Odd-Even Weave (Low Prime)");
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
        int check = 1;
        int lastbound = 2;
        int lastmove = currentLength;
        int move = currentLength;
        int noswapsfor = 0;
        int gap = currentLength;
        int primetesti = 2;
        int i = 1;
        int boundi = 1;
        double primetestrunning = 1.0;
        boolean primetest = false;
        boolean anyswaps = false;
        boolean testpass = false;
        boolean visualaesthetic = true;
        while (!testpass) {
            i = check;
            anyswaps = false;
            Highlights.markArray(1, i - 1);
            Highlights.markArray(2, (i - 1) + gap);
            Delays.sleep(0.25);
            while ((i - 1) + gap < currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.25);
                if (Reads.compareValues(array[i - 1], array[(i - 1) + gap]) > 0) {
                    Writes.swap(array, i - 1, (i - 1) + gap, 0.25, true, false);
                    anyswaps = true;
                }
                i += move;
            }
            if (!anyswaps && gap != 1) {
                noswapsfor++;
                if (noswapsfor == move) {
                    noswapsfor = 0;
                    lastmove = move;
                    primetestrunning = move;
                    if (!visualaesthetic) {
                        while (primetestrunning == move) {
                            primetest = false;
                            primetesti = 2;
                            while (!primetest) {
                                if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                                    primetestrunning = primetestrunning / primetesti;
                                    primetest = true;
                                } else {
                                    primetesti++;
                                }
                            }
                        }
                        move = move / primetesti;
                    }
                    visualaesthetic = false;
                    if (move != 1) {
                        primetestrunning = move;
                        while (primetestrunning == move) {
                            primetest = false;
                            primetesti = 2;
                            while (!primetest) {
                                if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                                    primetestrunning = primetestrunning / primetesti;
                                    primetest = true;
                                } else {
                                    primetesti++;
                                }
                            }
                        }
                        gap = move / primetesti;
                    } else {
                        move = lastmove;
                        gap = 1;
                    }
                    check = 0;
                }
            } else {
                noswapsfor = 0;
            }
            if (gap == 1) {
                if (lastbound > 1) {
                    boundi = lastbound - 1;
                } else {
                    boundi = 1;
                }
                testpass = true;
                while (boundi < currentLength && testpass) {
                    Highlights.markArray(1, boundi - 1);
                    Highlights.markArray(2, boundi);
                    Delays.sleep(0.25);
                    if (Reads.compareValues(array[boundi - 1], array[boundi]) <= 0) {
                        boundi++;
                    } else {
                        testpass = false;
                        lastbound = boundi;
                        check = boundi;
                    }
                }
            } else {
                check = (check % move) + 1;
            }
        }
    }
}
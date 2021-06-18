package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Gnome Weave by Highest Prime is a reversal of Gnome Weave by Lowest Prime.

The prime check runs to the first result in Lowest Prime, but Highest Prime
runs it to the last result.

Because the sorting algorithm divides by prime numbers over and over again,
list lengths with recurrent factor trees cause it to act accordingly. Also,
if the length is a prime number itself, it will resort to plain OptiGnome.
Try lengths like 1365, 2310, or 4199, and it will appear more diverse.

*/
final public class GnomeWeaveHighSort extends Sort {
    public GnomeWeaveHighSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Gnome Weave (High Prime)");
        this.setRunAllSortsName("Gnome Weave Sort (High Prime)");
        this.setRunSortName("Gnome Weave (High Prime)");
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
        int gap = currentLength;
        int icheck = 1;
        int i = 1;
        int bound = 1;
        int primetesti = 2;
        double primetestrunning = 1.0;
        boolean primetest = false;
        boolean finalgap = false;
        while (!finalgap) {
            i = icheck;
            bound = icheck;
            Highlights.markArray(1, i - 1);
            Highlights.markArray(2, (i - 1) + gap);
            Delays.sleep(0.25);
            while ((i - 1) + gap < currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.25);
                if (Reads.compareValues(array[i - 1], array[(i - 1) + gap]) > 0) {
                    Writes.swap(array, i - 1, (i - 1) + gap, 0.25, true, false);
                    if (i - gap > 0) {
                        i -= gap;
                    }
                } else {
                    bound += gap;
                    i = bound;
                }
            }
            if (gap == 1) {
                finalgap = true;
            }
            if (icheck + 1 > gap && !finalgap) {
                primetestrunning = gap;
                while (primetestrunning != 1) {
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
                gap = gap / primetesti;
                icheck = 1;
            } else {
                icheck++;
            }
        }
    }
}
package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Markov Sort is like Gnome Sort, but the next element to be inserted
 * randomly walks within the sorted section until it is in the correct position.
 * 
 * @author invented by Blasterfreund
 * @author implemented in Java by Sam Walko (Anonymous0726)
 * @author refactored by EmeraldBlock
 */
final public class MarkovSort extends BogoSorting {
    public MarkovSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Markov");
        this.setRunAllSortsName("Markov Sort");
        this.setRunSortName("Markov sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length-1; ++i) {
            int walk = i+1;
            while (
                   (walk==0 ? false : Reads.compareIndices(array, walk-1, walk, this.delay, true)>0)
                || (walk>i ? false : Reads.compareIndices(array, walk, walk+1, this.delay, true)>0)
            ) {
                int c = (walk==0 || walk<=i && BogoSorting.randBoolean()) ? 1 : -1;
                Writes.swap(array, walk, walk+c, this.delay, true, false);
                walk += c;
            }
        }
    }
}

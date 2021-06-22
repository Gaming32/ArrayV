package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * The Pattern-Defeating Insertion Sort was made by Gaming32.
 * This variant replaced overwrites with swaps.
 *  
 */

/**
 * @author Yuri-chan2007
 *
 */
public final class PatternDefeatingGnomeSort extends Sort {

    public PatternDefeatingGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Gnome");
        this.setRunAllSortsName("Pattern-Defeating Gnome Sort");
        this.setRunSortName("Pattern-Defeating Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void gnomeSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if(Reads.compareIndices(array, i - 1, i++, sleep, true) == 1) {
            while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) == 1) i++;
            Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        else while(i < b && Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) i++;

        Highlights.clearMark(2);

        while(i < b) {
            
            int pos = i;
            while(pos > a && Reads.compareValues(array[pos - 1], array[pos]) > 0){
                Writes.swap(array, pos - 1, pos, sleep, true, auxwrite);
                pos--;
            }
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        gnomeSort(array, 0, sortLength, 1.0D, false);

    }

}

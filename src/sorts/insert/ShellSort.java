package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.ShellSorting;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps

final public class ShellSort extends ShellSorting {
    public ShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Shell");
        this.setRunAllSortsName("Shell Sort");
        this.setRunSortName("Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void finishQuickShell(int[] array, int currentLen) {
        this.quickShellSort(array, 0, currentLen);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.shellSort(array, currentLength);
    }
}
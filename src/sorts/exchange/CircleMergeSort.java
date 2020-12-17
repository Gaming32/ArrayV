package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.IterativeCircleSorting;

/*
 * 
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

final public class CircleMergeSort extends IterativeCircleSorting {   
    public CircleMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Circle Merge");
        this.setRunAllSortsName("Circle Merge Sort");
        this.setRunSortName("Circle Mergesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
    	this.end = sortLength;
    	int n = 1;
    	for(; n <= sortLength; n*=2) {
            int numberOfSwaps = 0;
            do {
                numberOfSwaps = this.circleSortRoutine(array, n, 0.2);
            } while (numberOfSwaps != 0);
        }
    }
}
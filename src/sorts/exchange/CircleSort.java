package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.CircleSorting;

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

final public class CircleSort extends CircleSorting {   
    public CircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Circle");
        this.setRunAllSortsName("Circle Sort");
        this.setRunSortName("Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void singleRoutine(int[] array, int length) {
        this.circleSortRoutine(array, 0, length - 1, 0, 0.1);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int numberOfSwaps = 0;
        do {
            numberOfSwaps = this.circleSortRoutine(array, 0, sortLength - 1, 0, 1);
        } while (numberOfSwaps != 0);
    }
}
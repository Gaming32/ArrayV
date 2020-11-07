package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
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

final public class IntroCircleSort extends CircleSorting {
    public IntroCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Intro Circle");
        this.setRunAllSortsName("Introspective Circle Sort");
        this.setRunSortName("Introspective Circlesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int iterations = 0;
        int threshold = (int) (Math.log(length) / Math.log(2)) / 2;
        
        do {
            iterations++;
            
            if(iterations >= threshold) {
                BinaryInsertionSort binaryInserter = new BinaryInsertionSort(this.arrayVisualizer);
                binaryInserter.customBinaryInsert(array, 0, length, 0.1);
                break;
            }
        } while (this.circleSortRoutine(array, 0, length - 1, 0, 1) != 0);
    }
}
package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

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

final public class HeavyHeapSort extends HeapSorting {
    public HeavyHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Heavy Heap");
        this.setRunAllSortsName("Heavy Heap Sort");
        this.setRunSortName("Heavy Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void bitReversal(int[] array, int a, int b) {
        int len = b-a, m = 0;
        int d1 = len>>1, d2 = d1+(d1>>1);
                    
        for(int i = 1; i < len-1; i++) {
            int j = d1;
            
            for(
                int k = i, n = d2; 
                (k&1) == 0; 
                j -= n, k >>= 1, n >>= 1
            );
            m += j;
            if(m > i) Writes.swap(array, a+i, a+m, 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        boolean dir = false;
        for (int i = 0; i < length; i++) {
            this.heapify(array, i, length, 0.002, dir);
            dir = !dir;
        }
        Writes.changeReversals(1);
        for (int i = 1, j = length - 1; i < j; i += 2, j -= 2) {
            Writes.swap(array, i, j, 1, true, false);
        }
        bitReversal(array, 0, length);
        bitReversal(array, 0, length / 2);
        bitReversal(array, length / 2, length);
    }
}
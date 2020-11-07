package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
Copyright (c) rosettacode.org
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.3
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

final public class CycleSort extends Sort {
    public CycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Cycle");
        this.setRunAllSortsName("Cycle Sort");
        this.setRunSortName("Cyclesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int cycleStart = 0; cycleStart < length - 1; cycleStart++) {
            int val = array[cycleStart];
            
            /*
              Count the number of values that are smaller 
              than val since cycleStart
             */
            
            int pos = cycleStart;
            Highlights.markArray(3, pos);
            
            for (int i = cycleStart + 1; i < length; i++) {
                Highlights.markArray(2, i);
                Delays.sleep(0.01);
                
                if (Reads.compareValues(array[i], val) == -1) {
                    pos++;
                    Highlights.markArray(1, pos);
                    Delays.sleep(0.01);
                }

            }

            // there aren't any
            if (pos == cycleStart) {
                Highlights.markArray(1, pos);
                continue;
            }

            // Skip duplicates
            while (val == array[pos]) {
                pos++;
                Highlights.markArray(1, pos);
            }

            // Put val into final position
            int tmp = array[pos];
            Writes.write(array, pos, val, 0.02, true, false);
            val = tmp;

            /*
              Repeat as long as we can find values to swap
              otherwise start new cycle
             */
            while (pos != cycleStart) {
                pos = cycleStart;
                Highlights.markArray(3, pos);
                
                for (int i = cycleStart + 1; i < length; i++) {
                    Highlights.markArray(2, i);
                    Delays.sleep(0.01);
                    
                    if (Reads.compareValues(array[i], val) == -1) {
                        pos++;
                        Highlights.markArray(1, pos);
                        Delays.sleep(0.01);
                    }
                }

                while (val == array[pos]) {
                    pos++;
                    Highlights.markArray(1, pos);
                }

                tmp = array[pos];
                Writes.write(array, pos, val, 0.02, true, false);
                val = tmp;
            }
        }
    }
}
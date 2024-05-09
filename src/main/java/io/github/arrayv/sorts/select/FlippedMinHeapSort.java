package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

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

/*
modified by Lucy Phipps from ../templates/HeapSorting.java and MinHeapSort.java
the only real changes are subtracting every array access from (length - 1)
and removing the Writes.reverse() at the end
the rest is just compacting the code a bit
*/
@SortMeta(name = "Flipped Min Heap")
public final class FlippedMinHeapSort extends Sort {
    public FlippedMinHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void siftDown(int[] array, int length, int root, int dist) {
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compareIndices(array, length - leaf, length - leaf - 1, 0, true) == 1) {
                leaf++;
            }
            if (Reads.compareIndices(array, length - root, length - leaf, 1, true) == 1) {
                Writes.swap(array, length - root, length - leaf, 0, true, false);
                root = leaf;
            } else
                break;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length / 2; i >= 1; i--) {
            siftDown(array, length, i, length);
        }
        for (int i = length; i > 1; i--) {
            Writes.swap(array, length - 1, length - i, 1, true, false);
            siftDown(array, length, 1, i - 1);
        }
    }
}

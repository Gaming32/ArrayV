package io.github.arrayv.sorts.tests;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Rotations;

/*
 *
The MIT License (MIT)

Copyright (c) 2021 ArrayV 4.0 Team

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

public final class RotationTest extends Sort {
    final double BLOCK_DIV = 6.98;

    public RotationTest(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rotations");
        this.setRunAllSortsName("Rotation Test");
        this.setRunSortName("Rotation Test");
        this.setCategory("Tests");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int blockSize = (int)(length / BLOCK_DIV);

        for (int i = 0; i < blockSize; i++) {
            Writes.write(array, i, i + length - blockSize, 0.05, false, false);
        }
        for (int i = blockSize; i < length; i++) {
            Writes.write(array, i, i - blockSize, 0.05, false, false);
        }

        Writes.resetStatistics();

        Delays.sleep(500);

        Rotations.juggling(array, 0, blockSize, length - blockSize, 1, true, false);

        Delays.sleep(1);
        Delays.changePaused(true);
        Writes.changeWrites(0);
        Delays.sleep(1);

        for (int i = 0; i < length - blockSize; i++) {
            Writes.write(array, i, i + blockSize, 0.05, false, false);
        }
        for (int i = length - blockSize; i < length; i++) {
            Writes.write(array, i, i - (length - blockSize), 0.05, false, false);
        }

        Writes.resetStatistics();

        Delays.sleep(500);

        Rotations.juggling(array, 0, length - blockSize, blockSize, 1, true, false);
    }
}

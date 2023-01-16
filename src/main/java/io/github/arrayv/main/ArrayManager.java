package io.github.arrayv.main;

import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.utils.*;

import java.util.Arrays;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2020-2022 ArrayV Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class ArrayManager {
    private final io.github.arrayv.utils.Shuffles[] shuffleTypes;
    private final io.github.arrayv.utils.Distributions[] distributionTypes;
    private final String[] shuffleIDs;
    private final String[] distributionIDs;

    private boolean hadDistributionAllocationError;

    private volatile boolean mutableLength;

    private final ArrayVisualizer arrayVisualizer;
    private final Delays Delays;
    private final Highlights Highlights;
    private ShuffleGraph shuffle;
    private Distributions distribution;
    private final Writes Writes;

    public ArrayManager(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        this.shuffle = ShuffleGraph.single(Shuffles.RANDOM);
        this.distribution = Distributions.LINEAR;
        this.shuffleTypes = Shuffles.values();
        this.distributionTypes = Distributions.values();

        hadDistributionAllocationError = false;

        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Writes = arrayVisualizer.getWrites();

        this.shuffleIDs = new String[this.shuffleTypes.length];
        for (int i = 0; i < this.shuffleTypes.length; i++)
            this.shuffleIDs[i] = this.shuffleTypes[i].getName();

        this.distributionIDs = new String[this.distributionTypes.length];
        for (int i = 0; i < this.distributionTypes.length; i++)
            this.distributionIDs[i] = this.distributionTypes[i].getName();

        this.mutableLength = true;
    }

    public boolean isLengthMutable() {
        return this.mutableLength;
    }

    public void toggleMutableLength(boolean mutableLength) {
        this.mutableLength = mutableLength;
    }

    //TODO: Fix minimum to zero
    public void initializeArray(int[] array) {
        if (arrayVisualizer.doingStabilityCheck()) {
            arrayVisualizer.resetStabilityTable();
            arrayVisualizer.resetIndexTable();
        }

        int currentLen = arrayVisualizer.getCurrentLength();

        int[] temp;
        try {
            temp = new int[currentLen];
        } catch (OutOfMemoryError e) {
            if (!hadDistributionAllocationError)
                JErrorPane.invokeCustomErrorMessage("Failed to allocate temporary array for distribution. (will use main array, which may have side-effects.)");
            hadDistributionAllocationError = true;
            temp = array;
        }
        distribution.initializeArray(temp, this.arrayVisualizer);

        double uniqueFactor = (double)currentLen/arrayVisualizer.getUniqueItems();
        for (int i = 0; i < currentLen; i++)
            temp[i] = (int)(uniqueFactor*(int)(temp[i]/uniqueFactor))+(int)uniqueFactor/2;

        System.arraycopy(temp, 0, array, 0, currentLen);
        arrayVisualizer.updateNow();
    }

    public String[] getShuffleIDs() {
        return this.shuffleIDs;
    }
    public Shuffles[] getShuffles() {
        return this.shuffleTypes;
    }
    public ShuffleGraph getShuffle() {
        return this.shuffle;
    }

    /**
     * @deprecated This method is deprecatated. Please use {@link #setShuffleSingle(Shuffles)} or {@link #setShuffle(ShuffleGraph)} instead.
     * @see #setShuffleSingle(Shuffles)
     * @see #setShuffle(ShuffleGraph)
     */
    public void setShuffle(Shuffles choice) {
        this.setShuffleSingle(choice);
    }

    public ShuffleGraph setShuffle(ShuffleGraph graph) {
        this.shuffle = graph;
        return graph; // return the shuffle so additional methods can be called on it
    }

    public ShuffleGraph setShuffleSingle(Shuffles shuffle) {
        return this.setShuffle(ShuffleGraph.single(shuffle));
    }
    public ShuffleGraph setShuffleSingle(Distributions distribution) {
        return this.setShuffle(ShuffleGraph.single(distribution));
    }
    public ShuffleGraph setShuffleSingle(Distributions distribution, boolean warped) {
        return this.setShuffle(ShuffleGraph.single(distribution, warped));
    }

    public String[] getDistributionIDs() {
        return this.distributionIDs;
    }
    public Distributions[] getDistributions() {
        return this.distributionTypes;
    }
    public Distributions getDistribution() {
        return this.distribution;
    }
    public void setDistribution(Distributions choice) {
        this.distribution = choice;
        this.distribution.selectDistribution(arrayVisualizer.getArray(), arrayVisualizer);
        if (!arrayVisualizer.isActive())
            this.initializeArray(arrayVisualizer.getArray());
    }

    public boolean containsShuffle(Shuffles shuffle) {
        return this.shuffle.contains(new ShuffleInfo(shuffle));
    }

    public void shuffleArray(int[] array, int currentLen, ArrayVisualizer arrayVisualizer) {
        this.initializeArray(array);

        String tmp = arrayVisualizer.getHeading();
        arrayVisualizer.setHeading("Shuffling...");

        double speed = Delays.getSleepRatio();

        if (arrayVisualizer.isActive()) {
            double sleepRatio = arrayVisualizer.getCurrentLength()/1024d;
            sleepRatio *= shuffle.getSleepRatio();
            Delays.setSleepRatio(sleepRatio);
        }

        shuffle.shuffleArray(array, this.arrayVisualizer);

        Delays.setSleepRatio(speed);

        Highlights.clearAllMarks();
        arrayVisualizer.setHeading(tmp);
    }

    private void stableShuffle(int[] array, int length) {
        double speed = Delays.getSleepRatio();

        if (arrayVisualizer.isActive()) {
            double sleepRatio = arrayVisualizer.getCurrentLength()/1024d;
            Delays.setSleepRatio(sleepRatio);
        }

        int[] counts    = new int[length];
        int[] prefixSum = new int[length];
        int[] table     = arrayVisualizer.getStabilityTable();

        for (int i = 0; i < length; i++)
            counts[array[i]]++;

        prefixSum[0] = counts[0];
        for (int i = 1; i < length; i++)
            prefixSum[i] = counts[i] + prefixSum[i-1];

        for (int i = 0, j = 0; j < length; i++) {
            while (counts[i] > 0) {
                table[j++] = i;
                counts[i]--;
            }
        }

        for (int i = length-1; i >= 0; i--)
            Writes.write(array, i, --prefixSum[array[i]], 0.5, true, false);

        arrayVisualizer.setIndexTable();

        Delays.setSleepRatio(speed);
    }

    public void refreshArray(int[] array, int currentLen, ArrayVisualizer arrayVisualizer) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            JErrorPane.invokeErrorMessage(e);
        }

        arrayVisualizer.resetAllStatistics();
        Highlights.clearAllMarks();

        arrayVisualizer.setHeading("");
        if (!arrayVisualizer.useAntiQSort()) {
            this.shuffleArray(array, currentLen, arrayVisualizer);

            if (arrayVisualizer.doingStabilityCheck())
                this.stableShuffle(array, currentLen);

            int[] validateArray = arrayVisualizer.getValidationArray();
            if (validateArray != null) {
                System.arraycopy(array, 0, validateArray, 0, currentLen);
                Arrays.sort(validateArray, 0, currentLen);
                if (arrayVisualizer.reversedComparator()) {
                    for (int i = 0, j = currentLen - 1; i < j; i++, j--) {
                        int temp = validateArray[i];
                        validateArray[i] = validateArray[j];
                        validateArray[j] = temp;
                    }
                }
            }
        }

        Highlights.clearAllMarks();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            JErrorPane.invokeErrorMessage(e);
        }

        arrayVisualizer.resetAllStatistics();
    }
}

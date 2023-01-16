package io.github.arrayv.utils;

import io.github.arrayv.main.ArrayVisualizer;

import java.text.DecimalFormat;

public final class Statistics {
    private long frameTimeMillis;

    private String sortCategory;
    private String sortHeading;
    private String sortExtraHeading;
    private String arrayLength;

    private String framerate;
    private String sortDelay;
    private String visualTime;
    private String estSortTime;

    private String comparisonCount;
    private String swapCount;
    private String reversalCount;

    private String mainWriteCount;
    private String auxWriteCount;

    private String auxAllocAmount;

    private String segments;

    private final DecimalFormat formatter;

    public Statistics(ArrayVisualizer arrayVisualizer) {
        this.formatter = arrayVisualizer.getNumberFormat();
        this.updateStats(arrayVisualizer);
    }

    public int[] findSegments(int[] array, int length, boolean reversed) {
        int runs = 1;
        int correct = 0;
        for (int i = 0; i < length-1; i++) {
            if (!reversed && array[i] > array[i+1]) runs++;
            else if (reversed && array[i] < array[i+1]) runs++;
            else correct++;
        }
        int[] result = new int[2];
        result[0] = runs;
        result[1] = (int) ((((double) correct) / (length - 1)) * 100);
        return result;
    }

    public void updateStats(ArrayVisualizer arrayVisualizer) {
        this.sortCategory = arrayVisualizer.getCategory();
        this.sortHeading = arrayVisualizer.getHeading();
        this.sortExtraHeading = arrayVisualizer.getExtraHeading();
        int showUnique = Math.min(arrayVisualizer.getUniqueItems(), arrayVisualizer.getCurrentLength());
        this.arrayLength = this.formatter.format(arrayVisualizer.getCurrentLength()) + " Numbers"
            + ", " + this.formatter.format(showUnique) + " Unique";

        if (frameTimeMillis == 0) {
            this.framerate = ">1000 FPS";
        } else {
            this.framerate = (int)(1000.0 / frameTimeMillis) + " FPS";
        }
        this.sortDelay = "Delay: " + arrayVisualizer.getDelays().displayCurrentDelay();
        this.visualTime = "Visual Time: " + arrayVisualizer.getTimer().getVisualTime();
        this.estSortTime = "Sort Time: " + arrayVisualizer.getTimer().getRealTime();

        this.comparisonCount = arrayVisualizer.getReads().getStats();
        this.swapCount = arrayVisualizer.getWrites().getSwaps();
        this.reversalCount = arrayVisualizer.getWrites().getReversals();

        this.mainWriteCount = arrayVisualizer.getWrites().getMainWrites();
        this.auxWriteCount = arrayVisualizer.getWrites().getAuxWrites();

        this.auxAllocAmount = arrayVisualizer.getWrites().getAllocAmount();

        int[] shadowarray    = arrayVisualizer.getArray();
        int[] rawSegments    = this.findSegments(shadowarray, arrayVisualizer.getCurrentLength(), arrayVisualizer.reversedComparator());
        String plural = rawSegments[0] == 1 ? "" : "s";
        this.segments        = rawSegments[1] + "% Sorted (" + rawSegments[0] + " Segment" + plural + ")";
    }

    public void setFrameTimeMillis(long frameTimeMillis) {
        this.frameTimeMillis = frameTimeMillis;
    }

    public String getSortIdentity() {
        return this.sortCategory + ": " + this.sortHeading;
    }
    public String getArrayLength() {
        return this.arrayLength + this.sortExtraHeading;
    }
    public String getFramerate() {
        return this.framerate;
    }
    public String getSortDelay() {
        return this.sortDelay;
    }
    public String getVisualTime() {
        return this.visualTime;
    }
    public String getEstSortTime() {
        return this.estSortTime;
    }
    public String getComparisonCount() {
        return this.comparisonCount;
    }
    public String getSwapCount() {
        return this.swapCount;
    }
    public String getReversalCount() {
        return this.reversalCount;
    }
    public String getMainWriteCount() {
        return this.mainWriteCount;
    }
    public String getAuxWriteCount() {
        return this.auxWriteCount;
    }
    public String getAuxAllocAmount() {
        return this.auxAllocAmount;
    }
    public String getSegments() {
        return this.segments;
    }
}

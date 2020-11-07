package utils;

import java.text.DecimalFormat;

import main.ArrayVisualizer;

final public class Statistics {
    private String sortCategory;
    private String sortHeading;
    private String sortExtraHeading;
    private String arrayLength;
    
    private String sortDelay;
    private String visualTime;
    private String estSortTime;
    
    private String comparisonCount;
    private String swapCount;
    private String reversalCount;
    
    private String mainWriteCount;
    private String auxWriteCount;

    private String auxAllocAmount;
    
    private DecimalFormat formatter;
    
    public Statistics(ArrayVisualizer ArrayVisualizer) {
        this.formatter = ArrayVisualizer.getNumberFormat();
        this.updateStats(ArrayVisualizer);
    }

    public void updateStats(ArrayVisualizer ArrayVisualizer) {
        this.sortCategory = ArrayVisualizer.getCategory();
        this.sortHeading = ArrayVisualizer.getHeading();
        this.sortExtraHeading = ArrayVisualizer.getExtraHeading();
        this.arrayLength = this.formatter.format(ArrayVisualizer.getCurrentLength()) + " Numbers";
                //+ ", " + this.formatter.format(ArrayVisualizer.getCurrentLength() / ArrayVisualizer.getEqualItems()) + " Unique";
        
        this.sortDelay = "Delay: " + ArrayVisualizer.getDelays().displayCurrentDelay() + "ms";
        this.visualTime = "Visual Time: " + ArrayVisualizer.getTimer().getVisualTime();
        this.estSortTime = "Sort Time: " + ArrayVisualizer.getTimer().getRealTime();
        
        this.comparisonCount = ArrayVisualizer.getReads().getStats();
        this.swapCount = ArrayVisualizer.getWrites().getSwaps();
        this.reversalCount = ArrayVisualizer.getWrites().getReversals();
        
        this.mainWriteCount = ArrayVisualizer.getWrites().getMainWrites();
        this.auxWriteCount = ArrayVisualizer.getWrites().getAuxWrites();

        this.auxAllocAmount = ArrayVisualizer.getWrites().getAllocAmount();
    }
    
    public String getSortIdentity() {
        return this.sortCategory + ": " + this.sortHeading;
    }
    public String getArrayLength() {
        return this.arrayLength + this.sortExtraHeading;
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
}
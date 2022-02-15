package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Reads;
import io.github.arrayv.utils.Writes;

public abstract class Sort {
    private boolean sortEnabled;

    private String sortListName;
    private String runAllSortsName;
    private String runSortName;

    private String category;

    private boolean bucketSort;
    private boolean radixSort;
    private boolean unreasonablySlow;
    private boolean bogoSort;

    private int unreasonableLimit;

    private String question;
    private int defaultAnswer;

    protected ArrayVisualizer arrayVisualizer;

    protected Delays Delays;
    protected Highlights Highlights;
    protected Reads Reads;
    protected Writes Writes;

    protected Sort(ArrayVisualizer arrayVisualizer) {
        this.enableSort(true);           // If set to false, ArrayV won't load the sort

        this.setSortListName("");        // Displays in the 'Choose Sort' menu
        this.setRunAllSortsName("");     // Displays during sorting threads
        this.setRunSortName("");         // Displays when a sort is picked from 'Choose Sort'
        this.setCategory("");            // Shown at the top-left corner of the window

        this.setBucketSort(false);       // Slightly changes the 'Customize Sort' dialog
        this.setRadixSort(false);        // Also slightly changes the 'Customize Sort' dialog

        this.setUnreasonablySlow(false); // Indicates a sort is so inefficient that it will run for a very long time even after clicking 'Skip Sort'
        this.setUnreasonableLimit(0);    // If a sort is 'unreasonably slow', a warning will pop up if the array length is more than this number
        this.setBogoSort(false);         // Slightly changes the 'unreasonably slow' dialog

        this.setQuestion(null, 0);       // Asks a specific question before this sort is run

        this.arrayVisualizer = arrayVisualizer;

        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Reads = arrayVisualizer.getReads();
        this.Writes = arrayVisualizer.getWrites();
    }

    public boolean isFromExtraSorts() {
        return arrayVisualizer.getSortAnalyzer().didSortComeFromExtra(getClass());
    }

    public boolean isSortEnabled() {
        return this.sortEnabled;
    }

    public String getSortListName() {
        return this.sortListName;
    }

    public String getRunAllSortsName() {
        return this.runAllSortsName;
    }

    public String getRunSortName() {
        return this.runSortName;
    }

    public String getCategory() {
        return this.category;
    }

    /**
     * Whether this sort is a comparison sort or a distribution sort
     * @deprecated This method now always returns false, as this information is no longer stored
     * @return false
     */
    @Deprecated
    public boolean isComparisonBased() {
        return false;
    }

    public boolean usesBuckets() {
        return this.bucketSort;
    }

    public boolean isRadixSort() {
        return this.radixSort;
    }

    public boolean isUnreasonablySlow() {
        return this.unreasonablySlow;
    }

    public int getUnreasonableLimit() {
        return this.unreasonableLimit;
    }

    public boolean isBogoSort() {
        return this.bogoSort;
    }

    public String getQuestion() {
        return this.question;
    }

    public int getDefaultAnswer() {
        return this.defaultAnswer;
    }

    protected void enableSort(boolean Bool) {
        this.sortEnabled = Bool;
    }

    protected void setSortListName(String ID) {
        this.sortListName = ID;
    }

    protected void setRunAllSortsName(String ID) {
        this.runAllSortsName = ID;
    }

    protected void setRunSortName(String ID) {
        this.runSortName = ID;
    }

    protected void setCategory(String ID) {
        this.category = ID;
    }

    /**
     * Sets whether this sort is a comparison sort or a distribution sort
     * @deprecated This method doesn't do anything, as this information is no longer stored
     * @param comparisonBased Whether this sort is a comparison sort or a distribution sort
     */
    @Deprecated
    public void setComparisonBased(boolean comparisonBased) {
    }

    public void setBucketSort(boolean Bool) {
        this.bucketSort = Bool;
    }

    protected void setRadixSort(boolean Bool) {
        this.radixSort = Bool;
    }

    public void setUnreasonablySlow(boolean Bool) {
        this.unreasonablySlow = Bool;
    }

    public void setUnreasonableLimit(int number) {
        this.unreasonableLimit = number;
    }

    protected void setBogoSort(boolean Bool) {
        this.bogoSort = Bool;
    }

    protected void setQuestion(String question) {
        this.question = question;
    }

    protected void setQuestion(String question, int defaultAnswer) {
        this.question = question;
        this.defaultAnswer = defaultAnswer;
    }

    public int validateAnswer(int answer) {
        return answer;
    }

    public abstract void runSort(int[] array, int sortLength, int bucketCount) throws Exception; //bucketCount will be zero for comparison-based sorts
}

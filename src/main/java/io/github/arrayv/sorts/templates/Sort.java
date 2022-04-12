package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Reads;
import io.github.arrayv.utils.Writes;
import io.github.arrayv.sortdata.SortMeta;

public abstract class Sort {
    private Object[] deprecatedMetadataTable = null;

    protected ArrayVisualizer arrayVisualizer;

    protected Delays Delays;
    protected Highlights Highlights;
    protected Reads Reads;
    protected Writes Writes;

    protected Sort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;

        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Reads = arrayVisualizer.getReads();
        this.Writes = arrayVisualizer.getWrites();
    }

    private void initDeprecatedMetadataTable() {
        if (deprecatedMetadataTable != null) return;
        deprecatedMetadataTable = new Object[] {
            true, "", "", "", "", false, false, false, false, 0, null, 0
        };
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isFromExtraSorts() {
        return arrayVisualizer.getSortAnalyzer().didSortComeFromExtra(getClass());
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isSortEnabled() {
        initDeprecatedMetadataTable();
        return (boolean)deprecatedMetadataTable[0];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getSortListName() {
        initDeprecatedMetadataTable();
        return (String)deprecatedMetadataTable[1];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getRunAllSortsName() {
        initDeprecatedMetadataTable();
        return (String)deprecatedMetadataTable[2];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getRunSortName() {
        initDeprecatedMetadataTable();
        return (String)deprecatedMetadataTable[3];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getCategory() {
        initDeprecatedMetadataTable();
        return (String)deprecatedMetadataTable[4];
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

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean usesBuckets() {
        initDeprecatedMetadataTable();
        return (boolean)deprecatedMetadataTable[5];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isRadixSort() {
        initDeprecatedMetadataTable();
        return (boolean)deprecatedMetadataTable[6];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isUnreasonablySlow() {
        initDeprecatedMetadataTable();
        return (boolean)deprecatedMetadataTable[7];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public int getUnreasonableLimit() {
        initDeprecatedMetadataTable();
        return (int)deprecatedMetadataTable[9];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isBogoSort() {
        initDeprecatedMetadataTable();
        return (boolean)deprecatedMetadataTable[8];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getQuestion() {
        initDeprecatedMetadataTable();
        return (String)deprecatedMetadataTable[10];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public int getDefaultAnswer() {
        initDeprecatedMetadataTable();
        return (int)deprecatedMetadataTable[11];
    }

    protected void enableSort(boolean Bool) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[0] = Bool;
    }

    protected void setSortListName(String ID) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[1] = ID;
    }

    protected void setRunAllSortsName(String ID) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[2] = ID;
    }

    protected void setRunSortName(String ID) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[3] = ID;
    }

    protected void setCategory(String ID) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[4] = ID;
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
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[5] = Bool;
    }

    protected void setRadixSort(boolean Bool) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[6] = Bool;
    }

    public void setUnreasonablySlow(boolean Bool) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[7] = Bool;
    }

    public void setUnreasonableLimit(int number) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[9] = number;
    }

    protected void setBogoSort(boolean Bool) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[8] = Bool;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setQuestion(String question) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[10] = question;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setQuestion(String question, int defaultAnswer) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[10] = question;
        deprecatedMetadataTable[11] = defaultAnswer;
    }

    public static int validateAnswer(int answer) {
        return answer;
    }

    public abstract void runSort(int[] array, int sortLength, int bucketCount) throws Exception; //bucketCount will be zero for comparison-based sorts
}

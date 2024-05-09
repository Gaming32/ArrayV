package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Reads;
import io.github.arrayv.utils.Writes;

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
        if (deprecatedMetadataTable != null)
            return;
        deprecatedMetadataTable = new Object[] {
                true, "", "", "", "", false, false, false, 0, null, 0
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
        return (boolean) deprecatedMetadataTable[0];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getSortListName() {
        initDeprecatedMetadataTable();
        return (String) deprecatedMetadataTable[1];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getRunAllSortsName() {
        initDeprecatedMetadataTable();
        return (String) deprecatedMetadataTable[2];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getRunSortName() {
        initDeprecatedMetadataTable();
        return (String) deprecatedMetadataTable[3];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getCategory() {
        initDeprecatedMetadataTable();
        return (String) deprecatedMetadataTable[4];
    }

    /**
     * Whether this sort is a comparison sort or a distribution sort
     *
     * @deprecated This method now always returns false, as this information is no
     *             longer stored
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
        return (boolean) deprecatedMetadataTable[5];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isRadixSort() {
        initDeprecatedMetadataTable();
        return (boolean) deprecatedMetadataTable[6];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isUnreasonablySlow() {
        initDeprecatedMetadataTable();
        return (int) deprecatedMetadataTable[8] > 0;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public int getUnreasonableLimit() {
        initDeprecatedMetadataTable();
        return (int) deprecatedMetadataTable[8];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public boolean isBogoSort() {
        initDeprecatedMetadataTable();
        return (boolean) deprecatedMetadataTable[7];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public String getQuestion() {
        initDeprecatedMetadataTable();
        return (String) deprecatedMetadataTable[9];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public int getDefaultAnswer() {
        initDeprecatedMetadataTable();
        return (int) deprecatedMetadataTable[10];
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void enableSort(boolean enabled) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[0] = enabled;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setSortListName(String listName) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[1] = listName;
    }

    protected void setRunAllSortsName(String showcaseName) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[2] = showcaseName;
    }

    protected void setRunSortName(String runName) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[3] = runName;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setCategory(String category) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[4] = category;
    }

    /**
     * Sets whether this sort is a comparison sort or a distribution sort
     *
     * @deprecated This method doesn't do anything, as this information is no longer
     *             stored
     * @param comparisonBased Whether this sort is a comparison sort or a
     *                        distribution sort
     */
    @Deprecated
    public void setComparisonBased(boolean comparisonBased) {
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public void setBucketSort(boolean bucketSort) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[5] = bucketSort;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setRadixSort(boolean radixSort) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[6] = radixSort;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API. This method also no
     *             longer does anything.
     */
    @Deprecated
    public void setUnreasonablySlow(boolean unreasonableSlow) {
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    public void setUnreasonableLimit(int unreasonableLimit) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[8] = unreasonableLimit;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setBogoSort(boolean bogoSort) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[7] = bogoSort;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setQuestion(String question) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[9] = question;
    }

    /**
     * @deprecated Please move to the new {@link SortMeta} API.
     */
    @Deprecated
    protected void setQuestion(String question, int defaultAnswer) {
        initDeprecatedMetadataTable();
        deprecatedMetadataTable[9] = question;
        deprecatedMetadataTable[10] = defaultAnswer;
    }

    public static int validateAnswer(int answer) {
        return answer;
    }

    public abstract void runSort(int[] array, int sortLength, int bucketCount) throws Exception; // bucketCount will be
                                                                                                 // zero for
                                                                                                 // comparison-based
                                                                                                 // sorts
}

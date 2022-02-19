package io.github.arrayv.sortdata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class SortInfo {
    private final int id;
    private final Class<? extends Sort> sortClass;
    private final Supplier<? extends Sort> instanceSupplier;
    private final boolean disabled;
    private final boolean bogoSort;
    private final int unreasonableLimit;
    private final String listName;
    private final String runName;
    private final String runAllName;
    private final String category;
    private final boolean slowSort;
    private final boolean radixSort;
    private final boolean bucketSort;

    private SortInfo(int id, SortInfo sort) {
        this.id = id;
        this.sortClass = sort.sortClass;
        this.instanceSupplier = sort.instanceSupplier;
        this.disabled = sort.disabled;
        this.bogoSort = sort.bogoSort;
        this.unreasonableLimit = sort.unreasonableLimit;
        this.listName = sort.listName;
        this.runName = sort.runName;
        this.runAllName = sort.runAllName;
        this.category = sort.category;
        this.slowSort = sort.slowSort;
        this.radixSort = sort.radixSort;
        this.bucketSort = sort.bucketSort;
    }

    public SortInfo(int id, Class<? extends Sort> sortClass) {
        this.id = id;
        this.sortClass = sortClass;
        try {
            this.instanceSupplier = new NewSortInstance(sortClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Sort sort = getFreshInstance();
        this.disabled = !sort.isSortEnabled();
        this.bogoSort = sort.isBogoSort();
        this.unreasonableLimit = sort.getUnreasonableLimit();
        this.listName = sort.getSortListName();
        this.runName = sort.getRunSortName();
        this.runAllName = sort.getRunAllSortsName();
        this.category = sort.getCategory();
        this.slowSort = sort.isUnreasonablySlow();
        this.radixSort = sort.isRadixSort();
        this.bucketSort = sort.usesBuckets();
    }

    public SortInfo(int id, Sort sort) {
        this.id = id;
        this.sortClass = sort.getClass();
        try {
            this.instanceSupplier = new NewSortInstance(sortClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.disabled = !sort.isSortEnabled();
        this.bogoSort = sort.isBogoSort();
        this.unreasonableLimit = sort.getUnreasonableLimit();
        this.listName = sort.getSortListName();
        this.runName = sort.getRunSortName();
        this.runAllName = sort.getRunAllSortsName();
        this.category = sort.getCategory();
        this.slowSort = sort.isUnreasonablySlow();
        this.radixSort = sort.isRadixSort();
        this.bucketSort = sort.usesBuckets();
    }

    public SortInfo(Sort sort) {
        this(-1, sort);
    }

    public int getId() {
        return id;
    }

    public Supplier<? extends Sort> getInstanceSupplier() {
        return instanceSupplier;
    }

    public String getInternalName() {
        return sortClass != null ? sortClass.getName() : null;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isBogoSort() {
        return bogoSort;
    }

    public int getUnreasonableLimit() {
        return unreasonableLimit;
    }

    public String getListName() {
        return listName;
    }

    public String getRunName() {
        return runName;
    }

    public String getRunAllName() {
        return runAllName;
    }

    public String getCategory() {
        return category;
    }

    public boolean isSlowSort() {
        return slowSort;
    }

    public boolean isRadixSort() {
        return radixSort;
    }

    public boolean isBucketSort() {
        return bucketSort;
    }

    public Sort getFreshInstance() {
        return instanceSupplier.get();
    }

    public boolean isFromExtra() {
        return ArrayVisualizer.getInstance().getSortAnalyzer().didSortComeFromExtra(sortClass);
    }

    /**
     * Creates a copy of this info with a new ID
     * @param id The ID for the new instance
     * @return Copied info with new ID
     */
    public SortInfo withId(int id) {
        return new SortInfo(id, this);
    }

    public static String[] getListNames(SortInfo[] sorts) {
        String[] result = new String[sorts.length];
        for (int i = 0; i < sorts.length; i++) {
            result[i] = sorts[i].listName;
        }
        return result;
    }

    public static String[] getCategories(SortInfo[] sorts) {
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < sorts.length; i++) {
            result.add(sorts[i].category);
        }
        String[] resultArray = result.toArray(new String[result.size()]);
        Arrays.sort(resultArray);
        return resultArray;
    }
}

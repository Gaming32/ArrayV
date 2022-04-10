package io.github.arrayv.sortdata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class SortInfo {
    private final int id;
    private final String internalName;
    private final Supplier<? extends Sort> instanceSupplier;
    private final boolean disabled;
    private final int unreasonableLimit;
    private final String listName;
    private final String runName;
    private final String runAllName;
    private final String category;
    private final boolean slowSort;
    private final boolean bogoSort;
    private final boolean radixSort;
    private final boolean bucketSort;
    private final boolean fromExtra;

    private SortInfo(int id, SortInfo sort) {
        this.id = id;
        this.internalName = sort.internalName;
        this.instanceSupplier = sort.instanceSupplier;
        this.disabled = sort.disabled;
        this.unreasonableLimit = sort.unreasonableLimit;
        this.listName = sort.listName;
        this.runName = sort.runName;
        this.runAllName = sort.runAllName;
        this.category = sort.category;
        this.slowSort = sort.slowSort;
        this.bogoSort = sort.bogoSort;
        this.radixSort = sort.radixSort;
        this.bucketSort = sort.bucketSort;
        this.fromExtra = sort.fromExtra;
    }

    public SortInfo(int id, Class<? extends Sort> sortClass) {
        this.id = id;
        this.internalName = sortClass.getSimpleName();
        try {
            this.instanceSupplier = new NewSortInstance(sortClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Sort sort = getFreshInstance();
        this.disabled = !sort.isSortEnabled();
        this.unreasonableLimit = sort.getUnreasonableLimit();
        this.listName = sort.getSortListName();
        this.runName = sort.getRunSortName();
        this.runAllName = sort.getRunAllSortsName();
        this.category = sort.getCategory();
        this.slowSort = sort.isUnreasonablySlow();
        this.bogoSort = sort.isBogoSort();
        this.radixSort = sort.isRadixSort();
        this.bucketSort = sort.usesBuckets();
        this.fromExtra = ArrayVisualizer.getInstance().getSortAnalyzer().didSortComeFromExtra(sortClass);
    }

    public SortInfo(int id, Sort sort) {
        this.id = id;
        this.internalName = sort.getClass().getSimpleName();
        try {
            this.instanceSupplier = new NewSortInstance(sort.getClass());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.disabled = !sort.isSortEnabled();
        this.unreasonableLimit = sort.getUnreasonableLimit();
        this.listName = sort.getSortListName();
        this.runName = sort.getRunSortName();
        this.runAllName = sort.getRunAllSortsName();
        this.category = sort.getCategory();
        this.slowSort = sort.isUnreasonablySlow();
        this.bogoSort = sort.isBogoSort();
        this.radixSort = sort.isRadixSort();
        this.bucketSort = sort.usesBuckets();
        this.fromExtra = ArrayVisualizer.getInstance().getSortAnalyzer().didSortComeFromExtra(sort.getClass());
    }

    private SortInfo(
        int id,
        String internalName,
        Supplier<? extends Sort> instanceSupplier,
        boolean disabled,
        int unreasonableLimit,
        String listName,
        String runName,
        String runAllName,
        String category,
        boolean slowSort,
        boolean bogoSort,
        boolean radixSort,
        boolean bucketSort
    ) {
        this.id = id;
        this.internalName = internalName;
        this.instanceSupplier = instanceSupplier;
        this.disabled = disabled;
        this.unreasonableLimit = unreasonableLimit;
        this.listName = listName;
        this.runName = runName;
        this.runAllName = runAllName;
        this.category = category;
        this.slowSort = slowSort;
        this.bogoSort = bogoSort;
        this.radixSort = radixSort;
        this.bucketSort = bucketSort;
        this.fromExtra = false; // Built sorts cannot come from extra
    }

    public SortInfo(Class<? extends Sort> sort) {
        this(-1, sort);
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
        return internalName;
    }

    public boolean isDisabled() {
        return disabled;
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

    public boolean isBogoSort() {
        return bogoSort;
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
        return fromExtra;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bogoSort ? 1231 : 1237);
        result = prime * result + (bucketSort ? 1231 : 1237);
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + (disabled ? 1231 : 1237);
        result = prime * result + (fromExtra ? 1231 : 1237);
        result = prime * result + id;
        result = prime * result + ((listName == null) ? 0 : listName.hashCode());
        result = prime * result + (radixSort ? 1231 : 1237);
        result = prime * result + ((runAllName == null) ? 0 : runAllName.hashCode());
        result = prime * result + ((runName == null) ? 0 : runName.hashCode());
        result = prime * result + (slowSort ? 1231 : 1237);
        result = prime * result + unreasonableLimit;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SortInfo)) {
            return false;
        }
        SortInfo other = (SortInfo) obj;
        if (bogoSort != other.bogoSort) {
            return false;
        }
        if (bucketSort != other.bucketSort) {
            return false;
        }
        if (category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!category.equals(other.category)) {
            return false;
        }
        if (disabled != other.disabled) {
            return false;
        }
        if (fromExtra != other.fromExtra) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (listName == null) {
            if (other.listName != null) {
                return false;
            }
        } else if (!listName.equals(other.listName)) {
            return false;
        }
        if (radixSort != other.radixSort) {
            return false;
        }
        if (runAllName == null) {
            if (other.runAllName != null) {
                return false;
            }
        } else if (!runAllName.equals(other.runAllName)) {
            return false;
        }
        if (runName == null) {
            if (other.runName != null) {
                return false;
            }
        } else if (!runName.equals(other.runName)) {
            return false;
        }
        if (slowSort != other.slowSort) {
            return false;
        }
        if (unreasonableLimit != other.unreasonableLimit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SortInfo for " + runAllName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int id = -1;
        private String internalName = null;
        private Supplier<? extends Sort> instanceSupplier;
        private boolean disabled = false;
        private int unreasonableLimit = 0;
        private String listName; // Required
        private String runName = null;
        private String runAllName = null;
        private String category; // Required
        private boolean slowSort = false;
        private boolean bogoSort = false;
        private boolean radixSort = false;
        private boolean bucketSort = false;

        private Builder() {
        }

        public SortInfo build() {
            return new SortInfo(
                id,
                internalName,
                Objects.requireNonNull(instanceSupplier, "instanceSupplier"),
                disabled,
                unreasonableLimit,
                Objects.requireNonNull(listName, "listName"),
                runName != null ? runName : (listName + "sort"),
                runAllName != null ? runAllName : (listName + " Sort"),
                Objects.requireNonNull(category, "category"),
                slowSort,
                bogoSort,
                radixSort,
                bucketSort
            );
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder internalName(String internalName) {
            this.internalName = internalName;
            return this;
        }

        public Builder instanceSupplier(Supplier<? extends Sort> instanceSupplier) {
            this.instanceSupplier = instanceSupplier;
            return this;
        }

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Builder unreasonableLimit(int unreasonableLimit) {
            this.unreasonableLimit = unreasonableLimit;
            return this;
        }

        public Builder listName(String listName) {
            this.listName = listName;
            return this;
        }

        public Builder runName(String runName) {
            this.runName = runName;
            return this;
        }

        public Builder runAllName(String runAllName) {
            this.runAllName = runAllName;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder slowSort(boolean slowSort) {
            this.slowSort = slowSort;
            return this;
        }

        public Builder bogoSort(boolean bogoSort) {
            this.bogoSort = bogoSort;
            return this;
        }

        public Builder radixSort(boolean radixSort) {
            this.radixSort = radixSort;
            return this;
        }

        public Builder bucketSort(boolean bucketSort) {
            this.bucketSort = bucketSort;
            return this;
        }
    }
}

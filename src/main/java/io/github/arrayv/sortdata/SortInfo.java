package io.github.arrayv.sortdata;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public final class SortInfo {
    private static final String NAME_MUST_BE_SPECIFIED =
        "name must be specified unless all three of listName, showcaseName, and runName are specified";

    private final int id;
    private final String internalName;
    private final Supplier<? extends Sort> instanceSupplier;
    private final boolean disabled;
    private final int unreasonableLimit;
    private final String listName;
    private final String runName;
    private final String runAllName;
    private final String category;
    private final boolean bogoSort;
    private final boolean radixSort;
    private final boolean bucketSort;
    private final String question;
    private final int defaultAnswer;
    private final IntUnaryOperator answerValidator;
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
        this.bogoSort = sort.bogoSort;
        this.radixSort = sort.radixSort;
        this.bucketSort = sort.bucketSort;
        this.question = sort.question;
        this.defaultAnswer = sort.defaultAnswer;
        this.answerValidator = sort.answerValidator;
        this.fromExtra = sort.fromExtra;
    }

    @SuppressWarnings("deprecation")
    public SortInfo(int id, Class<? extends Sort> sortClass) {
        this.id = id;
        this.internalName = sortClass.getSimpleName();
        try {
            this.instanceSupplier = new NewSortInstance(sortClass);
            this.answerValidator = new MethodAnswerValidator(sortClass);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new Error(e);
        }
        SortMeta metaAnnotation = sortClass.getAnnotation(SortMeta.class);
        if (metaAnnotation == null) {
            Sort sort = getFreshInstance();
            this.disabled = !sort.isSortEnabled();
            this.unreasonableLimit = sort.getUnreasonableLimit();
            this.listName = sort.getSortListName();
            this.runName = sort.getRunSortName();
            this.runAllName = sort.getRunAllSortsName();
            this.category = sort.getCategory();
            this.bogoSort = sort.isBogoSort();
            this.radixSort = sort.isRadixSort();
            this.bucketSort = sort.usesBuckets();
            this.question = sort.getQuestion();
            this.defaultAnswer = sort.getDefaultAnswer();
        } else {
            String name = normalizeName(metaAnnotation);
            this.disabled = metaAnnotation.disabled();
            this.unreasonableLimit = metaAnnotation.unreasonableLimit();
            this.listName = metaAnnotation.listName().isEmpty()
                ? requireName(name)
                : metaAnnotation.listName();
            this.runName = metaAnnotation.runName().isEmpty()
                ? requireName(name) + "sort"
                : metaAnnotation.runName();
            this.runAllName = metaAnnotation.showcaseName().isEmpty()
                ? requireName(name) + " Sort"
                : metaAnnotation.showcaseName();
            this.category = metaAnnotation.category().isEmpty() ? findSortCategory(sortClass) : metaAnnotation.category();
            this.bogoSort = metaAnnotation.bogoSort();
            this.radixSort = metaAnnotation.radixSort();
            this.bucketSort = metaAnnotation.bucketSort();
            this.question = metaAnnotation.question().isEmpty() ? null : metaAnnotation.question();
            this.defaultAnswer = metaAnnotation.defaultAnswer();
        }
        this.fromExtra = ArrayVisualizer.getInstance().getSortAnalyzer().didSortComeFromExtra(sortClass);
    }

    @SuppressWarnings("deprecation")
    public SortInfo(int id, Sort sort) {
        this.id = id;
        this.internalName = sort.getClass().getSimpleName();
        try {
            this.instanceSupplier = new NewSortInstance(sort.getClass());
            this.answerValidator = new MethodAnswerValidator(sort.getClass());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new Error(e);
        }
        SortMeta metaAnnotation = sort.getClass().getAnnotation(SortMeta.class);
        if (metaAnnotation == null) {
            this.disabled = !sort.isSortEnabled();
            this.unreasonableLimit = sort.getUnreasonableLimit();
            this.listName = sort.getSortListName();
            this.runName = sort.getRunSortName();
            this.runAllName = sort.getRunAllSortsName();
            this.category = sort.getCategory();
            this.bogoSort = sort.isBogoSort();
            this.radixSort = sort.isRadixSort();
            this.bucketSort = sort.usesBuckets();
            this.question = sort.getQuestion();
            this.defaultAnswer = sort.getDefaultAnswer();
        } else {
            String name = normalizeName(metaAnnotation);
            this.disabled = metaAnnotation.disabled();
            this.unreasonableLimit = metaAnnotation.unreasonableLimit();
            this.listName = metaAnnotation.listName().isEmpty()
                ? requireName(name)
                : metaAnnotation.listName();
            this.runName = metaAnnotation.runName().isEmpty()
                ? requireName(name) + "sort"
                : metaAnnotation.runName();
            this.runAllName = metaAnnotation.showcaseName().isEmpty()
                ? requireName(name) + " Sort"
                : metaAnnotation.showcaseName();
            this.category = metaAnnotation.category().isEmpty() ? findSortCategory(sort.getClass()) : metaAnnotation.category();
            this.bogoSort = metaAnnotation.bogoSort();
            this.radixSort = metaAnnotation.radixSort();
            this.bucketSort = metaAnnotation.bucketSort();
            this.question = metaAnnotation.question().isEmpty() ? null : metaAnnotation.question();
            this.defaultAnswer = metaAnnotation.defaultAnswer();
        }
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
        boolean bucketSort,
        String question,
        int defaultAnswer,
        IntUnaryOperator answerValidator
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
        this.bogoSort = bogoSort;
        this.radixSort = radixSort;
        this.bucketSort = bucketSort;
        this.question = question;
        this.defaultAnswer = defaultAnswer;
        this.answerValidator = answerValidator;
        this.fromExtra = false; // Built sorts cannot come from extra
    }

    public SortInfo(Class<? extends Sort> sort) {
        this(-1, sort);
    }

    public SortInfo(Sort sort) {
        this(-1, sort);
    }

    private static String findSortCategory(Class<? extends Sort> sortClass) {
        Package checkPackage = sortClass.getPackage();
        if (checkPackage != null) {
            SortPackageMeta packageMetaAnnotation = checkPackage.getAnnotation(SortPackageMeta.class);
            if (packageMetaAnnotation != null) {
                return packageMetaAnnotation.category();
            }
        }
        throw new NullPointerException(
            "Sort " + sortClass.getSimpleName() + " does not declare a category, and neither do any of its packages"
        );
    }

    private static String requireName(String name) {
        return Objects.requireNonNull(name, NAME_MUST_BE_SPECIFIED);
    }

    private static String normalizeName(SortMeta meta) {
        String name = meta.name();
        if (name.endsWith(" Sort")) {
            return name.substring(0, name.length() - 5);
        }
        if (name.endsWith("sort")) {
            return name.substring(0, name.length() - 4);
        }
        // Cause an NPE if name isn't specified, and all three required names also aren't
        return name.isEmpty() ? null : name;
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

    public boolean hasUnreasonableLimit() {
        return unreasonableLimit > 0;
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

    public String getQuestion() {
        return question;
    }

    public int getDefaultAnswer() {
        return defaultAnswer;
    }

    public IntUnaryOperator getAnswerValidator() {
        return answerValidator;
    }

    public Sort getFreshInstance() {
        return instanceSupplier.get();
    }

    public int validateAnswer(int answer) {
        return answerValidator.applyAsInt(answer);
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
        for (SortInfo sort : sorts) {
            result.add(sort.category);
        }
        String[] resultArray = result.toArray(new String[0]);
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
        result = prime * result + defaultAnswer;
        result = prime * result + (disabled ? 1231 : 1237);
        result = prime * result + (fromExtra ? 1231 : 1237);
        result = prime * result + id;
        result = prime * result + ((listName == null) ? 0 : listName.hashCode());
        result = prime * result + ((question == null) ? 0 : question.hashCode());
        result = prime * result + (radixSort ? 1231 : 1237);
        result = prime * result + ((runAllName == null) ? 0 : runAllName.hashCode());
        result = prime * result + ((runName == null) ? 0 : runName.hashCode());
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
        if (defaultAnswer != other.defaultAnswer) {
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
        if (question == null) {
            if (other.question != null) {
                return false;
            }
        } else if (!question.equals(other.question)) {
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
        return unreasonableLimit == other.unreasonableLimit;
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
        private String question = null;
        private int defaultAnswer = 0;
        private IntUnaryOperator answerValidator = IntUnaryOperator.identity();

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
                bucketSort,
                question,
                defaultAnswer,
                answerValidator
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

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder defaultAnswer(int defaultAnswer) {
            this.defaultAnswer = defaultAnswer;
            return this;
        }

        public Builder answerValidator(IntUnaryOperator answerValidator) {
            this.answerValidator = Objects.requireNonNull(answerValidator, "answerValidator");
            return this;
        }
    }
}

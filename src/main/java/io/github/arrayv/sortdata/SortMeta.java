package io.github.arrayv.sortdata;

public @interface SortMeta {
    /**
     * The sort's name. This is generally of the form {@code "Something Sort"}, although the {@code " Sort"} is
     * optional.
     * @return The sort's annotated name.
     */
    String name();

    /**
     * The sort's cateogry.
     * @return The sort's category.
     */
    String category();

    /**
     * Explicit sort list name. This is generated from {@link #name()} by default.
     * @return The sort's explicit list name, if it has one. {@code ""} otherwise.
     */
    String listName() default "";

    /**
     * Explicit Run Sort name. This is generated from {@link #name()} by default.
     * @return The sort's explicit Run Sort name, if it has one. {@code ""} otherwise.
     */
    String runName() default "";

    /**
     * Explicit Showcase Sorts name (and scripting name). This is generated from {@link #name()} by default.
     * @return The sort's explicit Showcase Sorts name, if it has one. {@code ""} otherwise.
     */
    String showcaseName() default "";

    /**
     * Whether this sort is disabled. Disabled sorts won't be loaded.
     * @return Whether this sort is disabled.
     */
    boolean disabled() default false;

    /**
     * This sort's unreasonable limit. If the sort is run with lengths higher than this, a warning is displayed.
     * @return This sort's unreasonable limit.
     */
    int unreasonableLimit() default 0;

    /**
     * Whether to treat this sort as slow in Showcase Sorts and in sort scripts.
     * @return Whether to treat this sort as slow.
     */
    boolean slowSort() default false;

    /**
     * Whether this sort is a bogo sort (i.e. it has "bogo" in its name and it's non-deterministic).
     * @return Whether this sort is a bogo sort.
     */
    boolean bogoSort() default false;

    /**
     * Whether this sort is a Radix Sort.
     * @return Whether this sort is a Radix Sort.
     */
    boolean radixSort() default false;

    /**
     * Whether this sort uses buckets.
     * @return Whether this sort uses buckets.
     */
    boolean bucketSort() default false;
}

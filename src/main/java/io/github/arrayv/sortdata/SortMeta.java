package io.github.arrayv.sortdata;

import java.lang.annotation.*;

/**
 * Annotation to specify the sorting algorithm's metadata. This should only be applied to subclasses of
 * {@link io.github.arrayv.sorts.templates.Sort Sort}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SortMeta {
    /**
     * The sort's name. This is generally of the form {@code "Something"} or {@code "Something Sort"}. If not
     * specified, all three of {@link #listName()}, {@link #runName()}, and {@link #showcaseName()} must be specified.
     * @return The sort's annotated name.
     */
    String name() default "";

    /**
     * The sort's category. This may be specified on a package-level in package-info.java. Specifying it here will
     * override it for the package.
     * @return The sort's category, or {@code ""} if you should look at the package level.
     */
    String category() default "";

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
     * Whether this sort is a bogo sort (i.e. it has "bogo" in its name, and it's non-deterministic).
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

    /**
     * A question to ask the user when they choose this sort. You can perform response validation by creating a method
     * that is {@code public static int validateAnswer(int answer)}.
     * @return The question to ask the user, or {@code ""} if there isn't one.
     */
    String question() default "";

    /**
     * The default response to use for {@link #question()}. This is used when the user pressed "Use default". This
     * value is ignored if there is no question. This value is <i>not</i> passed through {@code validatorAnswer}.
     * @return The default answer response.
     */
    int defaultAnswer() default 0;
}

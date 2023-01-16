package io.github.arrayv.sortdata;

import java.lang.annotation.*;

/**
 * Annotation to specify metadata about a package containing sorting algorithms.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface SortPackageMeta {
    String category();
}

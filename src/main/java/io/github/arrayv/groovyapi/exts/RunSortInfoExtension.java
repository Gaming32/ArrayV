package io.github.arrayv.groovyapi.exts;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import io.github.arrayv.groovyapi.RunSortBuilder;

/**
 * Extension for creating {@link Map.Entry}s to configure {@link RunSortBuilder} with
 * <br>
 * See {@link RunSortBuilder} for more info on these keys
 */
public final class RunSortInfoExtension {
    private RunSortInfoExtension() {
    }

    /**
     * Create a {@code numbers} key
     * @param self The number of numbers
     * @return A {@link Map.Entry} for {@code numbers}
     */
    public static Map.Entry<String, Object> getNumbers(Integer self) {
        return create("numbers", self);
    }

    /**
     * Create a {@code buckets} key
     * @param self The extra value to pass to the sort
     * @return A {@link Map.Entry} for {@code buckets}
     */
    public static Map.Entry<String, Object> getBuckets(Integer self) {
        return create("buckets", self);
    }

    /**
     * Create a {@code speed} key
     * @param self The speed multiplier for the visual
     * @return A {@link Map.Entry} for {@code speed}
     */
    public static Map.Entry<String, Object> getSpeed(Number self) {
        return create("speed", self);
    }

    private static Map.Entry<String, Object> create(String key, Object self) {
        return new SimpleImmutableEntry<>(key, self);
    }
}

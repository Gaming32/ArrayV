package io.github.arrayv.groovyapi.exts;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

public final class RunSortInfoExtension {
    private RunSortInfoExtension() {
    }

    public static Map.Entry<String, Object> getNumbers(Integer self) {
        return new SimpleImmutableEntry<>("numbers", self);
    }

    public static Map.Entry<String, Object> getBuckets(Integer self) {
        return new SimpleImmutableEntry<>("buckets", self);
    }

    public static Map.Entry<String, Object> getDelay(Number self) {
        return new SimpleImmutableEntry<>("delay", self.doubleValue());
    }
}

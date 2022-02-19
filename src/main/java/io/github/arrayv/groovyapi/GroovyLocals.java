package io.github.arrayv.groovyapi;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.sortdata.SortNameType;

public final class GroovyLocals {
    // No instancing!
    private GroovyLocals() {
    }

    public static ArrayVisualizer getArrayv() {
        return ArrayVisualizer.getInstance();
    }

    public static SortInfo getSort(String internalName) {
        return getSort(internalName, SortNameType.INTERNAL_NAME);
    }

    public static SortInfo getSort(String name, SortNameType nameType) {
        return ArrayVisualizer.getInstance().getSortAnalyzer().getSortByName(nameType, name);
    }
}

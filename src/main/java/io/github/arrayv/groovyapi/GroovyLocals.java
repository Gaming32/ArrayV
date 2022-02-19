package io.github.arrayv.groovyapi;

import io.github.arrayv.main.ArrayVisualizer;

public final class GroovyLocals {
    // No instancing!
    private GroovyLocals() {
    }

    public static ArrayVisualizer getArrayv() {
        return ArrayVisualizer.getInstance();
    }
}

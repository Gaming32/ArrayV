package io.github.arrayv.groovyapi;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class RunGroupContext {
    public static final ThreadLocal<RunGroupContext> CONTEXT = new ThreadLocal<>();

    private final int sortCount;
    private final int startingLength;
    final Set<Runnable> closers;
    private int sortNumber;

    public RunGroupContext(int sortCount, int startingLength) {
        this.sortCount = sortCount;
        this.startingLength = startingLength;
        this.closers = Collections.newSetFromMap(new IdentityHashMap<>());
        this.sortNumber = 0;
    }

    public int getSortCount() {
        return sortCount;
    }

    public int getStartingLength() {
        return startingLength;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public int nextSort() {
        return ++sortNumber;
    }
}

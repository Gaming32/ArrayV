package io.github.arrayv.groovyapi;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * This class is used for holding run group thread local information.
 */
public final class RunGroupContext {
    /**
     * The {@link ThreadLocal} object used for getting and setting the thread RunGroupContext
     */
    public static final ThreadLocal<RunGroupContext> CONTEXT = new ThreadLocal<>();

    private final int sortCount;
    private final int startingLength;
    final Set<Runnable> closers;
    private int sortNumber;

    /**
     * Creates a RunGroupContext with the specified number of sorts and a base length
     * @param sortCount See {@link #getSortCount}
     * @param startingLength See {@link #getStartingLength}
     */
    public RunGroupContext(int sortCount, int startingLength) {
        this.sortCount = sortCount;
        this.startingLength = startingLength;
        this.closers = Collections.newSetFromMap(new IdentityHashMap<>());
        this.sortNumber = 0;
    }

    /**
     * The number of sorts in the current run group
     * @return The number of sorts in the current run group
     */
    public int getSortCount() {
        return sortCount;
    }

    /**
     * The base length for the current run group
     * @return The base length for the current run group
     */
    public int getStartingLength() {
        return startingLength;
    }

    /**
     * The current sorting algorithm index
     * @return The current sorting algorithm index
     */
    public int getSortNumber() {
        return sortNumber;
    }

    /**
     * Increments and returns the new sorting algorithm index
     * @return The newly incremented indexed
     * @see #getSortNumber
     */
    public int nextSort() {
        return ++sortNumber;
    }
}

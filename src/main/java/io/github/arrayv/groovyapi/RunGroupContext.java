package io.github.arrayv.groovyapi;

public final class RunGroupContext {
    public static final ThreadLocal<RunGroupContext> CONTEXT = new ThreadLocal<>();

    private final int sortCount;
    private final int startingLength;
    private int sortNumber;

    public RunGroupContext(int sortCount, int startingLength) {
        this.sortCount = sortCount;
        this.startingLength = startingLength;
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

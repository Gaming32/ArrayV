package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.ShellSorting;

// Shell sort variant retrieved from:
// https://www.cs.princeton.edu/~rs/talks/shellsort.ps
@SortMeta(name = "Shell")
public final class ShellSort extends ShellSorting {
    public ShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void finishQuickShell(int[] array, int currentLen) {
        this.quickShellSort(array, 0, currentLen);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.shellSort(array, currentLength);
    }
}

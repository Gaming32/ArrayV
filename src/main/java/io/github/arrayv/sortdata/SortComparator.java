package io.github.arrayv.sortdata;

import java.util.Comparator;

public final class SortComparator implements Comparator<SortInfo> {
    public SortComparator() {}

    @Override
    public int compare(SortInfo left, SortInfo right) {
        return left.getListName().compareTo(right.getListName());
    }
}

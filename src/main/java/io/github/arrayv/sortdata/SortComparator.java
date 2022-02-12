package io.github.arrayv.sortdata;

import java.util.Comparator;

final public class SortComparator implements Comparator<SortInfo> {
    public SortComparator() {}

    @Override
    public int compare(SortInfo left, SortInfo right) {
        return left.getListName().compareTo(right.getListName());
    }
}

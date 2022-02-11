package sorts.templates;

import java.util.Comparator;

import main.SortAnalyzer.SortInfo;

final public class SortComparator implements Comparator<SortInfo> {
    public SortComparator() {}

    @Override
    public int compare(SortInfo left, SortInfo right) {
        return left.getListName().compareTo(right.getListName());
    }
}
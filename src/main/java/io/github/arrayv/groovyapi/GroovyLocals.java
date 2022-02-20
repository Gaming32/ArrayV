package io.github.arrayv.groovyapi;

import org.codehaus.groovy.runtime.MethodClosure;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.sortdata.SortNameType;
import io.github.arrayv.sorts.templates.Sort;

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
        return getArrayv().getSortAnalyzer().getSortByName(nameType, name);
    }

    public static SortInfo newSort(
        @ClosureParams(
            value = SimpleType.class,
            options = {"int[]"}
        ) Closure<?> sort,
        @DelegatesTo(SortInfo.Builder.class) Closure<?> metadata
    ) {
        SortInfo.Builder builder = SortInfo.builder();

        // Initialize metadata
        metadata = metadata.rehydrate(builder, metadata, metadata);
        metadata.setResolveStrategy(Closure.DELEGATE_FIRST);
        metadata.call();

        if (sort instanceof MethodClosure) {
            // The method has a name!
            builder.internalName(((MethodClosure)sort).getMethod());
        }

        SortInfo[] sortInfo = new SortInfo[1];

        class GroovySort extends Sort {
            public GroovySort() {
                super(ArrayVisualizer.getInstance());

                // Support the (to be deprecated) method of settings sort metadata in the class
                this.setSortListName(sortInfo[0].getListName());
                this.setRunAllSortsName(sortInfo[0].getRunAllName());
                this.setRunSortName(sortInfo[0].getRunName());
                this.setCategory(sortInfo[0].getCategory());
                this.setBucketSort(sortInfo[0].isBucketSort());
                this.setRadixSort(sortInfo[0].isRadixSort());
                this.setUnreasonablySlow(sortInfo[0].isSlowSort());
                this.setUnreasonableLimit(sortInfo[0].getUnreasonableLimit());
                this.setBogoSort(sortInfo[0].isBogoSort());
            }

            @Override
            public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
                sort.call(array);
            }
        }
        builder.instanceSupplier(GroovySort::new);

        return registerSort(sortInfo[0] = builder.build());
    }

    public static SortInfo registerSort(SortInfo sort) {
        sort = getArrayv().getSortAnalyzer().insortSort(sort);
        getArrayv().refreshSorts();
        return sort;
    }

    public static SortInfo registerSort(Sort sort) {
        return registerSort(new SortInfo(sort));
    }

    public static SortInfo registerSort(Class<? extends Sort> sort) {
        return registerSort(new SortInfo(sort));
    }

    public static RunSortBuilder run(SortInfo sort) {
        return new RunSortBuilder(sort);
    }
}

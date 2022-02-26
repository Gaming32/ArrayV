package io.github.arrayv.groovyapi;

import java.util.Map;

import org.codehaus.groovy.runtime.MethodClosure;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import io.github.arrayv.groovyapi.ScriptManager.ScriptThread;
import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.sortdata.SortNameType;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Sounds;

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

        final class GroovySort extends Sort {
            // @checkstyle:off RedundantModifierCheck
            // This needs to be public so it can be accessed from reflection/java.lang.invoke
            public GroovySort() {
            // @checkstyle:on RedundantModifierCheck
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
        return new RunSortBuilder(null, sort);
    }

    public static RunSortBuilder run(Map<String, Object> options, SortInfo sort) {
        return new RunSortBuilder(options, sort);
    }

    public static String getCategory() {
        return getArrayv().getCategory();
    }

    public static void setCategory(String category) {
        getArrayv().setCategory(category);
    }

    public static void runGroup(Integer sortCount, Runnable run) {
        try {
            runGroupInThread(sortCount, run).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Thread runGroupInThread(Integer sortCount, Runnable run) {
        final ArrayVisualizer arrayVisualizer = getArrayv();
        final ArrayManager arrayManager = arrayVisualizer.getArrayManager();
        final Sounds Sounds = arrayVisualizer.getSounds();
        final String threadName = (Thread.currentThread() instanceof ScriptThread)
            ? ("SortGroup-" + ((ScriptThread)Thread.currentThread()).getScript().getClass().getName())
            : "SortGroup";

        Sounds.toggleSound(true);
        Thread sortingThread = new Thread(() -> {
            RunGroupContext.CONTEXT.set(new RunGroupContext(sortCount, arrayVisualizer.getCurrentLength()));
            try {
                arrayManager.toggleMutableLength(false);

                run.run();

                arrayVisualizer.setCategory("Run " + arrayVisualizer.getCategory());
                arrayVisualizer.setHeading("Done");
                arrayVisualizer.updateNow();

                arrayManager.toggleMutableLength(true);
            } catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
            Sounds.toggleSound(false);
            arrayVisualizer.setSortingThread(null);
        }, threadName);

        arrayVisualizer.setSortingThread(sortingThread);
        arrayVisualizer.runSortingThread();
        return sortingThread;
    }
}

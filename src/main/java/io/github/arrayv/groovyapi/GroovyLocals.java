package io.github.arrayv.groovyapi;

import java.util.Map;

import org.codehaus.groovy.runtime.MethodClosure;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.FromAbstractTypeMethods;
import io.github.arrayv.groovyapi.ScriptManager.ScriptThread;
import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.prompts.SortPrompt;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.sortdata.SortNameType;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Sounds;

/**
 * This class defines methods and properties directly accessible from within Groovy scripts.
 * Methods and properties in this class can be directly named without prefixing them with {@code GroovyLocals.}.
 */
public final class GroovyLocals {
    // No instancing!
    private GroovyLocals() {
    }

    /**
     * <p>Property for the main {@link ArrayVisualizer} instance.</p>
     * Intended to be used like this (example):
     * <pre>
     * println arrayv.sortAnalyzer
     * </pre>
     * @return The main {@link ArrayVisualizer} instance
     */
    public static ArrayVisualizer getArrayv() {
        return ArrayVisualizer.getInstance();
    }

    /**
     * Get a sort by its internal name, which is usually the name of the sort class.
     * @param internalName The internal name to find the sort by
     * @return The sort with this internal name or {@code null} if no sort with the given internal name was found
     */
    public static SortInfo getSort(String internalName) {
        return getSort(internalName, SortNameType.INTERNAL_NAME);
    }

    /**
     * Get a sort by name
     * @param name The name of the sort
     * @param nameType The type of name to search by (such as list name, run name, etc.).
     * See {@link SortNameType} for more details
     * @return The sort with this name or {@code null} if no sort with the given name and name type was found
     */
    public static SortInfo getSort(String name, SortNameType nameType) {
        return getArrayv().getSortAnalyzer().getSortByName(nameType, name);
    }

    /**
     * <p>Creates (and adds) a new sorting algorithm to this ArrayV instance.</p>
     * Here's an example:
     * <pre>
     * def sortFn(array, length) {
     *     // Sorting algorithm code here
     * }
     *
     * newSort(this::sortFn) {
     *     listName "My Custom Sort"
     *     category "Examples"
     *     unreasonableLimit 1024
     * }
     * </pre>
     * @param sort The sort method/function/closure to add.
     * This closure can follow any of the signatures listed in {@link SortFunctionSignatures}
     * @param metadata The closure used to define metadata.
     * See above for an example, and {@link SortInfo.Builder} for the list of metadata methods you can use.
     * @return The {@link SortInfo} object associated with the newly created algorithm
     */
    public static SortInfo newSort(
        @ClosureParams(
            value = FromAbstractTypeMethods.class,
            options = {"io.github.arrayv.groovyapi.GroovyLocals$SortFunctionSignatures"}
        ) Closure<?> sort,
        @DelegatesTo(SortInfo.Builder.class) Closure<?> metadata
    ) {
        switch (sort.getMaximumNumberOfParameters()) {
            case 2:
            case 3:
                break;
            default:
                throw new IllegalArgumentException(
                    "Illegal number of arguments for Groovy sort function: "
                        + sort.getMaximumNumberOfParameters()
                        + ". Must be one of: 2, 3"
                );
        }

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
                switch (sort.getMaximumNumberOfParameters()) {
                    case 2:
                        sort.call(array, sortLength);
                        break;
                    case 3:
                        sort.call(array, sortLength, bucketCount);
                        break;
                }
            }
        }
        builder.instanceSupplier(GroovySort::new);

        return registerSort(sortInfo[0] = builder.build());
    }

    /**
     * Registers a sort with the sort list
     * @param sort The sort to register
     * @return The {@code sort} parameter, but likely with a different id
     */
    public static SortInfo registerSort(SortInfo sort) {
        sort = getArrayv().getSortAnalyzer().insortSort(sort);
        getArrayv().refreshSorts();
        return sort;
    }

    /**
     * Registers a sort with the sort list
     * @param sort The sort instance to convert to a {@link SortInfo}
     * @return The registered {@link SortInfo} object
     */
    public static SortInfo registerSort(Sort sort) {
        return registerSort(new SortInfo(sort));
    }

    /**
     * Registers a sort with the sort list
     * @param sort The sort class to convert to a {@link SortInfo}
     * @return The registered {@link SortInfo} object
     */
    public static SortInfo registerSort(Class<? extends Sort> sort) {
        return registerSort(new SortInfo(sort));
    }

    /**
     * Prepares to run a sorting algorithm
     * @param sort The sorting algorithm to run
     * @return A {@link RunSortBuilder} for setting up sort running parameters
     */
    public static RunSortBuilder run(SortInfo sort) {
        return new RunSortBuilder(null, sort);
    }

    /**
     * Effectively an alias for {@link #run(SortInfo)}, but designed around a special
     * case dealing with TimSort and DualPivotQuickSort, as Groovy loads the java.util classes instead
     * @param sort The class to coerce to a {@link SortInfo} using {@code getSort(sort.getSimpleName())}
     * @return A {@link RunSortBuilder} for setting up sort running parameters
     */
    public static RunSortBuilder run(Class<?> sort) {
        return new RunSortBuilder(null, getSort(sort.getSimpleName()));
    }

    /**
     * Runs a sort, but adds the ability to pass Groovy map arguments,
     * e.g.: {@code run(BubbleSort, numbers: 2048, speed: 0.75)}
     * @param options Groovy map arguments. See {@link RunSortBuilder} for valid keys
     * @param sort The sorting algorithm to run
     * @return A {@link RunSortBuilder} for setting up sort running parameters
     */
    public static RunSortBuilder run(Map<String, Object> options, SortInfo sort) {
        return new RunSortBuilder(options, sort);
    }

    /**
     * Combination of {@link #run(Class)} and {@link #run(Map, SortInfo)}
     * @param options
     * @param sort
     * @return
     */
    public static RunSortBuilder run(Map<String, Object> options, Class<?> sort) {
        return new RunSortBuilder(options, getSort(sort.getSimpleName()));
    }

    /**
     * Get the current category display
     * @return The current category
     */
    public static String getCategory() {
        return getArrayv().getCategory();
    }

    /**
     * Set the current category display
     * @param category The category to display
     */
    public static void setCategory(String category) {
        getArrayv().setCategory(category);
    }

    /**
     * Setus up a run group. Run groups run in a separate thread and keep track of the number of sorts remaining.
     * This particular method simply joins on the new thread. If you wish for finer-grained control, see {@link #runGroupInThread(Integer, Runnable)}.
     * @param sortCount The total number of sorts in this group
     * @param run A closure around the code to run. Generally built of mostly {@link #run} calls
     */
    public static void runGroup(Integer sortCount, Runnable run) {
        try {
            runGroupInThread(sortCount, run).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setus up a run group. Run groups run in a separate thread and keep track of the number of sorts remaining.
     * This is identical to {@link #runGroup}, except that it returns the new thread instead of simply joining on it.
     * @param sortCount The total number of sorts in this group
     * @param run A closure around the code to run. Generally built of mostly {@link #run} calls
     * @return The newly created group thread
     */
    public static Thread runGroupInThread(Integer sortCount, Runnable run) {
        return runGroupInThread(sortCount, run, false);
    }

    /**
     * Similar to {@link #runGroupInThread(Integer, Runnable)}, except that it has an extra {@code isRunAll parameter}.
     * This method is generally intended only to be used internally by {@link SortPrompt#jButton1ActionPerformed}.
     * @param sortCount The total number of sorts in this group
     * @param run A closure around the code to run. Generally built of mostly {@link #run} calls
     * @param isRunAll If this is {@code true}, the group will behave as if it was Showcase Sorts.
     * @return The newly created group thread
     */
    public static Thread runGroupInThread(Integer sortCount, Runnable run, boolean isRunAll) {
        final ArrayVisualizer arrayVisualizer = getArrayv();
        final ArrayManager arrayManager = arrayVisualizer.getArrayManager();
        final Sounds Sounds = arrayVisualizer.getSounds();
        final String threadName =
            (isRunAll ? "RunAll" : "SortGroup") + (
                (Thread.currentThread() instanceof ScriptThread)
                    ? ("-" + ((ScriptThread)Thread.currentThread()).getScript().getClass().getName())
                    : ""
            );

        Sounds.toggleSound(true);
        Thread sortingThread = new Thread(() -> {
            RunGroupContext.CONTEXT.set(new RunGroupContext(sortCount, arrayVisualizer.getCurrentLength()));
            try {
                arrayManager.toggleMutableLength(false);

                run.run();

                if (isRunAll) {
                    arrayVisualizer.setCategory("Showcase Sorts");
                    arrayVisualizer.setHeading("Finished!!");
                } else {
                    arrayVisualizer.setCategory("Run " + arrayVisualizer.getCategory());
                    arrayVisualizer.setHeading("Done");
                }
                arrayVisualizer.updateNow();

                arrayManager.toggleMutableLength(true);
            } catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
            Sounds.toggleSound(false);

            arrayVisualizer.setSortingThread(null);
            RunGroupContext rgc;
            if ((rgc = RunGroupContext.CONTEXT.get()) != null) {
                ScriptThread.runClosers(rgc.closers);
            }
        }, threadName);

        arrayVisualizer.setSortingThread(sortingThread);
        arrayVisualizer.runSortingThread();
        return sortingThread;
    }

    /**
     * Registers an event handler and returns the handler object.
     * See {@link ArrayVEventHandler} for more details.
     * @param eventType The type of the event to handle
     * @param cb The callback to run for the event
     * @return The registered event handler
     */
    public static ArrayVEventHandler registerEventHandler(ArrayVEventHandler.EventType eventType, Runnable cb) {
        ArrayVEventHandler handler = new ArrayVEventHandler(eventType, cb);
        handler.register();
        return handler;
    }

    /**
     * <p>Register a closer.</p>
     *
     * <p>Ok, so what is a closer? A closer is ArrayV's Groovy API's equivalent of finalizers. Closers
     * fix many of the flaws of finalizers. Closers are <i>guaranteed</i> to be called when the thread
     * exits, whereas finalizers may <i>never</i> be called. Closers are also guaranteed to be called
     * in the thread they are registered in, whereas finalizers may be called from any any thread.
     * Furthermore, finalizers have been deprecated and are scheduled to be removed at some point in
     * the future.</p>
     * @param closer The closer to register
     */
    public static void registerCloser(Runnable closer) {
        Thread cThread = Thread.currentThread();
        RunGroupContext rgc = RunGroupContext.CONTEXT.get();
        boolean forCThread = cThread instanceof ScriptThread;
        boolean forRgc = rgc != null;
        if (forCThread && forRgc) {
            // Could be called in multiple places
            final boolean[] hasBeenRun = {false};
            final Runnable originalCloser = closer;
            closer = () -> {
                if (hasBeenRun[0]) return;
                try {
                    originalCloser.run();
                } finally {
                    hasBeenRun[0] = true;
                }
            };
        }
        if (forCThread) {
            ((ScriptThread)cThread).closers.add(closer);
        }
        if (forRgc) {
            rgc.closers.add(closer);
        }
        if (!forCThread && !forRgc) {
            throw new IllegalArgumentException("Can't register a closer here");
        }
    }

    /**
     * This abstract class declares which signatures the closures passed into {@link GroovyLocals#newSort} can use.
     */
    public abstract static class SortFunctionSignatures {
        /**
         * This signature takes all the information that is standard with {@link Sort#runSort}
         * @param array The main array, which is sized at the maximum arrays size
         * @param length The current length of the array.
         * Accessing any indices outside of the range [0, length) is considered illegal.
         * @param buckets Any extra parameter passed to the sort
         */
        public abstract void standardOptions(int[] array, int length, int buckets);

        /**
         * Same as {@link #standardOptions}, but takes no buckets argument.
         * This is primarily useful if your sort takes no additional user input.
         * @param array
         * @param length
         */
        public abstract void standardOptionsNoBuckets(int[] array, int length);
    }
}

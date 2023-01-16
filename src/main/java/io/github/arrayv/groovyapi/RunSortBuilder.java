package io.github.arrayv.groovyapi;

import io.github.arrayv.frames.ArrayFrame;
import io.github.arrayv.groovyapi.ScriptManager.ScriptThread;
import io.github.arrayv.groovyapi.exts.RunSortInfoExtension;
import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.StopSort;
import io.github.arrayv.utils.Timer;

import javax.swing.*;
import java.util.*;

/**
 * <p>This class is used for running sorting algorithms. You can construct an instance using the
 * {@link GroovyLocals#run(SortInfo)} method, and other {@code run} methods.</p>
 *
 * <p>This class is configured using a map of options. This map can contain a strict set of keys
 * Here is the list of allowed keys and their types:</p>
 * <ul>
 *   <li>{@code numbers} ({@code Integer}) &mdash; The number of values to sort</li>
 *   <li>{@code buckets} ({@code Integer}) &mdash; An extra value to pass to the sorting
 *       algorithm</li>
 *   <li>{@code speed} ({@code Number}) &mdash; The time multiplier of the visual</li>
 * </ul>
 * <p>{@link Map.Entry} values to put into the option map can be obtained using {@link RunSortInfoExtension}.</p>
 *
 * @see GroovyLocals#run(SortInfo)
 * @see RunSortInfoExtension
 */
public final class RunSortBuilder {
    private static final Set<String> ALLOWED_KEYS = new HashSet<>(Arrays.asList(
        "numbers",
        "buckets",
        "speed"
    ));

    private final SortInfo sort;
    private final Map<String, Object> opts;
    private volatile boolean closed;
    private final Runnable closer = () -> {
        if (closed) return;
        removeClosers();
        final String message = this + " never run from Groovy script";
        System.err.println("WARNING: " + message);
        JOptionPane.showMessageDialog(
            null,
            message,
            RunSortBuilder.class.getName(),
            JOptionPane.WARNING_MESSAGE
        );
    };
    private Map<String, Object> unmodifiableOpts = null;

    RunSortBuilder(Map<String, Object> options, SortInfo sort) {
        this.closed = false;
        this.sort = sort;
        if (options == null) {
            this.opts = new HashMap<>(ALLOWED_KEYS.size());
        } else {
            this.opts = new HashMap<>(options);
        }

        if (Thread.currentThread() instanceof ScriptThread) {
            ((ScriptThread)Thread.currentThread()).closers.add(closer);
        }
        RunGroupContext rgc;
        if ((rgc = RunGroupContext.CONTEXT.get()) != null) {
            rgc.closers.add(closer);
        }
    }

    /**
     * The sorting algorithm this builder will run
     * @return The sorting algorithm this builder will run
     * @see SortInfo
     */
    public SortInfo getSort() {
        return sort;
    }

    /**
     * The options configured for this builder
     * @return The options configured for this builder
     */
    public Map<String, Object> getOpts() {
        return unmodifiableOpts == null ? (unmodifiableOpts = Collections.unmodifiableMap(opts)) : unmodifiableOpts;
    }

    @Override
    public String toString() {
        return "RunSortBuilder[sort=" + sort + ", opts=" + opts + "]";
    }

    /**
     * Merge the specified options with the options map
     * @param opts The options to merge, generally using Groovy's named argument syntax
     * @return {@code this} for chaining
     * @see RunSortInfoExtension
     */
    public RunSortBuilder with(Map<String, Object> opts) {
        if (opts != null) {
            for (Map.Entry<String, Object> opt : opts.entrySet()) {
                put(opt);
            }
        }
        return this;
    }

    /**
     * Merge the specified options with the options map
     * @param opts The options to merge, generally obtained with {@link RunSortInfoExtension}
     * @return {@code this} for chaining
     * @see RunSortInfoExtension
     */
    @SafeVarargs
    public final RunSortBuilder with(Map.Entry<String, Object>... opts) {
        for (Map.Entry<String, Object> opt : opts) {
            put(opt);
        }
        return this;
    }

    /**
     * Merge the specified options with the options map, and run the sort
     * @param opts The options to merge, generally using Groovy's named argument syntax
     * @see RunSortInfoExtension
     */
    public RunSortBuilder go(Map<String, Object> opts) {
        return with(opts).finish();
    }

    /**
     * Merge the specified options with the options map, and run the sort
     * @param opts The options to merge, generally obtained with {@link RunSortInfoExtension}
     * @see RunSortInfoExtension
     */
    @SafeVarargs
    public final RunSortBuilder go(Map.Entry<String, Object>... opts) {
        return with(opts).finish();
    }

    private void put(Map.Entry<String, Object> opt) {
        if (!ALLOWED_KEYS.contains(opt.getKey())) {
            throw new IllegalArgumentException("Invalid RunSortBuilder key: " + opt.getKey());
        }
        opts.put(opt.getKey(), opt.getValue());
    }

    /**
     * The configured length of the array (key of {@code "numbers"})
     * @return The configured length
     * @see RunSortInfoExtension#getNumbers
     */
    public int getLength() {
        // @checkstyle:off LeftCurlyCheck - It's more readable this way, I think
        return ((Number)opts.computeIfAbsent("numbers", k -> { throw new NullPointerException("numbers"); })).intValue();
        // @checkstyle:on LeftCurlyCheck
    }

    /**
     * The extra value to pass to the sort (key of {@code "buckets"})
     * @return The extra value
     * @see RunSortInfoExtension#getBuckets
     */
    public int getBuckets() {
        return ((Number)opts.getOrDefault("buckets", 0)).intValue();
    }

    /**
     * The speed multiplier of the visual (key of {@code "speed"})
     * @return The speed multiplier
     * @see RunSortInfoExtension#getSpeed
     */
    public double getSpeed() {
        return ((Number)opts.getOrDefault("speed", 1.0)).doubleValue();
    }

    private int calculateLength(int defaultLength, int startingLength) {
        if (startingLength != -1) {
            return (int)Math.max(defaultLength / 2048d * startingLength, 2);
        }
        return Math.max(defaultLength, 2);
    }

    private int calculateLengthSlow(int defaultLength, int unreasonableLimit, int startingLength) {
        return Math.min(calculateLength(defaultLength, startingLength), unreasonableLimit);
    }

    private double calculateSpeed(double defaultSpeed, int length, int startingLength) {
        if (startingLength != -1) {
            if (length < startingLength / 2) {
                return defaultSpeed * Math.pow(startingLength / 2048d, 2);
            } else {
                return defaultSpeed * (startingLength / 2048d);
            }
        }
        return defaultSpeed;
    }

    private void removeClosers() {
        if (Thread.currentThread() instanceof ScriptThread) {
            ((ScriptThread)Thread.currentThread()).closers.remove(closer);
        }
        RunGroupContext rgc;
        if ((rgc = RunGroupContext.CONTEXT.get()) != null) {
            rgc.closers.remove(closer);
        }
        // Set this just in case we removed the wrong closer.
        // This happens if we initialized this instance in a different thread
        // from the one where we are calling finish().
        closed = true;
    }

    private RunSortBuilder finish() {
        removeClosers();
        if (RunGroupContext.CONTEXT.get() == null) {
            final ArrayVisualizer arrayVisualizer = ArrayVisualizer.getInstance();
            Thread sortThread = new Thread(this::run0, "ScriptedSort");
            arrayVisualizer.setSortingThread(sortThread);
            arrayVisualizer.runSortingThread();
            try {
                sortThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            run0();
        }
        return this;
    }

    private void run0() {
        final ArrayVisualizer arrayVisualizer = ArrayVisualizer.getInstance();
        final ArrayManager arrayManager = arrayVisualizer.getArrayManager();
        final ArrayFrame arrayFrame = arrayVisualizer.getArrayFrame();
        final int[] array = arrayVisualizer.getArray();
        final Delays Delays = arrayVisualizer.getDelays();
        final Timer Timer = arrayVisualizer.getTimer();

        final RunGroupContext runGroupContext = RunGroupContext.CONTEXT.get();
        final int startingLength = runGroupContext != null ? runGroupContext.getStartingLength() : -1;

        Delays.setSleepRatio(2.5);

        int sortLength;
        if (sort.hasUnreasonableLimit()) {
            sortLength = calculateLengthSlow(getLength(), sort.getUnreasonableLimit(), startingLength);
        } else {
            sortLength = calculateLength(getLength(), startingLength);
        }
        if (sortLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(sortLength);
        }

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);

        if (runGroupContext != null) {
            arrayVisualizer.setHeading(sort.getRunAllName() + " (Sort " + runGroupContext.nextSort() + " of " + runGroupContext.getSortCount() + ")");
        } else {
            arrayVisualizer.setHeading(sort.getRunAllName());
        }

        double sortSpeed = calculateSpeed(getSpeed(), arrayVisualizer.getCurrentLength(), startingLength);
        Delays.setSleepRatio(sortSpeed);

        Timer.enableRealTimer();

        try {
            sort.getFreshInstance().runSort(array, arrayVisualizer.getCurrentLength(), getBuckets());
        } catch (StopSort ignored) {
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }

        arrayVisualizer.endSort();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}

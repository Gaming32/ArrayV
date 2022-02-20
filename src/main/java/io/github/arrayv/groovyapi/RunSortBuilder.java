package io.github.arrayv.groovyapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import io.github.arrayv.frames.ArrayFrame;
import io.github.arrayv.groovyapi.ScriptManager.ScriptThread;
import io.github.arrayv.main.ArrayManager;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sortdata.SortInfo;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.StopSort;
import io.github.arrayv.utils.Timer;

public final class RunSortBuilder {
    private static final Set<String> ALLOWED_KEYS = new HashSet<>(Arrays.asList(
        "numbers",
        "buckets",
        "delay"
    ));

    private final SortInfo sort;
    private final Map<String, Object> opts;
    private final Runnable closer = () -> {
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

    RunSortBuilder(SortInfo sort) {
        this.sort = sort;
        this.opts = new HashMap<>(ALLOWED_KEYS.size());
        if (Thread.currentThread() instanceof ScriptThread) {
            ((ScriptThread)Thread.currentThread()).closers.add(closer);
        }
    }

    public SortInfo getSort() {
        return sort;
    }

    public Map<String, Object> getOpts() {
        return unmodifiableOpts == null ? (unmodifiableOpts = Collections.unmodifiableMap(opts)) : unmodifiableOpts;
    }

    @Override
    public String toString() {
        return "RunSortBuilder[sort=" + sort + ", opts=" + opts + "]";
    }

    @SafeVarargs
    public final RunSortBuilder with(Map.Entry<String, Object>... opts) {
        for (Map.Entry<String, Object> opt : opts) {
            put(opt);
        }
        return this;
    }

    private void put(Map.Entry<String, Object> opt) {
        if (!ALLOWED_KEYS.contains(opt.getKey())) {
            throw new IllegalArgumentException("Invalid RunSortBuilder key: " + opt.getKey());
        }
        opts.put(opt.getKey(), opt.getValue());
    }

    public int getLength() {
        return (int)opts.computeIfAbsent("numbers", k -> { throw new NullPointerException("numbers"); });
    }

    public int getBuckets() {
        return (int)opts.getOrDefault("buckets", 0);
    }

    public double getDelay() {
        return (double)opts.getOrDefault("delay", 1.0);
    }

    public void and(Map.Entry<String, Object> opt) {
        put(opt);
        if (Thread.currentThread() instanceof ScriptThread) {
            ((ScriptThread)Thread.currentThread()).closers.remove(closer);
        }
        finish();
    }

    private int calculateLength(int defaultLength) {
        return (int) Math.max(defaultLength / 2048d * 2048, 2);
        // return (int) Math.max(defaultLength / 2048d * startingLength, 2);
    }

    private int calculateLengthSlow(int defaultLength, int unreasonableLimit) {
        return Math.min(calculateLength(defaultLength), unreasonableLimit);
    }

    private double calculateSpeed(double defaultDelay, int length) {
        if (length < 2048 / 2) {
            return defaultDelay * Math.pow(2048 / 2048d, 2);
        } else {
            return defaultDelay * (2048 / 2048d);
        }
        // if (length < startingLength / 2) {
        //     return defaultDelay * Math.pow(startingLength / 2048d, 2);
        // } else {
        //     return defaultDelay * (startingLength / 2048d);
        // }
    }

    private void finish() {
        final ArrayVisualizer arrayVisualizer = ArrayVisualizer.getInstance();
        Thread sortThread = new Thread(this::run, "ScriptedSort");

        arrayVisualizer.setSortingThread(sortThread);
        arrayVisualizer.runSortingThread();
        try {
            sortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        final ArrayVisualizer arrayVisualizer = ArrayVisualizer.getInstance();
        final ArrayManager arrayManager = arrayVisualizer.getArrayManager();
        final ArrayFrame arrayFrame = arrayVisualizer.getArrayFrame();
        final int[] array = arrayVisualizer.getArray();
        final Delays Delays = arrayVisualizer.getDelays();
        final Timer Timer = arrayVisualizer.getTimer();

        Delays.setSleepRatio(2.5);

        int sortLength;
        if (sort.isSlowSort()) {
            sortLength = calculateLengthSlow(getLength(), sort.getUnreasonableLimit());
        } else {
            sortLength = calculateLength(getLength());
        }
        if (sortLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(sortLength);
        }

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);

        // arrayVisualizer.setHeading(sort.getRunAllName() + " (Sort " + sortNumber + " of " + sortCount + ")");
        arrayVisualizer.setHeading(sort.getRunAllName());

        double sortSpeed = calculateSpeed(getDelay(), arrayVisualizer.getCurrentLength());
        Delays.setSleepRatio(sortSpeed);

        Timer.enableRealTimer();

        try {
            sort.getFreshInstance().runSort(array, arrayVisualizer.getCurrentLength(), getBuckets());
        } catch (StopSort e) {
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }

        arrayVisualizer.endSort();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }
}

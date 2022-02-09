package threads;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JErrorPane;
import frames.ArrayFrame;
import sorts.templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Sounds;
import utils.StopSort;
import utils.Timer;
import utils.Writes;

public abstract class MultipleSortThread {
    protected ArrayManager arrayManager;
    protected ArrayVisualizer arrayVisualizer;
    protected ArrayFrame arrayFrame;
    protected Delays Delays;
    protected Highlights Highlights;
    protected Reads Reads;
    protected Writes Writes;
    protected Sounds Sounds;
    protected Timer Timer;

    protected volatile int sortCount;
    protected volatile int sortNumber;

    protected volatile int categoryCount;

    private int startingLength;

    public MultipleSortThread(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();
        this.arrayFrame = arrayVisualizer.getArrayFrame();
        this.Delays = arrayVisualizer.getDelays();
        this.Highlights = arrayVisualizer.getHighlights();
        this.Reads = arrayVisualizer.getReads();
        this.Writes = arrayVisualizer.getWrites();
        this.Sounds = arrayVisualizer.getSounds();
        this.Timer = arrayVisualizer.getTimer();

        this.startingLength = arrayVisualizer.getCurrentLength();
    }

    protected int calculateLength(int defaultLength) {
        return (int) Math.max((defaultLength / 2048d) * this.startingLength, 2);
    }
    protected int calculateLengthSlow(int defaultLength, int unreasonableLimit) {
        return Math.min(this.calculateLength(defaultLength), unreasonableLimit);
    }

    protected double calculateSpeed(double defaultDelay, int length) {
        if (length < (this.startingLength / 2)) {
            return defaultDelay * Math.pow((this.startingLength / 2048d), 2);
        } else {
            return defaultDelay * (this.startingLength / 2048d);
        }
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort) throws Exception {
        Delays.setSleepRatio(2.5);

        int sortLength;
        if (slowSort) {
            sortLength = this.calculateLengthSlow(defaultLength, sort.getUnreasonableLimit());
        } else {
            sortLength = this.calculateLength(defaultLength);
        }
        if (sortLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(sortLength);
        }

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), this.arrayVisualizer);

        arrayVisualizer.setHeading(sort.getRunAllSortsName() + " (Sort " + this.sortNumber + " of " + this.sortCount + ")");

        double sortSpeed = this.calculateSpeed(defaultSpeed, arrayVisualizer.getCurrentLength());
        Delays.setSleepRatio(sortSpeed);

        Timer.enableRealTimer();

        // arrayVisualizer.toggleVisualUpdates(true);
        try {
            sort.runSort(array, arrayVisualizer.getCurrentLength(), bucketCount);
        } catch (StopSort e) {
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
        // arrayVisualizer.toggleVisualUpdates(false);

        arrayVisualizer.endSort();
        Thread.sleep(1000);

        this.sortNumber++;
    }

    protected abstract void executeSortList(int[] array) throws Exception;
    protected abstract void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception;

    public synchronized void reportCategorySorts(int[] array) throws Exception {
        this.runThread(array, 0, 0, false);
    }

    public synchronized void reportAllSorts(int[] array, int current, int total) throws Exception {
        this.runThread(array, current, total, true);
    }

    public int getSortCount() {
        return this.sortCount;
    }

    public int getCategoryCount() {
        return this.categoryCount;
    }
}
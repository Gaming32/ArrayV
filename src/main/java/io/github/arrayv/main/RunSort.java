package io.github.arrayv.main;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import io.github.arrayv.panes.JEnhancedOptionPane;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.SortingNetworkGenerator;
import io.github.arrayv.utils.Sounds;
import io.github.arrayv.utils.StopSort;
import io.github.arrayv.utils.Timer;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class RunSort {
    private ArrayManager arrayManager;
    private ArrayVisualizer arrayVisualizer;
    private Delays delayOps;
    private Sounds sounds;
    private Timer realTimer;

    private Object[] inputOptions;

    public RunSort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();
        this.delayOps = arrayVisualizer.getDelays();
        this.sounds = arrayVisualizer.getSounds();
        this.realTimer = arrayVisualizer.getTimer();

        this.inputOptions = new Object[]{"Enter", "Use default"};
    }

    private String getTimeSortEstimate(int bucketCount) {
        String timeString = "";
        String timeUnit;

        int seconds = Math.max(((arrayVisualizer.getCurrentLength() * bucketCount) / 1000), 1);
        int minutes;
        int hours;
        long days;

        if (seconds >= 60) {
            minutes = Math.round(seconds / 60);

            if (minutes >= 60) {
                hours = Math.round(minutes / 60);

                if (hours >= 24) {
                    days = Math.round(hours / 24);

                    if (days < 2)  timeUnit = "day";
                    else           timeUnit = "days";

                    timeString = "" + arrayVisualizer.getNumberFormat().format(days) + " " + timeUnit + " ";
                } else {
                    if (hours < 2) timeUnit = "hour";
                    else           timeUnit = "hours";

                    timeString = "" + hours + " " + timeUnit + " ";
                }
            } else {
                if (minutes < 2) timeUnit = "minute";
                else             timeUnit = "minutes";

                timeString = "" + minutes + " " + timeUnit + " ";
            }
        } else {
            if (seconds < 2) timeUnit = "second";
            else             timeUnit = "seconds";

            timeString = "" + seconds + " " + timeUnit + " ";
        }

        return timeString;
    }

    private int getCustomInput(String text) throws Exception {
        String input = JEnhancedOptionPane.showInputDialog("Customize Sort", text, this.inputOptions);
        int integer = Integer.parseInt(input);
        return Math.abs(integer);
    }

    public void runSort(int[] array, int selection) {
        if (arrayVisualizer.isActive())
            return;

        //TODO: This code is bugged! It causes the program to forget the sleep ratio specified by the user!
        if (delayOps.skipped()) {
            delayOps.setSleepRatio(1);
            delayOps.changeSkipped(false);
        }

        double storeVol = sounds.getVolume();
        sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("ComparisonSorting") {
            @Override
            public void run() {
                try {
                    Sort sort = arrayVisualizer.getSorts()[selection].getFreshInstance();
                    int extra = 0;

                    if (sort.getQuestion() != null) {
                        try {
                            extra = sort.validateAnswer(getCustomInput(sort.getQuestion()));
                        } catch (Exception e) {
                            extra = sort.getDefaultAnswer();
                        }
                    } else if (sort.usesBuckets()) {
                        if (sort.isRadixSort()) {
                            try {
                                extra = getCustomInput("Enter the base for this sort:");
                            } catch (Exception e) {
                                extra = 4;
                            }
                        } else if (sort.getRunSortName().contains("Shatter")) {
                            try {
                                extra = getCustomInput("Enter the size for each partition:");
                            } catch (Exception e) {
                                extra = arrayVisualizer.getCurrentLength() / 16;
                            }
                        } else {
                            try {
                                extra = getCustomInput("How many buckets will this sort use?");
                            } catch (Exception e) {
                                extra = 16;
                            }
                        }
                        if (extra < 2) extra = 2;
                    } else {
                        extra = 0;
                    }

                    boolean goAhead;

                    if (sort.getRunSortName().equals("Timesort")) {
                        Object[] options = { "Continue", "Cancel" };

                        int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Time Sort will take at least " + getTimeSortEstimate(extra)
                                                                 + "to complete. Once it starts, you cannot skip this sort.", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                 null, options, options[1]);

                        if (warning == 0) goAhead = true;
                        else goAhead = false;
                    } else if (sort.isUnreasonablySlow() && arrayVisualizer.getCurrentLength() > sort.getUnreasonableLimit()) {
                        goAhead = false;

                        Object[] options = { "Let's see how bad " + sort.getRunSortName() + " is!", "Cancel" };

                        if (sort.isBogoSort()) {
                            int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Even at a high speed, "
                                                                    + sort.getRunSortName() + "ing " + arrayVisualizer.getCurrentLength()
                                                                    + " numbers will almost certainly not finish in a reasonable amount of time. "
                                                                    + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                    null, options, options[1]);
                            if (warning == 0) goAhead = true;
                            else goAhead = false;
                        } else {
                            int warning = JOptionPane.showOptionDialog(arrayVisualizer.getMainWindow(), "Even at a high speed, "
                                                                    + sort.getRunSortName() + "ing " + arrayVisualizer.getCurrentLength()
                                                                    + " numbers will not finish in a reasonable amount of time. "
                                                                    + "Are you sure you want to continue?", "Warning!", 2, JOptionPane.WARNING_MESSAGE,
                                                                    null, options, options[1]);

                            if (warning == 0) goAhead = true;
                            else goAhead = false;
                        }
                    } else {
                        goAhead = true;
                    }

                    if (goAhead) {
                        if (sort.getRunSortName().equals("In-Place LSD Radix")) {
                            sounds.changeVolume(0.01); // Here to protect your ears :)
                        }

                        arrayManager.toggleMutableLength(false);
                        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);

                        arrayVisualizer.setHeading(sort.getRunSortName());
                        arrayVisualizer.setCategory(sort.getCategory());

                        realTimer.enableRealTimer();
                        boolean antiq = arrayVisualizer.useAntiQSort();
                        boolean networks = arrayVisualizer.generateSortingNetworks();
                        if (antiq)
                            arrayVisualizer.initAntiQSort();

                        try {
                            sort.runSort(array, arrayVisualizer.getCurrentLength(), extra);
                        } catch (StopSort e) {
                        } catch (OutOfMemoryError e) {
                            JErrorPane.invokeCustomErrorMessage(sort.getRunAllSortsName() + " ran out of memory: " + e.getMessage());
                            throw new RuntimeException(e);
                        }

                        if (antiq)
                            arrayVisualizer.finishAntiQSort(sort.getClass().getSimpleName());
                        else if (networks) {
                            ArrayList<Integer> indicesList = arrayVisualizer.getReads().getNetworkIndices();
                            SortingNetworkGenerator.encodeNetworkAndDisplay(
                                sort.getClass().getSimpleName(),
                                indicesList,
                                arrayVisualizer.getCurrentLength()
                            );
                        }
                    } else {
                        arrayManager.initializeArray(array);
                    }
                } catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                arrayVisualizer.endSort();
                arrayManager.toggleMutableLength(true);
                sounds.changeVolume(storeVol);
                sounds.toggleSound(false);
                System.gc(); // Reduce RAM usage from any high-memory tasks (e.g. visualizing a sorting network)
            }
        });

        arrayVisualizer.runSortingThread();
    }
}

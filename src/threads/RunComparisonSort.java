package threads;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JEnhancedOptionPane;
import panes.JErrorPane;
import sorts.templates.Sort;
import utils.Delays;
import utils.SortingNetworkGenerator;
import utils.Sounds;
import utils.StopSort;
import utils.Timer;

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

final public class RunComparisonSort {
    private ArrayManager arrayManager;
    private ArrayVisualizer arrayVisualizer;
    private Delays delayOps;
    private Sounds sounds;
    private Timer realTimer;

    private Object[] inputOptions;

    public RunComparisonSort(ArrayVisualizer arrayVisualizer) {
        this.arrayVisualizer = arrayVisualizer;
        this.arrayManager = arrayVisualizer.getArrayManager();
        this.delayOps = arrayVisualizer.getDelays();
        this.sounds = arrayVisualizer.getSounds();
        this.realTimer = arrayVisualizer.getTimer();

        this.inputOptions = new Object[]{"Enter", "Use default"};
    }

    private int getCustomInput(String text) throws Exception {
        String input = JEnhancedOptionPane.showInputDialog("Customize Sort", text, this.inputOptions);
        int integer = Integer.parseInt(input);
        return Math.abs(integer);
    }

    public void ReportComparativeSort(int[] array, int selection) {
        if (arrayVisualizer.isActive())
            return;

        //TODO: This code is bugged! It causes the program to forget the sleep ratio specified by the user!
        if (delayOps.skipped()) {
            delayOps.setSleepRatio(1);
            delayOps.changeSkipped(false);
        }

        sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("ComparisonSorting") {
            @Override
            public void run() {
                try {
                    Class<?> sortClass = arrayVisualizer.getComparisonSorts()[selection].sortClass;
                    Constructor<?> newSort = sortClass.getConstructor(new Class[] {ArrayVisualizer.class});
                    Sort sort = (Sort) newSort.newInstance(RunComparisonSort.this.arrayVisualizer);

                    int extra = 0;

                    if (sort.getQuestion() != null) {
                        try {
                            extra = sort.validateAnswer(getCustomInput(sort.getQuestion()));
                        } catch (Exception e) {
                            extra = sort.getDefaultAnswer();
                        }
                    }

                    boolean goAhead;

                    if (sort.isUnreasonablySlow() && arrayVisualizer.getCurrentLength() > sort.getUnreasonableLimit()) {
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
                            ArrayList<Integer> indicesList = arrayVisualizer.getReads().networkIndices;
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
                sounds.toggleSound(false);
                System.gc(); // Reduce RAM usage from any high-memory tasks (e.g. visualizing a sorting network)
            }
        });

        arrayVisualizer.runSortingThread();
    }
}
package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.misc.BurntPancakeSort;
import sorts.misc.PancakeSort;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2021 ArrayV 4.0 Team
Copyright (c) 2022 ArrayV Team

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

final public class RunMiscellaneousSorts extends MultipleSortThread {
    private Sort PancakeSort;
    private Sort BurntPancakeSort;

    public RunMiscellaneousSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 2;
        this.categoryCount = this.sortCount;

        PancakeSort      = new      PancakeSort(this.arrayVisualizer);
        BurntPancakeSort = new BurntPancakeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunMiscellaneousSorts.this.runIndividualSort(PancakeSort,      0, array, 128, 0.015, false);
        RunMiscellaneousSorts.this.runIndividualSort(BurntPancakeSort, 0, array, 128, 0.015, false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("MiscSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunMiscellaneousSorts.this.sortNumber = current;
                        RunMiscellaneousSorts.this.sortCount = total;
                    } else {
                        RunMiscellaneousSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Miscellaneous Sorts");

                    RunMiscellaneousSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Miscellaneous Sorts");
                        arrayVisualizer.setHeading("Done");
                    }

                    arrayManager.toggleMutableLength(true);
                } catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });
        arrayVisualizer.runSortingThread();
    }
}
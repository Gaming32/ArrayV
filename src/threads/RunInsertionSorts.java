package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.insert.AATreeSort;
import sorts.insert.AVLTreeSort;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.ClassicTreeSort;
import sorts.insert.DoubleInsertionSort;
import sorts.insert.InsertionSort;
import sorts.insert.LibrarySort;
import sorts.insert.PatienceSort;
import sorts.insert.RecursiveShellSort;
import sorts.insert.ShellSort;
import sorts.insert.ShellSortParallel;
import sorts.insert.SimplifiedLibrarySort;
import sorts.insert.SplaySort;
import sorts.insert.TreeSort;
import sorts.templates.Sort;
import utils.Shuffles;

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

final public class RunInsertionSorts extends MultipleSortThread {
    private Sort InsertionSort;
    private Sort DoubleInsertionSort;
    private Sort BinaryInsertionSort;
    private Sort ShellSort;
    private Sort RecursiveShellSort;
    private Sort SimplifiedLibrarySort;
    private Sort PatienceSort;
    private Sort ClassicTreeSort;
    private Sort AATreeSort;
    private Sort AVLTreeSort;
    private Sort SplaySort;
    private Sort LibrarySort;
    private Sort ShellSortParallel;
    private Sort TreeSort;

    public RunInsertionSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;

        InsertionSort               = new               InsertionSort(this.arrayVisualizer);
        DoubleInsertionSort         = new         DoubleInsertionSort(this.arrayVisualizer);
        BinaryInsertionSort         = new         BinaryInsertionSort(this.arrayVisualizer);
        ShellSort                   = new                   ShellSort(this.arrayVisualizer);
        RecursiveShellSort          = new          RecursiveShellSort(this.arrayVisualizer);
        SimplifiedLibrarySort       = new       SimplifiedLibrarySort(this.arrayVisualizer);
        PatienceSort                = new                PatienceSort(this.arrayVisualizer);
        ClassicTreeSort             = new             ClassicTreeSort(this.arrayVisualizer);
        AATreeSort                  = new                  AATreeSort(this.arrayVisualizer);
        AVLTreeSort                 = new                 AVLTreeSort(this.arrayVisualizer);
        SplaySort                   = new                   SplaySort(this.arrayVisualizer);
        LibrarySort                 = new                 LibrarySort(this.arrayVisualizer);
        ShellSortParallel           = new           ShellSortParallel(this.arrayVisualizer);
        TreeSort                    = new                    TreeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunInsertionSorts.this.runIndividualSort(InsertionSort,               0, array,  128,  0.005, false);
        RunInsertionSorts.this.runIndividualSort(DoubleInsertionSort,         0, array,  128,  0.002, false);
        RunInsertionSorts.this.runIndividualSort(BinaryInsertionSort,         0, array,  128,  0.025, false);
        RunInsertionSorts.this.runIndividualSort(ShellSort,                   0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(RecursiveShellSort,          0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(ShellSortParallel,           0, array,  256,  0.1,   false);
        RunInsertionSorts.this.runIndividualSort(SimplifiedLibrarySort,       0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(LibrarySort,                 0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(PatienceSort,                0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(ClassicTreeSort,             0, array,  2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5, false);
        RunInsertionSorts.this.runIndividualSort(TreeSort,                    0, array,  2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5, false);
        RunInsertionSorts.this.runIndividualSort(AATreeSort,                  0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(AVLTreeSort,                 0, array,  2048, 1,     false);
        RunInsertionSorts.this.runIndividualSort(SplaySort,                   0, array,  2048, 1,     false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("InsertionSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunInsertionSorts.this.sortNumber = current;
                        RunInsertionSorts.this.sortCount = total;
                    } else {
                        RunInsertionSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Insertion Sorts");

                    RunInsertionSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Insertion Sorts");
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
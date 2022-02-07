package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.concurrent.BitonicSortIterative;
import sorts.concurrent.BitonicSortParallel;
import sorts.concurrent.BitonicSortRecursive;
import sorts.concurrent.BoseNelsonSortIterative;
import sorts.concurrent.BoseNelsonSortParallel;
import sorts.concurrent.BoseNelsonSortRecursive;
import sorts.concurrent.CreaseSort;
import sorts.concurrent.DiamondSortIterative;
import sorts.concurrent.DiamondSortRecursive;
import sorts.concurrent.FoldSort;
import sorts.concurrent.MatrixSort;
import sorts.concurrent.MergeExchangeSortIterative;
import sorts.concurrent.OddEvenMergeSortIterative;
import sorts.concurrent.OddEvenMergeSortParallel;
import sorts.concurrent.OddEvenMergeSortRecursive;
import sorts.concurrent.PairwiseMergeSortIterative;
import sorts.concurrent.PairwiseMergeSortRecursive;
import sorts.concurrent.PairwiseSortIterative;
import sorts.concurrent.PairwiseSortRecursive;
import sorts.concurrent.WeaveSortIterative;
import sorts.concurrent.WeaveSortParallel;
import sorts.concurrent.WeaveSortRecursive;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 ArrayV 4.0 Team
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

final public class RunConcurrentSorts extends MultipleSortThread {
    private Sort FoldSort;
    private Sort CreaseSort;
    private Sort MatrixSort;
    private Sort BitonicSortRecursive;
    private Sort OddEvenMergeSortRecursive;
    private Sort PairwiseSortRecursive;
    private Sort BoseNelsonSortRecursive;
    private Sort WeaveSortRecursive;
    private Sort BitonicSortIterative;
    private Sort OddEvenMergeSortIterative;
    private Sort PairwiseSortIterative;
    private Sort BoseNelsonSortIterative;
    private Sort WeaveSortIterative;
    private Sort MergeExchangeSortIterative;
    private Sort BitonicSortParallel;
    private Sort BoseNelsonSortParallel;
    private Sort DiamondSortIterative;
    private Sort DiamondSortRecursive;
    private Sort OddEvenMergeSortParallel;
    private Sort PairwiseMergeSortIterative;
    private Sort PairwiseMergeSortRecursive;
    private Sort WeaveSortParallel;

    public RunConcurrentSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 22;
        this.categoryCount = this.sortCount;

        FoldSort                   = new                   FoldSort(this.arrayVisualizer);
        CreaseSort                 = new                 CreaseSort(this.arrayVisualizer);
        MatrixSort                 = new                 MatrixSort(this.arrayVisualizer);
        BitonicSortRecursive       = new       BitonicSortRecursive(this.arrayVisualizer);
        OddEvenMergeSortRecursive  = new  OddEvenMergeSortRecursive(this.arrayVisualizer);
        PairwiseSortRecursive      = new      PairwiseSortRecursive(this.arrayVisualizer);
        BoseNelsonSortRecursive    = new    BoseNelsonSortRecursive(this.arrayVisualizer);
        WeaveSortRecursive         = new         WeaveSortRecursive(this.arrayVisualizer);
        BitonicSortIterative       = new       BitonicSortIterative(this.arrayVisualizer);
        OddEvenMergeSortIterative  = new  OddEvenMergeSortIterative(this.arrayVisualizer);
        PairwiseSortIterative      = new      PairwiseSortIterative(this.arrayVisualizer);
        BoseNelsonSortIterative    = new    BoseNelsonSortIterative(this.arrayVisualizer);
        WeaveSortIterative         = new         WeaveSortIterative(this.arrayVisualizer);
        MergeExchangeSortIterative = new MergeExchangeSortIterative(this.arrayVisualizer);
        BitonicSortParallel        = new        BitonicSortParallel(this.arrayVisualizer);
        BoseNelsonSortParallel     = new     BoseNelsonSortParallel(this.arrayVisualizer);
        DiamondSortIterative       = new       DiamondSortIterative(this.arrayVisualizer);
        DiamondSortRecursive       = new       DiamondSortRecursive(this.arrayVisualizer);
        OddEvenMergeSortParallel   = new   OddEvenMergeSortParallel(this.arrayVisualizer);
        PairwiseMergeSortIterative = new PairwiseMergeSortIterative(this.arrayVisualizer);
        PairwiseMergeSortRecursive = new PairwiseMergeSortRecursive(this.arrayVisualizer);
        WeaveSortParallel          = new          WeaveSortParallel(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        // Other
        RunConcurrentSorts.this.runIndividualSort(FoldSort,                   0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(CreaseSort,                 0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(MatrixSort,                 0, array, 256,  0.667, false);

        // Recursive
        RunConcurrentSorts.this.runIndividualSort(BitonicSortRecursive,       0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(OddEvenMergeSortRecursive,  0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(PairwiseSortRecursive,      0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(BoseNelsonSortRecursive,    0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(WeaveSortRecursive,         0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(DiamondSortRecursive,       0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(PairwiseMergeSortRecursive, 0, array, 1024, 1,     false);

        // Parallel
        RunConcurrentSorts.this.runIndividualSort(BitonicSortParallel,        0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(OddEvenMergeSortParallel,   0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(BoseNelsonSortParallel,     0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(WeaveSortParallel,          0, array, 1024, 1,     false);

        // Iterative
        RunConcurrentSorts.this.runIndividualSort(BitonicSortIterative,       0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(OddEvenMergeSortIterative,  0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(PairwiseSortIterative,      0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(BoseNelsonSortIterative,    0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(WeaveSortIterative,         0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(MergeExchangeSortIterative, 0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(DiamondSortIterative,       0, array, 1024, 1,     false);
        RunConcurrentSorts.this.runIndividualSort(PairwiseMergeSortIterative, 0, array, 1024, 1,     false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("ConcurrentSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunConcurrentSorts.this.sortNumber = current;
                        RunConcurrentSorts.this.sortCount = total;
                    } else {
                        RunConcurrentSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Concurrent Sorts");

                    RunConcurrentSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Concurrent Sorts");
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
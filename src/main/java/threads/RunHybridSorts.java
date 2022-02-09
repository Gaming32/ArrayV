package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.hybrid.AdaptiveGrailSort;
import sorts.hybrid.BinaryMergeSort;
import sorts.hybrid.BufferPartitionMergeSort;
import sorts.hybrid.CocktailMergeSort;
import sorts.hybrid.DropMergeSort;
import sorts.hybrid.EctaSort;
import sorts.hybrid.FlanSort;
import sorts.hybrid.GrailSort;
import sorts.hybrid.HybridCombSort;
import sorts.hybrid.ImprovedBlockSelectionSort;
import sorts.hybrid.IntroCircleSortIterative;
import sorts.hybrid.IntroCircleSortRecursive;
import sorts.hybrid.IntroSort;
import sorts.hybrid.KotaSort;
import sorts.hybrid.LaziestSort;
import sorts.hybrid.MedianMergeSort;
import sorts.hybrid.MergeInsertionSort;
import sorts.hybrid.OptimizedBottomUpMergeSort;
import sorts.hybrid.OptimizedDualPivotQuickSort;
import sorts.hybrid.OptimizedWeaveMergeSort;
import sorts.hybrid.PDQBranchedSort;
import sorts.hybrid.PDQBranchlessSort;
import sorts.hybrid.ParallelBlockMergeSort;
import sorts.hybrid.ParallelGrailSort;
import sorts.hybrid.RemiSort;
import sorts.hybrid.SqrtSort;
import sorts.hybrid.StacklessDualPivotQuickSort;
import sorts.hybrid.StacklessHybridQuickSort;
import sorts.hybrid.TimSort;
import sorts.hybrid.UnstableGrailSort;
import sorts.hybrid.WeaveMergeSort;
import sorts.hybrid.WikiSort;
import sorts.templates.Sort;
import utils.Shuffles;

/*
 *
MIT License

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

final public class RunHybridSorts extends MultipleSortThread {
    private Sort HybridCombSort;
    private Sort IntroCircleSortRecursive;
    private Sort IntroCircleSortIterative;
    private Sort BinaryMergeSort;
    private Sort MergeInsertionSort;
    private Sort WeaveMergeSort;
    private Sort TimSort;
    private Sort CocktailMergeSort;
    private Sort LaziestSort;
    private Sort WikiSort;
    private Sort GrailSort;
    private Sort UnstableGrailSort;
    private Sort SqrtSort;
    private Sort KotaSort;
    private Sort EctaSort;
    private Sort ImprovedBlockSelectionSort;
    private Sort MedianMergeSort;
    private Sort IntroSort;
    private Sort OptimizedBottomUpMergeSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort OptimizedWeaveMergeSort;
    private Sort StacklessHybridQuickSort;
    private Sort PDQBranchedSort;
    private Sort PDQBranchlessSort;
    private Sort DropMergeSort;
    private Sort FlanSort;
    private Sort BufferPartitionMergeSort;
    private Sort ParallelBlockMergeSort;
    private Sort ParallelGrailSort;
    private Sort RemiSort;
    private Sort StacklessDualPivotQuickSort;
    private Sort AdaptiveGrailSort;

    public RunHybridSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 32;
        this.categoryCount = this.sortCount;

        HybridCombSort                   = new                   HybridCombSort(this.arrayVisualizer);
        IntroCircleSortRecursive         = new         IntroCircleSortRecursive(this.arrayVisualizer);
        IntroCircleSortIterative         = new         IntroCircleSortIterative(this.arrayVisualizer);
        BinaryMergeSort                  = new                  BinaryMergeSort(this.arrayVisualizer);
        MergeInsertionSort               = new               MergeInsertionSort(this.arrayVisualizer);
        WeaveMergeSort                   = new                   WeaveMergeSort(this.arrayVisualizer);
        TimSort                          = new                          TimSort(this.arrayVisualizer);
        CocktailMergeSort                = new                CocktailMergeSort(this.arrayVisualizer);
        LaziestSort                      = new                      LaziestSort(this.arrayVisualizer);
        WikiSort                         = new                         WikiSort(this.arrayVisualizer);
        GrailSort                        = new                        GrailSort(this.arrayVisualizer);
        UnstableGrailSort                = new                UnstableGrailSort(this.arrayVisualizer);
        SqrtSort                         = new                         SqrtSort(this.arrayVisualizer);
        KotaSort                         = new                         KotaSort(this.arrayVisualizer);
        EctaSort                         = new                         EctaSort(this.arrayVisualizer);
        FlanSort                         = new                         FlanSort(this.arrayVisualizer);
        ImprovedBlockSelectionSort       = new       ImprovedBlockSelectionSort(this.arrayVisualizer);
        MedianMergeSort                  = new                  MedianMergeSort(this.arrayVisualizer);
        IntroSort                        = new                        IntroSort(this.arrayVisualizer);
        OptimizedBottomUpMergeSort       = new       OptimizedBottomUpMergeSort(this.arrayVisualizer);
        OptimizedDualPivotQuickSort      = new      OptimizedDualPivotQuickSort(this.arrayVisualizer);
        OptimizedWeaveMergeSort          = new          OptimizedWeaveMergeSort(this.arrayVisualizer);
        StacklessHybridQuickSort         = new         StacklessHybridQuickSort(this.arrayVisualizer);
        PDQBranchedSort                  = new                  PDQBranchedSort(this.arrayVisualizer);
        PDQBranchlessSort                = new                PDQBranchlessSort(this.arrayVisualizer);
        DropMergeSort                    = new                    DropMergeSort(this.arrayVisualizer);
        BufferPartitionMergeSort         = new         BufferPartitionMergeSort(this.arrayVisualizer);
        ParallelBlockMergeSort           = new           ParallelBlockMergeSort(this.arrayVisualizer);
        ParallelGrailSort                = new                ParallelGrailSort(this.arrayVisualizer);
        RemiSort                         = new                         RemiSort(this.arrayVisualizer);
        StacklessDualPivotQuickSort      = new      StacklessDualPivotQuickSort(this.arrayVisualizer);
        AdaptiveGrailSort                = new                AdaptiveGrailSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunHybridSorts.this.runIndividualSort(HybridCombSort,                   0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroCircleSortRecursive,         0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroCircleSortIterative,         0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(BinaryMergeSort,                  0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(MergeInsertionSort,               0, array, 2048, 1.75, false);
        RunHybridSorts.this.runIndividualSort(WeaveMergeSort,                   0, array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.65 : 6.5, false);
        RunHybridSorts.this.runIndividualSort(TimSort,                          0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(CocktailMergeSort,                0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(LaziestSort,                      0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(WikiSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(GrailSort,                        0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(AdaptiveGrailSort,                0, array, 2048, 1,    false);
        // RunHybridSorts.this.runIndividualSort(HolyGrailSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(UnstableGrailSort,                0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(SqrtSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(KotaSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(EctaSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(ParallelBlockMergeSort,           0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(ParallelGrailSort,                0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(FlanSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(RemiSort,                         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(ImprovedBlockSelectionSort,       0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(MedianMergeSort,                  0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(BufferPartitionMergeSort,         0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroSort,                        0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedBottomUpMergeSort,       0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedDualPivotQuickSort,      0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(OptimizedWeaveMergeSort,          0, array, 1024, 0.4,  false);
        RunHybridSorts.this.runIndividualSort(StacklessHybridQuickSort,         0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(StacklessDualPivotQuickSort,      0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(PDQBranchedSort,                  0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(PDQBranchlessSort,                0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(DropMergeSort,                    0, array, 2048, 0.75, false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("HybridSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunHybridSorts.this.sortNumber = current;
                        RunHybridSorts.this.sortCount = total;
                    } else {
                        RunHybridSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Hybrid Sorts");

                    RunHybridSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Hybrid Sorts");
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
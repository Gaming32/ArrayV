package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;

import sorts.hybrid.*;
import sorts.templates.Sort;
import utils.Shuffles;

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

final public class RunHybridSorts extends MultipleSortThread {
    private Sort HybridCombSort;
    private Sort IntroCircleSort;
    private Sort IterativeIntroCircleSort;
    private Sort PairwiseCircleSort;
    private Sort BinaryMergeSort;
    private Sort SwapMergeSort;
    private Sort BaseNMergeSort;
    private Sort WeaveMergeSort;
    private Sort ImprovedWeaveMergeSort;
    private Sort TimSort;
    private Sort CocktailMergeSort;
    private Sort WikiSort;
    private Sort GrailSort;
    private Sort UnstableGrailSort;
    private Sort SqrtSort;
    private Sort KotaSort;
    private Sort EctaSort;
    private Sort BufferedMergeSort;
    private Sort OOPBufferedMergeSort;
    private Sort BlockSelectionMergeSort;
    private Sort MedianMergeSort;
    private Sort IntroSort;
    private Sort OptimizedBottomUpMergeSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort StupidQuickSort;
    private Sort LAQuickSort;
    private Sort StacklessHybridQuickSort;
    private Sort PDQBranchedSort;
    private Sort PDQBranchlessSort;
    private Sort DropMergeSort;
    
    public RunHybridSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 30;
        this.categoryCount = this.sortCount;
        
        HybridCombSort              = new              HybridCombSort(this.arrayVisualizer);
        IntroCircleSort             = new             IntroCircleSort(this.arrayVisualizer);
        IterativeIntroCircleSort    = new    IterativeIntroCircleSort(this.arrayVisualizer);
        PairwiseCircleSort          = new          PairwiseCircleSort(this.arrayVisualizer);
        BinaryMergeSort             = new             BinaryMergeSort(this.arrayVisualizer);
        SwapMergeSort               = new               SwapMergeSort(this.arrayVisualizer);
        BaseNMergeSort              = new              BaseNMergeSort(this.arrayVisualizer);
        WeaveMergeSort              = new              WeaveMergeSort(this.arrayVisualizer);
        ImprovedWeaveMergeSort      = new      ImprovedWeaveMergeSort(this.arrayVisualizer);
        TimSort                     = new                     TimSort(this.arrayVisualizer);
        CocktailMergeSort           = new           CocktailMergeSort(this.arrayVisualizer);
        WikiSort                    = new                    WikiSort(this.arrayVisualizer);
        GrailSort                   = new                   GrailSort(this.arrayVisualizer);
        UnstableGrailSort           = new           UnstableGrailSort(this.arrayVisualizer);
        SqrtSort                    = new                    SqrtSort(this.arrayVisualizer);
        KotaSort                    = new                    KotaSort(this.arrayVisualizer);
        EctaSort                    = new                    EctaSort(this.arrayVisualizer);
        BufferedMergeSort           = new           BufferedMergeSort(this.arrayVisualizer);
        OOPBufferedMergeSort        = new        StableBufferedMerge(this.arrayVisualizer);
        BlockSelectionMergeSort     = new     BlockSelectionMergeSort(this.arrayVisualizer);
        MedianMergeSort             = new             MedianMergeSort(this.arrayVisualizer);
        IntroSort                   = new                   IntroSort(this.arrayVisualizer);
        OptimizedBottomUpMergeSort  = new  OptimizedBottomUpMergeSort(this.arrayVisualizer);
        OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(this.arrayVisualizer);
        StupidQuickSort             = new             StupidQuickSort(this.arrayVisualizer);
        LAQuickSort                 = new                 LAQuickSort(this.arrayVisualizer);
        StacklessHybridQuickSort    = new    StacklessHybridQuickSort(this.arrayVisualizer);
        PDQBranchedSort             = new             PDQBranchedSort(this.arrayVisualizer);
        PDQBranchlessSort           = new           PDQBranchlessSort(this.arrayVisualizer);
        DropMergeSort               = new               DropMergeSort(this.arrayVisualizer);
    }
    
    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunHybridSorts.this.runIndividualSort(HybridCombSort,              0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroCircleSort,             0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IterativeIntroCircleSort,    0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(PairwiseCircleSort,          0, array, 1024, 1.5,  false);
        RunHybridSorts.this.runIndividualSort(BinaryMergeSort,             0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(SwapMergeSort,               0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.65 : 6.5,    false);
        RunHybridSorts.this.runIndividualSort(BaseNMergeSort,              4, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(WeaveMergeSort,              0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.65 : 6.5, false);
        RunHybridSorts.this.runIndividualSort(ImprovedWeaveMergeSort,      0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.65 : 6.5, false);
        RunHybridSorts.this.runIndividualSort(TimSort,                     0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(CocktailMergeSort,           0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(WikiSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(GrailSort,                   0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(UnstableGrailSort,           0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(SqrtSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(KotaSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(EctaSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(BufferedMergeSort,           0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OOPBufferedMergeSort,        0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(BlockSelectionMergeSort,     0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(MedianMergeSort,             0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroSort,                   0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedBottomUpMergeSort,  0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(StupidQuickSort,             0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(LAQuickSort,                 0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(StacklessHybridQuickSort,    0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(PDQBranchedSort,             0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(PDQBranchlessSort,           0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(DropMergeSort,               0, array, 2048, 0.75, false);
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        RunHybridSorts.this.sortNumber = current;
                        RunHybridSorts.this.sortCount = total;
                    }
                    else {
                        RunHybridSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Hybrid Sorts");

                    RunHybridSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Hybrid Sorts");
                        arrayVisualizer.setHeading("Done");
                    }
                    
                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });
        arrayVisualizer.runSortingThread();
    }
}
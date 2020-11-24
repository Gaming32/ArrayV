package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.hybrid.BaseNMergeSort;
import sorts.hybrid.BinaryMergeSort;
import sorts.hybrid.BlockSelectionMergeSort;
import sorts.hybrid.BranchedPDQSort;
import sorts.hybrid.BranchlessPDQSort;
import sorts.hybrid.CocktailMergeSort;
import sorts.hybrid.EctaSort;
import sorts.hybrid.GrailSort;
import sorts.hybrid.HybridCombSort;
import sorts.hybrid.ImprovedWeaveMergeSort;
import sorts.hybrid.IntroCircleSort;
import sorts.hybrid.IntroSort;
import sorts.hybrid.IterativeIntroCircleSort;
import sorts.hybrid.KotaSort;
import sorts.hybrid.LAQuickSort;
import sorts.hybrid.MedianMergeSort;
import sorts.hybrid.OptimizedBottomUpMergeSort;
import sorts.hybrid.OptimizedDualPivotQuickSort;
import sorts.hybrid.SqrtSort;
import sorts.hybrid.StacklessHybridQuickSort;
import sorts.hybrid.SwapMergeSort;
import sorts.hybrid.TimSort;
import sorts.hybrid.UnstableGrailSort;
import sorts.hybrid.WeaveMergeSort;
import sorts.hybrid.WikiSort;
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
    private Sort BlockSelectionMergeSort;
    private Sort MedianMergeSort;
    private Sort IntroSort;
    private Sort OptimizedBottomUpMergeSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort LAQuickSort;
    private Sort StacklessHybridQuickSort;
    private Sort BranchedPDQSort;
    private Sort BranchlessPDQSort;
    
    public RunHybridSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 25;
        this.categoryCount = this.sortCount;
        
        HybridCombSort              = new              HybridCombSort(this.arrayVisualizer);
        IntroCircleSort             = new             IntroCircleSort(this.arrayVisualizer);
        IterativeIntroCircleSort    = new    IterativeIntroCircleSort(this.arrayVisualizer);
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
        BlockSelectionMergeSort     = new     BlockSelectionMergeSort(this.arrayVisualizer);
        MedianMergeSort             = new             MedianMergeSort(this.arrayVisualizer);
        IntroSort                   = new                   IntroSort(this.arrayVisualizer);
        OptimizedBottomUpMergeSort  = new  OptimizedBottomUpMergeSort(this.arrayVisualizer);
        OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(this.arrayVisualizer);
        LAQuickSort                 = new                 LAQuickSort(this.arrayVisualizer);
        StacklessHybridQuickSort    = new    StacklessHybridQuickSort(this.arrayVisualizer);
        BranchedPDQSort             = new             BranchedPDQSort(this.arrayVisualizer);
        BranchlessPDQSort           = new           BranchlessPDQSort(this.arrayVisualizer);
    }
    
    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunHybridSorts.this.runIndividualSort(HybridCombSort,              0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroCircleSort,             0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IterativeIntroCircleSort,    0, array, 1024, 1.5,  false);
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
        RunHybridSorts.this.runIndividualSort(BlockSelectionMergeSort,     0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(MedianMergeSort,             0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroSort,                   0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedBottomUpMergeSort,  0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(LAQuickSort,                 0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(StacklessHybridQuickSort,    0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(BranchedPDQSort,             0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(BranchlessPDQSort,           0, array, 2048, 0.75, false);
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
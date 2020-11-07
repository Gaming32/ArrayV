package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.hybrid.BinaryMergeSort;
import sorts.hybrid.OptimizedBottomUpMergeSort;
import sorts.hybrid.BranchedPDQSort;
import sorts.hybrid.BranchlessPDQSort;
import sorts.hybrid.CocktailMergeSort;
import sorts.hybrid.GrailSort;
import sorts.hybrid.HybridCombSort;
import sorts.hybrid.IntroCircleSort;
import sorts.hybrid.IntroSort;
import sorts.hybrid.OptimizedDualPivotQuickSort;
import sorts.hybrid.SqrtSort;
import sorts.hybrid.TimSort;
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
    private Sort BinaryMergeSort;
    private Sort WeaveMergeSort;
    private Sort TimSort;
    private Sort CocktailMergeSort;
    private Sort WikiSort;
    private Sort GrailSort;
    private Sort SqrtSort;
    private Sort IntroSort;
    private Sort OptimizedBottomUpMergeSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort BranchedPDQSort;
    private Sort BranchlessPDQSort;
    
    public RunHybridSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;
        
        HybridCombSort              = new              HybridCombSort(this.arrayVisualizer);
        IntroCircleSort             = new             IntroCircleSort(this.arrayVisualizer);
        BinaryMergeSort             = new             BinaryMergeSort(this.arrayVisualizer);
        WeaveMergeSort              = new              WeaveMergeSort(this.arrayVisualizer);
        TimSort                     = new                     TimSort(this.arrayVisualizer);
        CocktailMergeSort           = new           CocktailMergeSort(this.arrayVisualizer);
        WikiSort                    = new                    WikiSort(this.arrayVisualizer);
        GrailSort                   = new                   GrailSort(this.arrayVisualizer);
        SqrtSort                    = new                    SqrtSort(this.arrayVisualizer);
        IntroSort                   = new                   IntroSort(this.arrayVisualizer);
        OptimizedBottomUpMergeSort  = new  OptimizedBottomUpMergeSort(this.arrayVisualizer);
        OptimizedDualPivotQuickSort = new OptimizedDualPivotQuickSort(this.arrayVisualizer);
        BranchedPDQSort             = new             BranchedPDQSort(this.arrayVisualizer);
        BranchlessPDQSort           = new           BranchlessPDQSort(this.arrayVisualizer);
    }
    
    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunHybridSorts.this.runIndividualSort(HybridCombSort,              0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroCircleSort,             0, array, 1024, 1,    false);
        RunHybridSorts.this.runIndividualSort(BinaryMergeSort,             0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(WeaveMergeSort,              0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.65 : 6.5, false);
        RunHybridSorts.this.runIndividualSort(TimSort,                     0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(CocktailMergeSort,           0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(WikiSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(GrailSort,                   0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(SqrtSort,                    0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(IntroSort,                   0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedBottomUpMergeSort,  0, array, 2048, 1,    false);
        RunHybridSorts.this.runIndividualSort(OptimizedDualPivotQuickSort, 0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(BranchedPDQSort,             0, array, 2048, 0.75, false);
        RunHybridSorts.this.runIndividualSort(BranchlessPDQSort,           0, array, 2048, 0.75, false);
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.getSortingThread() != null && arrayVisualizer.getSortingThread().isAlive())
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
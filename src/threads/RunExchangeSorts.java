package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.exchange.*;
import sorts.templates.Sort;
import utils.Shuffles;

/*
 * 
MIT License

Copyright (c) 2021 ArrayV 4.0 Team

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

final public class RunExchangeSorts extends MultipleSortThread {
    private Sort BubbleSort;
    private Sort SmartBubbleSort;
    private Sort CocktailShakerSort;
    private Sort SmartCocktailSort;
    private Sort OddEvenSort;
    private Sort SwapMapSort;
    private Sort OptimizedStoogeSort;
    private Sort GnomeSort;
    private Sort SmartGnomeSort;
    private Sort BinaryGnomeSort;
    private Sort CombSort;
    private Sort ThreeSmoothCombSortRecursive;
    private Sort ThreeSmoothCombSortIterative;
    private Sort CircleSortRecursive;
    private Sort CircleMergeSort;
    private Sort CircleSortIterative;
    private Sort LLQuickSort;
    private Sort LLQuickSortMiddlePivot;
    private Sort LRQuickSort;
    private Sort DualPivotQuickSort;
    private Sort MeanQuickSort;
    private Sort StableQuickSort;
    private Sort StableQuickSortMiddlePivot;
    private Sort ForcedStableQuickSort;
    private Sort LazyStableQuickSort;
    private Sort TableSort;
    private Sort SmarterBubbleSort;
    private Sort SmarterCocktailSort;
    private Sort OptimizedStoogeSortStudio;
    private Sort ooPQuickSort;
    private Sort FunSort;
    private Sort ClassicThreeSmoothCombSort;
    private Sort IndexQuickSort;
    private Sort LRQuickSortParallel;
    private Sort ReverseBubbleSort;
    private Sort ReverseGnomeSort;
    private Sort StableQuickSortParallel;
    private Sort StacklessQuickSort;
    private Sort ThreeSmoothCombSortParallel;
    
    public RunExchangeSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 38;
        this.categoryCount = this.sortCount;
        
        BubbleSort                   = new                   BubbleSort(this.arrayVisualizer);
        SmartBubbleSort              = new              SmartBubbleSort(this.arrayVisualizer);
        SmarterBubbleSort            = new            SmarterBubbleSort(this.arrayVisualizer);
        CocktailShakerSort           = new           CocktailShakerSort(this.arrayVisualizer);
        SmartCocktailSort            = new            SmartCocktailSort(this.arrayVisualizer);
        SmarterCocktailSort          = new          SmarterCocktailSort(this.arrayVisualizer);
        OddEvenSort                  = new                  OddEvenSort(this.arrayVisualizer);
        SwapMapSort                  = new                  SwapMapSort(this.arrayVisualizer);
        OptimizedStoogeSort          = new          OptimizedStoogeSort(this.arrayVisualizer);
        OptimizedStoogeSortStudio    = new    OptimizedStoogeSortStudio(this.arrayVisualizer);
        FunSort                      = new                      FunSort(this.arrayVisualizer);
        GnomeSort                    = new                    GnomeSort(this.arrayVisualizer);
        SmartGnomeSort               = new               SmartGnomeSort(this.arrayVisualizer);
        BinaryGnomeSort              = new              BinaryGnomeSort(this.arrayVisualizer);
        CombSort                     = new                     CombSort(this.arrayVisualizer);
        ThreeSmoothCombSortRecursive = new ThreeSmoothCombSortRecursive(this.arrayVisualizer);
        ThreeSmoothCombSortIterative = new ThreeSmoothCombSortIterative(this.arrayVisualizer);
        CircleSortRecursive          = new          CircleSortRecursive(this.arrayVisualizer);
        CircleSortIterative          = new          CircleSortIterative(this.arrayVisualizer);
        CircleMergeSort              = new              CircleMergeSort(this.arrayVisualizer);
        LLQuickSort                  = new                  LLQuickSort(this.arrayVisualizer);
        LLQuickSortMiddlePivot       = new       LLQuickSortMiddlePivot(this.arrayVisualizer);
        LRQuickSort                  = new                  LRQuickSort(this.arrayVisualizer);
        DualPivotQuickSort           = new           DualPivotQuickSort(this.arrayVisualizer);
        MeanQuickSort                = new                MeanQuickSort(this.arrayVisualizer);
        StableQuickSort              = new              StableQuickSort(this.arrayVisualizer);
        StableQuickSortMiddlePivot   = new   StableQuickSortMiddlePivot(this.arrayVisualizer);
        ooPQuickSort                 = new                 ooPQuicksort(this.arrayVisualizer);
        ForcedStableQuickSort        = new        ForcedStableQuickSort(this.arrayVisualizer);
        LazyStableQuickSort          = new          LazyStableQuickSort(this.arrayVisualizer);
        TableSort                    = new                    TableSort(this.arrayVisualizer);
        ClassicThreeSmoothCombSort   = new   ClassicThreeSmoothCombSort(this.arrayVisualizer);
        IndexQuickSort               = new               IndexQuickSort(this.arrayVisualizer);
        LRQuickSortParallel          = new          LRQuickSortParallel(this.arrayVisualizer);
        ReverseBubbleSort            = new          LRQuickSortParallel(this.arrayVisualizer);
        ReverseGnomeSort             = new             ReverseGnomeSort(this.arrayVisualizer);
        StableQuickSortParallel      = new      StableQuickSortParallel(this.arrayVisualizer);
        StacklessQuickSort           = new           StacklessQuickSort(this.arrayVisualizer);
        ThreeSmoothCombSortParallel  = new  ThreeSmoothCombSortParallel(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunExchangeSorts.this.runIndividualSort(BubbleSort,                   0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(SmartBubbleSort,              0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(SmarterBubbleSort,            0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(ReverseBubbleSort,            0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(CocktailShakerSort,           0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(SmartCocktailSort,            0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(SmarterCocktailSort,          0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(OddEvenSort,                  0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(SwapMapSort,                  0,   array,  512, 0.125, false);
        RunExchangeSorts.this.runIndividualSort(OptimizedStoogeSort,          0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(OptimizedStoogeSortStudio,    0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(FunSort,                      0,   array,  256, 2,     false);
        RunExchangeSorts.this.runIndividualSort(GnomeSort,                    0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(SmartGnomeSort,               0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(BinaryGnomeSort,              0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(ReverseGnomeSort,             0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(CombSort,                     130, array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortRecursive, 0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortParallel,  0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortIterative, 0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ClassicThreeSmoothCombSort,   0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(CircleSortRecursive,          0,   array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(CircleSortIterative,          0,   array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(CircleMergeSort,              0,   array, 1024, 0.75,  false);
        RunExchangeSorts.this.runIndividualSort(LLQuickSort,                  0,   array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.5 : 5, false);
        RunExchangeSorts.this.runIndividualSort(LLQuickSortMiddlePivot,       0,   array, 2048, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(LRQuickSort,                  0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(LRQuickSortParallel,          0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(DualPivotQuickSort,           0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(StacklessQuickSort,           0,   array, 2048, 1,     false);
        // RunExchangeSorts.this.runIndividualSort(MeanQuickSort,                0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSort,              0,   array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1 : 6.5,    false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSortMiddlePivot,   0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(ooPQuickSort,                 0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSortParallel,      0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(ForcedStableQuickSort,        0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(LazyStableQuickSort,          0,   array, 256,  0.5,   false);
        RunExchangeSorts.this.runIndividualSort(TableSort,                    0,   array, 1024, 0.75,  false);
        RunExchangeSorts.this.runIndividualSort(IndexQuickSort,               0,   array, 1024, 0.75,  false);
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
                        RunExchangeSorts.this.sortNumber = current;
                        RunExchangeSorts.this.sortCount = total;
                    }
                    else {
                        RunExchangeSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Exchange Sorts");

                    RunExchangeSorts.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Run Exchange Sorts");
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
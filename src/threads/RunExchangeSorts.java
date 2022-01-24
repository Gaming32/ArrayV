package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.exchange.BinaryGnomeSort;
import sorts.exchange.BubbleSort;
import sorts.exchange.CircleSortIterative;
import sorts.exchange.CircleSortRecursive;
import sorts.exchange.ClassicThreeSmoothCombSort;
import sorts.exchange.CocktailShakerSort;
import sorts.exchange.CombSort;
import sorts.exchange.DualPivotQuickSort;
import sorts.exchange.ForcedStableQuickSort;
import sorts.exchange.FunSort;
import sorts.exchange.GnomeSort;
import sorts.exchange.LLQuickSort;
import sorts.exchange.LRQuickSort;
import sorts.exchange.LRQuickSortParallel;
import sorts.exchange.OddEvenSort;
import sorts.exchange.OptimizedBubbleSort;
import sorts.exchange.OptimizedCocktailShakerSort;
import sorts.exchange.OptimizedGnomeSort;
import sorts.exchange.OptimizedStoogeSort;
import sorts.exchange.OptimizedStoogeSortStudio;
import sorts.exchange.SlopeSort;
import sorts.exchange.StableQuickSort;
import sorts.exchange.StableQuickSortParallel;
import sorts.exchange.TableSort;
import sorts.exchange.ThreeSmoothCombSortIterative;
import sorts.exchange.ThreeSmoothCombSortParallel;
import sorts.exchange.ThreeSmoothCombSortRecursive;
import sorts.exchange.UnoptimizedBubbleSort;
import sorts.exchange.UnoptimizedCocktailShakerSort;
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

final public class RunExchangeSorts extends MultipleSortThread {
    private Sort UnoptimizedBubbleSort;
    private Sort BubbleSort;
    private Sort UnoptimizedCocktailShakerSort;
    private Sort CocktailShakerSort;
    private Sort OddEvenSort;
    private Sort OptimizedStoogeSort;
    private Sort GnomeSort;
    private Sort OptimizedGnomeSort;
    private Sort BinaryGnomeSort;
    private Sort CombSort;
    private Sort ThreeSmoothCombSortRecursive;
    private Sort ThreeSmoothCombSortIterative;
    private Sort CircleSortRecursive;
    private Sort CircleSortIterative;
    private Sort LLQuickSort;
    private Sort LRQuickSort;
    private Sort DualPivotQuickSort;
    private Sort StableQuickSort;
    private Sort ForcedStableQuickSort;
    private Sort TableSort;
    private Sort OptimizedBubbleSort;
    private Sort OptimizedCocktailShakerSort;
    private Sort OptimizedStoogeSortStudio;
    private Sort FunSort;
    private Sort ClassicThreeSmoothCombSort;
    private Sort LRQuickSortParallel;
    private Sort StableQuickSortParallel;
    private Sort ThreeSmoothCombSortParallel;
    private Sort SlopeSort;

    public RunExchangeSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 29;
        this.categoryCount = this.sortCount;

        UnoptimizedBubbleSort         = new         UnoptimizedBubbleSort(this.arrayVisualizer);
        BubbleSort                    = new                    BubbleSort(this.arrayVisualizer);
        OptimizedBubbleSort           = new           OptimizedBubbleSort(this.arrayVisualizer);
        UnoptimizedCocktailShakerSort = new UnoptimizedCocktailShakerSort(this.arrayVisualizer);
        CocktailShakerSort            = new            CocktailShakerSort(this.arrayVisualizer);
        OptimizedCocktailShakerSort   = new   OptimizedCocktailShakerSort(this.arrayVisualizer);
        OddEvenSort                   = new                   OddEvenSort(this.arrayVisualizer);
        OptimizedStoogeSort           = new           OptimizedStoogeSort(this.arrayVisualizer);
        OptimizedStoogeSortStudio     = new     OptimizedStoogeSortStudio(this.arrayVisualizer);
        FunSort                       = new                       FunSort(this.arrayVisualizer);
        GnomeSort                     = new                     GnomeSort(this.arrayVisualizer);
        OptimizedGnomeSort            = new            OptimizedGnomeSort(this.arrayVisualizer);
        BinaryGnomeSort               = new               BinaryGnomeSort(this.arrayVisualizer);
        CombSort                      = new                      CombSort(this.arrayVisualizer);
        ThreeSmoothCombSortRecursive  = new  ThreeSmoothCombSortRecursive(this.arrayVisualizer);
        ThreeSmoothCombSortIterative  = new  ThreeSmoothCombSortIterative(this.arrayVisualizer);
        CircleSortRecursive           = new           CircleSortRecursive(this.arrayVisualizer);
        CircleSortIterative           = new           CircleSortIterative(this.arrayVisualizer);
        LLQuickSort                   = new                   LLQuickSort(this.arrayVisualizer);
        LRQuickSort                   = new                   LRQuickSort(this.arrayVisualizer);
        DualPivotQuickSort            = new            DualPivotQuickSort(this.arrayVisualizer);
        StableQuickSort               = new               StableQuickSort(this.arrayVisualizer);
        ForcedStableQuickSort         = new         ForcedStableQuickSort(this.arrayVisualizer);
        TableSort                     = new                     TableSort(this.arrayVisualizer);
        ClassicThreeSmoothCombSort    = new    ClassicThreeSmoothCombSort(this.arrayVisualizer);
        LRQuickSortParallel           = new           LRQuickSortParallel(this.arrayVisualizer);
        StableQuickSortParallel       = new       StableQuickSortParallel(this.arrayVisualizer);
        ThreeSmoothCombSortParallel   = new   ThreeSmoothCombSortParallel(this.arrayVisualizer);
        SlopeSort                     = new                     SlopeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunExchangeSorts.this.runIndividualSort(UnoptimizedBubbleSort,         0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(BubbleSort,                    0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(OptimizedBubbleSort,           0,   array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(UnoptimizedCocktailShakerSort, 0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(CocktailShakerSort,            0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(OptimizedCocktailShakerSort,   0,   array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(OddEvenSort,                   0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(OptimizedStoogeSort,           0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(OptimizedStoogeSortStudio,     0,   array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(FunSort,                       0,   array,  256, 2,     false);
        RunExchangeSorts.this.runIndividualSort(GnomeSort,                     0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(OptimizedGnomeSort,            0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(BinaryGnomeSort,               0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(SlopeSort,                     0,   array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(CombSort,                      130, array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortRecursive,  0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortParallel,   0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ThreeSmoothCombSortIterative,  0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(ClassicThreeSmoothCombSort,    0,   array, 1024, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(CircleSortRecursive,           0,   array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(CircleSortIterative,           0,   array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(LLQuickSort,                   0,   array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5, false);
        RunExchangeSorts.this.runIndividualSort(LRQuickSort,                   0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(LRQuickSortParallel,           0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(DualPivotQuickSort,            0,   array, 2048, 1,     false);
        // RunExchangeSorts.this.runIndividualSort(MeanQuickSort,                0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSort,               0,   array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5,    false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSortParallel,       0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(ForcedStableQuickSort,         0,   array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(TableSort,                     0,   array, 1024, 0.75,  false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("ExchangeSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunExchangeSorts.this.sortNumber = current;
                        RunExchangeSorts.this.sortCount = total;
                    } else {
                        RunExchangeSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Exchange Sorts");

                    RunExchangeSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Exchange Sorts");
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

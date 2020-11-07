package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.exchange.BinaryGnomeSort;
import sorts.exchange.BubbleSort;
import sorts.exchange.CircleSort;
import sorts.exchange.CocktailShakerSort;
import sorts.exchange.CombSort;
import sorts.exchange.DualPivotQuickSort;
import sorts.exchange.GnomeSort;
import sorts.exchange.LLQuickSort;
import sorts.exchange.LRQuickSort;
import sorts.exchange.OddEvenSort;
import sorts.exchange.SmartBubbleSort;
import sorts.exchange.SmartCocktailSort;
import sorts.exchange.SmartGnomeSort;
import sorts.exchange.StableQuickSort;
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

final public class RunExchangeSorts extends MultipleSortThread {
    private Sort BubbleSort;
    private Sort SmartBubbleSort;
    private Sort CocktailShakerSort;
    private Sort SmartCocktailSort;
    private Sort OddEvenSort;
    private Sort GnomeSort;
    private Sort SmartGnomeSort;
    private Sort BinaryGnomeSort;
    private Sort CombSort;
    private Sort CircleSort;
    private Sort LLQuickSort;
    private Sort LRQuickSort;
    private Sort DualPivotQuickSort;
    private Sort StableQuickSort;
    
    public RunExchangeSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 14;
        this.categoryCount = this.sortCount;
        
        BubbleSort         = new         BubbleSort(this.arrayVisualizer);
        SmartBubbleSort    = new    SmartBubbleSort(this.arrayVisualizer);
        CocktailShakerSort = new CocktailShakerSort(this.arrayVisualizer);
        SmartCocktailSort  = new  SmartCocktailSort(this.arrayVisualizer);
        OddEvenSort        = new        OddEvenSort(this.arrayVisualizer);
        GnomeSort          = new          GnomeSort(this.arrayVisualizer);
        SmartGnomeSort     = new     SmartGnomeSort(this.arrayVisualizer);
        BinaryGnomeSort    = new    BinaryGnomeSort(this.arrayVisualizer);
        CombSort           = new           CombSort(this.arrayVisualizer);
        CircleSort         = new         CircleSort(this.arrayVisualizer);
        LLQuickSort        = new        LLQuickSort(this.arrayVisualizer);
        LRQuickSort        = new        LRQuickSort(this.arrayVisualizer);
        DualPivotQuickSort = new DualPivotQuickSort(this.arrayVisualizer);
        StableQuickSort    = new    StableQuickSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunExchangeSorts.this.runIndividualSort(BubbleSort,         0, array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(SmartBubbleSort,    0, array,  512, 1.5,   false);
        RunExchangeSorts.this.runIndividualSort(CocktailShakerSort, 0, array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(SmartCocktailSort,  0, array,  512, 1.25,  false);
        RunExchangeSorts.this.runIndividualSort(OddEvenSort,        0, array,  512, 1,     false);
        RunExchangeSorts.this.runIndividualSort(GnomeSort,          0, array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(SmartGnomeSort,     0, array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(BinaryGnomeSort,    0, array,  128, 0.025, false);
        RunExchangeSorts.this.runIndividualSort(CombSort,           0, array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(CircleSort,         0, array, 1024, 1,     false);
        RunExchangeSorts.this.runIndividualSort(LLQuickSort,        0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1.5 : 5, false);
        RunExchangeSorts.this.runIndividualSort(LRQuickSort,        0, array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(DualPivotQuickSort, 0, array, 2048, 1,     false);
        RunExchangeSorts.this.runIndividualSort(StableQuickSort,    0, array, 2048, arrayManager.getShuffle() == Shuffles.RANDOM ? 1 : 6.5,    false);
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
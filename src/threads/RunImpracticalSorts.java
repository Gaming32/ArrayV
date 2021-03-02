package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.distribute.BogoBogoSort;
import sorts.distribute.BogoSort;
import sorts.distribute.CocktailBogoSort;
import sorts.distribute.LessBogoSort;
import sorts.distribute.SliceBogoSort;
import sorts.distribute.SmartBogoSort;
import sorts.exchange.BubbleBogoSort;
import sorts.exchange.ExchangeBogoSort;
import sorts.exchange.SillySort;
import sorts.exchange.SlowSort;
import sorts.exchange.SnuffleSort;
import sorts.exchange.StoogeSort;
import sorts.select.BadSort;
import sorts.distribute.GuessSort;
import sorts.exchange.MarkovSort;
import sorts.exchange.NapoleonSort;
import sorts.insert.HanoiSort;
import sorts.insert.StableHanoiSort;
import sorts.exchange.StableStoogeSort;
import sorts.templates.Sort;

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

final public class RunImpracticalSorts extends MultipleSortThread {
    private Sort BadSort;
    private Sort StoogeSort;
    private Sort SillySort;
    private Sort SlowSort;
    private Sort SnuffleSort;
    private Sort ExchangeBogoSort;
    private Sort BubbleBogoSort;
    private Sort LessBogoSort;
    private Sort CocktailBogoSort;
    private Sort SmartBogoSort;
    private Sort SliceBogoSort;
    private Sort BogoSort;
    private Sort BogoBogoSort;
    private Sort MarkovSort;
    private Sort GuessSort;
    private Sort NapoleonSort;
    private Sort HanoiSort;
    private Sort StableHanoiSort;
    private Sort StableStoogeSort;

    public RunImpracticalSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 18;
        this.categoryCount = this.sortCount;

        BadSort             = new             BadSort(this.arrayVisualizer);
        //MarkovSort          = new          MarkovSort(this.arrayVisualizer);
        StoogeSort          = new          StoogeSort(this.arrayVisualizer);
        StableStoogeSort    = new    StableStoogeSort(this.arrayVisualizer);
        SillySort           = new           SillySort(this.arrayVisualizer);
        SlowSort            = new            SlowSort(this.arrayVisualizer);
        SnuffleSort         = new         SnuffleSort(this.arrayVisualizer);
        GuessSort           = new           GuessSort(this.arrayVisualizer);
        HanoiSort           = new           HanoiSort(this.arrayVisualizer);
        StableHanoiSort     = new     StableHanoiSort(this.arrayVisualizer);
        NapoleonSort        = new        NapoleonSort(this.arrayVisualizer);
        ExchangeBogoSort    = new    ExchangeBogoSort(this.arrayVisualizer);
        BubbleBogoSort      = new      BubbleBogoSort(this.arrayVisualizer);
        LessBogoSort        = new        LessBogoSort(this.arrayVisualizer);
        CocktailBogoSort    = new    CocktailBogoSort(this.arrayVisualizer);
        SmartBogoSort       = new       SmartBogoSort(this.arrayVisualizer);
        SliceBogoSort       = new       SliceBogoSort(this.arrayVisualizer);
        BogoSort            = new            BogoSort(this.arrayVisualizer);
        BogoBogoSort        = new        BogoBogoSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunImpracticalSorts.this.runIndividualSort(BadSort,             0, array, 64,  0.0075, true);
        //RunImpracticalSorts.this.runIndividualSort(MarkovSort,          0, array, 64,  0.2,    true);
        RunImpracticalSorts.this.runIndividualSort(StoogeSort,          0, array, 64,  0.005,  true);
        RunImpracticalSorts.this.runIndividualSort(StableStoogeSort,    0, array, 64,  0.005,  true);
        RunImpracticalSorts.this.runIndividualSort(SillySort,           0, array, 64,  0.5,    true);
        RunImpracticalSorts.this.runIndividualSort(SlowSort,            0, array, 64,  0.5,    true);
        RunImpracticalSorts.this.runIndividualSort(SnuffleSort,         0, array, 64,  0.25,   true);
        RunImpracticalSorts.this.runIndividualSort(GuessSort,           0, array, 6,   5,      true);
        RunImpracticalSorts.this.runIndividualSort(HanoiSort,           0, array, 8,   0.025,  true);
        RunImpracticalSorts.this.runIndividualSort(StableHanoiSort,     0, array, 8,   0.025,  true);
        RunImpracticalSorts.this.runIndividualSort(NapoleonSort,        0, array, 6,   0.005,  true);
        
        Sounds.toggleSofterSounds(true);
        RunImpracticalSorts.this.runIndividualSort(ExchangeBogoSort,    0, array, 32,  0.01,   true);
        RunImpracticalSorts.this.runIndividualSort(BubbleBogoSort,      0, array, 32,  0.01,   true);
        RunImpracticalSorts.this.runIndividualSort(LessBogoSort,        0, array, 16,  0.0025, true);
        RunImpracticalSorts.this.runIndividualSort(CocktailBogoSort,    0, array, 16,  0.0025, true);
        RunImpracticalSorts.this.runIndividualSort(SmartBogoSort,       0, array, 10,  1,      true);
        RunImpracticalSorts.this.runIndividualSort(SliceBogoSort,       0, array,  8,  1,      true);
        RunImpracticalSorts.this.runIndividualSort(BogoSort,            0, array,  8,  1,      true);
        RunImpracticalSorts.this.runIndividualSort(BogoBogoSort,        0, array,  6,  1,      true);
        Sounds.toggleSofterSounds(false);
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
                        RunImpracticalSorts.this.sortNumber = current;
                        RunImpracticalSorts.this.sortCount = total;
                    }
                    else {
                        RunImpracticalSorts.this.sortNumber = 1;
                    }
                    
                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Impractical Sorts");

                    RunImpracticalSorts.this.executeSortList(array);
                    
                    if(runAllActive) {
                        Thread.sleep(3000);
                    }
                    else {
                        arrayVisualizer.setCategory("Run Impractical Sorts");
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
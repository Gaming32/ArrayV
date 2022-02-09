package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.distribute.BogoBogoSort;
import sorts.distribute.BogoSort;
import sorts.distribute.BozoSort;
import sorts.distribute.CocktailBogoSort;
import sorts.distribute.DeterministicBogoSort;
import sorts.distribute.GuessSort;
import sorts.distribute.LessBogoSort;
import sorts.distribute.MedianQuickBogoSort;
import sorts.distribute.MergeBogoSort;
import sorts.distribute.OptimizedGuessSort;
import sorts.distribute.QuickBogoSort;
import sorts.distribute.RandomGuessSort;
import sorts.distribute.SelectionBogoSort;
import sorts.distribute.SmartBogoBogoSort;
import sorts.distribute.SmartGuessSort;
import sorts.exchange.BubbleBogoSort;
import sorts.exchange.ExchangeBogoSort;
import sorts.exchange.QuadStoogeSort;
import sorts.exchange.SillySort;
import sorts.exchange.SlowSort;
import sorts.exchange.SnuffleSort;
import sorts.exchange.StoogeSort;
import sorts.insert.HanoiSort;
import sorts.select.BadSort;
import sorts.templates.Sort;

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

final public class RunImpracticalSorts extends MultipleSortThread {
    private Sort BadSort;
    private Sort StoogeSort;
    private Sort SillySort;
    private Sort SlowSort;
    private Sort SnuffleSort;
    private Sort HanoiSort;
    private Sort SelectionBogoSort;
    private Sort BubbleBogoSort;
    private Sort CocktailBogoSort;
    private Sort LessBogoSort;
    private Sort ExchangeBogoSort;
    private Sort MedianQuickBogoSort;
    private Sort QuickBogoSort;
    private Sort MergeBogoSort;
    private Sort SmartGuessSort;
    private Sort BozoSort;
    private Sort DeterministicBogoSort;
    private Sort SmartBogoBogoSort;
    private Sort BogoSort;
    private Sort OptimizedGuessSort;
    private Sort RandomGuessSort;
    private Sort GuessSort;
    private Sort BogoBogoSort;
    private Sort QuadStoogeSort;

    public RunImpracticalSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 24;
        this.categoryCount = this.sortCount;

        BadSort                   = new                   BadSort(this.arrayVisualizer);
        StoogeSort                = new                StoogeSort(this.arrayVisualizer);
        SillySort                 = new                 SillySort(this.arrayVisualizer);
        SlowSort                  = new                  SlowSort(this.arrayVisualizer);
        SnuffleSort               = new               SnuffleSort(this.arrayVisualizer);
        HanoiSort                 = new                 HanoiSort(this.arrayVisualizer);
        SelectionBogoSort         = new         SelectionBogoSort(this.arrayVisualizer);
        BubbleBogoSort            = new            BubbleBogoSort(this.arrayVisualizer);
        CocktailBogoSort          = new          CocktailBogoSort(this.arrayVisualizer);
        LessBogoSort              = new              LessBogoSort(this.arrayVisualizer);
        ExchangeBogoSort          = new          ExchangeBogoSort(this.arrayVisualizer);
        MedianQuickBogoSort       = new       MedianQuickBogoSort(this.arrayVisualizer);
        QuickBogoSort             = new             QuickBogoSort(this.arrayVisualizer);
        MergeBogoSort             = new             MergeBogoSort(this.arrayVisualizer);
        SmartGuessSort            = new            SmartGuessSort(this.arrayVisualizer);
        BozoSort                  = new                  BozoSort(this.arrayVisualizer);
        DeterministicBogoSort     = new     DeterministicBogoSort(this.arrayVisualizer);
        SmartBogoBogoSort         = new         SmartBogoBogoSort(this.arrayVisualizer);
        BogoSort                  = new                  BogoSort(this.arrayVisualizer);
        OptimizedGuessSort        = new        OptimizedGuessSort(this.arrayVisualizer);
        RandomGuessSort           = new           RandomGuessSort(this.arrayVisualizer);
        GuessSort                 = new                 GuessSort(this.arrayVisualizer);
        BogoBogoSort              = new              BogoBogoSort(this.arrayVisualizer);
        QuadStoogeSort            = new            QuadStoogeSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunImpracticalSorts.this.runIndividualSort(BadSort,                   0, array, 64,  0.0075, true);
        RunImpracticalSorts.this.runIndividualSort(StoogeSort,                0, array, 64,  0.005,  true);
        RunImpracticalSorts.this.runIndividualSort(QuadStoogeSort,            0, array, 64,  0.005,  true);
        RunImpracticalSorts.this.runIndividualSort(SillySort,                 0, array, 64,  0.5,    true);
        RunImpracticalSorts.this.runIndividualSort(SlowSort,                  0, array, 64,  0.5,    true);
        RunImpracticalSorts.this.runIndividualSort(SnuffleSort,               0, array, 64,  0.25,   true);
        RunImpracticalSorts.this.runIndividualSort(HanoiSort,                 0, array, 8,   0.025,  true);

        // Bogosorts
        Sounds.toggleSofterSounds(true);
        // the not-bad ones
        RunImpracticalSorts.this.runIndividualSort(SelectionBogoSort,         0, array, 64,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(BubbleBogoSort,            0, array, 40,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(CocktailBogoSort,          0, array, 40,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(LessBogoSort,              0, array, 32,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(ExchangeBogoSort,          0, array, 28,  1e-9,   true);
        // the meh ones
        RunImpracticalSorts.this.runIndividualSort(MedianQuickBogoSort,       0, array, 12,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(QuickBogoSort,             0, array,  9,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(MergeBogoSort,             0, array,  9,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(SmartGuessSort,            0, array,  8,  1e-9,   true);
        // the scary ones
        RunImpracticalSorts.this.runIndividualSort(BozoSort,                  0, array,  7,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(DeterministicBogoSort,     0, array,  7,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(SmartBogoBogoSort,         0, array,  6,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(BogoSort,                  0, array,  6,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(OptimizedGuessSort,        0, array,  5,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(RandomGuessSort,           0, array,  5,  1e-9,   true);
        RunImpracticalSorts.this.runIndividualSort(GuessSort,                 0, array,  4,  1e-9,   true);
        // aaaaa
        RunImpracticalSorts.this.runIndividualSort(BogoBogoSort,              0, array,  4,  1e-9,   true);
        Sounds.toggleSofterSounds(false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("ImpracticalSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunImpracticalSorts.this.sortNumber = current;
                        RunImpracticalSorts.this.sortCount = total;
                    } else {
                        RunImpracticalSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Impractical Sorts");

                    RunImpracticalSorts.this.executeSortList(array);

                    if (runAllActive) {
                        Thread.sleep(3000);
                    } else {
                        arrayVisualizer.setCategory("Run Impractical Sorts");
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
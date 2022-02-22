import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Impractical Sorts', 32) {
    run BadSort with 64.numbers and 0.0075.delay
    run StoogeSort with 64.numbers and 0.005.delay
    run QuadStoogeSort with 64.numbers and 0.005.delay
    run SillySort with 64.numbers and 0.5.delay
    run SlowSort with 64.numbers and 0.5.delay
    run SnuffleSort with 64.numbers and 0.25.delay
    run HanoiSort with 8.numbers and 0.025.delay

    // Bogosorts
    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    // The not-bad ones
    run SelectionBogoSort with 64.numbers and 1e-9.delay
    run BubbleBogoSort with 40.numbers and 1e-9.delay
    run CocktailBogoSort with 40.numbers and 1e-9.delay
    run LessBogoSort with 32.numbers and 1e-9.delay
    run ExchangeBogoSort with 28.numbers and 1e-9.delay
    // The meh ones
    run MedianQuickBogoSort with 12.numbers and 1e-9.delay
    run QuickBogoSort with 9.numbers and 1e-9.delay
    run MergeBogoSort with 9.numbers and 1e-9.delay
    run SmartGuessSort with 8.numbers and 1e-9.delay
    // The scary ones
    run BozoSort with 7.numbers and 1e-9.delay
    run DeterministicBogoSort with 7.numbers and 1e-9.delay
    run SmartBogoBogoSort with 6.numbers and 1e-9.delay
    run BogoSort with 6.numbers and 1e-9.delay
    run OptimizedGuessSort with 5.numbers and 1e-9.delay
    run RandomGuessSort with 5.numbers and 1e-9.delay
    run GuessSort with 4.numbers and 1e-9.delay
    // aaaaaa
    run BogoBogoSort with 4.numbers and 1e-9.delay
    arrayv.sounds.softerSounds = oldSofterSounds
}

import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Impractical Sorts', 32) {
    run BadSort with 64.numbers and 0.0075.speed
    run StoogeSort with 64.numbers and 0.005.speed
    run QuadStoogeSort with 64.numbers and 0.005.speed
    run SillySort with 64.numbers and 0.5.speed
    run SlowSort with 64.numbers and 0.5.speed
    run SnuffleSort with 64.numbers and 0.25.speed
    run HanoiSort with 8.numbers and 0.025.speed

    // Bogosorts
    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    // The not-bad ones
    run SelectionBogoSort with 64.numbers and 1e-9.speed
    run BubbleBogoSort with 40.numbers and 1e-9.speed
    run CocktailBogoSort with 40.numbers and 1e-9.speed
    run LessBogoSort with 32.numbers and 1e-9.speed
    run ExchangeBogoSort with 28.numbers and 1e-9.speed
    // The meh ones
    run MedianQuickBogoSort with 12.numbers and 1e-9.speed
    run QuickBogoSort with 9.numbers and 1e-9.speed
    run MergeBogoSort with 9.numbers and 1e-9.speed
    run SmartGuessSort with 8.numbers and 1e-9.speed
    // The scary ones
    run BozoSort with 7.numbers and 1e-9.speed
    run DeterministicBogoSort with 7.numbers and 1e-9.speed
    run SmartBogoBogoSort with 6.numbers and 1e-9.speed
    run BogoSort with 6.numbers and 1e-9.speed
    run OptimizedGuessSort with 5.numbers and 1e-9.speed
    run RandomGuessSort with 5.numbers and 1e-9.speed
    run GuessSort with 4.numbers and 1e-9.speed
    // aaaaaa
    run BogoBogoSort with 4.numbers and 1e-9.speed
    arrayv.sounds.softerSounds = oldSofterSounds
}

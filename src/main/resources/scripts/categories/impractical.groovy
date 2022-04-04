import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Impractical Sorts', 32) {
    run BadSort go 64.numbers, 0.0075.speed
    run StoogeSort go 64.numbers, 0.005.speed
    run QuadStoogeSort go 64.numbers, 0.005.speed
    run SillySort go 64.numbers, 0.5.speed
    run SlowSort go 64.numbers, 0.5.speed
    run SnuffleSort go 64.numbers, 0.25.speed
    run HanoiSort go 8.numbers, 0.025.speed

    // Bogosorts
    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    // The not-bad ones
    run SelectionBogoSort go 64.numbers, 1e-9.speed
    run BubbleBogoSort go 40.numbers, 1e-9.speed
    run CocktailBogoSort go 40.numbers, 1e-9.speed
    run LessBogoSort go 32.numbers, 1e-9.speed
    run ExchangeBogoSort go 28.numbers, 1e-9.speed
    // The meh ones
    run MedianQuickBogoSort go 12.numbers, 1e-9.speed
    run QuickBogoSort go 9.numbers, 1e-9.speed
    run MergeBogoSort go 9.numbers, 1e-9.speed
    run SmartGuessSort go 8.numbers, 1e-9.speed
    // The scary ones
    run BozoSort go 7.numbers, 1e-9.speed
    run DeterministicBogoSort go 7.numbers, 1e-9.speed
    run SmartBogoBogoSort go 6.numbers, 1e-9.speed
    run BogoSort go 6.numbers, 1e-9.speed
    run OptimizedGuessSort go 5.numbers, 1e-9.speed
    run RandomGuessSort go 5.numbers, 1e-9.speed
    run GuessSort go 4.numbers, 1e-9.speed
    // aaaaaa
    run BogoBogoSort go 4.numbers, 1e-9.speed
    arrayv.sounds.softerSounds = oldSofterSounds
}

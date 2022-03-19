import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Exchange Sorts', 29) {
    run UnoptimizedBubbleSort with 512.numbers and 1.5.speed
    run BubbleSort with 512.numbers and 1.5.speed
    run OptimizedBubbleSort with 512.numbers and 1.5.speed
    run UnoptimizedCocktailShakerSort with 512.numbers and 1.25.speed
    run CocktailShakerSort with 512.numbers and 1.25.speed
    run OptimizedCocktailShakerSort with 512.numbers and 1.25.speed
    run OddEvenSort with 512.numbers run()
    run OptimizedStoogeSort with 512.numbers run()
    run OptimizedStoogeSortStudio with 512.numbers run()
    run FunSort with 256.numbers and 2.speed
    run GnomeSort with 128.numbers and 0.025.speed
    run OptimizedGnomeSort with 128.numbers and 0.025.speed
    run BinaryGnomeSort with 128.numbers and 0.025.speed
    run SlopeSort with 128.numbers and 0.025.speed
    run CombSort with 1024.numbers and 130.buckets
    run ThreeSmoothCombSortRecursive with 1024.numbers and 1.25.speed
    run ThreeSmoothCombSortParallel with 1024.numbers and 1.25.speed
    run ThreeSmoothCombSortIterative with 1024.numbers and 1.25.speed
    run ClassicThreeSmoothCombSort with 1024.numbers and 1.25.speed
    run CircleSortRecursive with 1024.numbers run()
    run CircleSortIterative with 1024.numbers run()
    run LLQuickSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5).speed)
    run LRQuickSort with 2048.numbers run()
    run LRQuickSortParallel with 2048.numbers run()
    run DualPivotQuickSort with 2048.numbers run()
    run StableQuickSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5).speed)
    run StableQuickSortParallel with 2048.numbers run()
    run ForcedStableQuickSort with 2048.numbers run()
    run TableSort with 1024.numbers and 0.75.speed
}

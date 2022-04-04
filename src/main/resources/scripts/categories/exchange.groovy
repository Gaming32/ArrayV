import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Exchange Sorts', 29) {
    run UnoptimizedBubbleSort go 512.numbers, 1.5.speed
    run BubbleSort go 512.numbers, 1.5.speed
    run OptimizedBubbleSort go 512.numbers, 1.5.speed
    run UnoptimizedCocktailShakerSort go 512.numbers, 1.25.speed
    run CocktailShakerSort go 512.numbers, 1.25.speed
    run OptimizedCocktailShakerSort go 512.numbers, 1.25.speed
    run OddEvenSort go 512.numbers
    run OptimizedStoogeSort go 512.numbers
    run OptimizedStoogeSortStudio go 512.numbers
    run FunSort go 256.numbers, 2.speed
    run GnomeSort go 128.numbers, 0.025.speed
    run OptimizedGnomeSort go 128.numbers, 0.025.speed
    run BinaryGnomeSort go 128.numbers, 0.025.speed
    run SlopeSort go 128.numbers, 0.025.speed
    run CombSort go 1024.numbers, 130.buckets
    run ThreeSmoothCombSortRecursive go 1024.numbers, 1.25.speed
    run ThreeSmoothCombSortParallel go 1024.numbers, 1.25.speed
    run ThreeSmoothCombSortIterative go 1024.numbers, 1.25.speed
    run ClassicThreeSmoothCombSort go 1024.numbers, 1.25.speed
    run CircleSortRecursive go 1024.numbers
    run CircleSortIterative go 1024.numbers
    run LLQuickSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5).speed)
    run LRQuickSort go 2048.numbers
    run LRQuickSortParallel go 2048.numbers
    run DualPivotQuickSort go 2048.numbers
    run StableQuickSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5).speed)
    run StableQuickSortParallel go 2048.numbers
    run ForcedStableQuickSort go 2048.numbers
    run TableSort go 1024.numbers, 0.75.speed
}

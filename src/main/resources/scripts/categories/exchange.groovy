import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Exchange Sorts', 29) {
    run UnoptimizedBubbleSort with 512.numbers and 1.5.delay
    run BubbleSort with 512.numbers and 1.5.delay
    run OptimizedBubbleSort with 512.numbers and 1.5.delay
    run UnoptimizedCocktailShakerSort with 512.numbers and 1.25.delay
    run CocktailShakerSort with 512.numbers and 1.25.delay
    run OptimizedCocktailShakerSort with 512.numbers and 1.25.delay
    run OddEvenSort with 512.numbers go()
    run OptimizedStoogeSort with 512.numbers go()
    run OptimizedStoogeSortStudio with 512.numbers go()
    run FunSort with 256.numbers and 2.delay
    run GnomeSort with 128.numbers and 0.025.delay
    run OptimizedGnomeSort with 128.numbers and 0.025.delay
    run BinaryGnomeSort with 128.numbers and 0.025.delay
    run SlopeSort with 128.numbers and 0.025.delay
    run CombSort with 1024.numbers and 130.buckets
    run ThreeSmoothCombSortRecursive with 1024.numbers and 1.25.delay
    run ThreeSmoothCombSortParallel with 1024.numbers and 1.25.delay
    run ThreeSmoothCombSortIterative with 1024.numbers and 1.25.delay
    run ClassicThreeSmoothCombSort with 1024.numbers and 1.25.delay
    run CircleSortRecursive with 1024.numbers go()
    run CircleSortIterative with 1024.numbers go()
    run(LLQuickSort, numbers: 2048, delay: arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5).go()
    run LRQuickSort with 2048.numbers go()
    run LRQuickSortParallel with 2048.numbers go()
    run DualPivotQuickSort with 2048.numbers go()
    run(StableQuickSort, numbers: 2048, delay: arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5).go()
    run StableQuickSortParallel with 2048.numbers go()
    run ForcedStableQuickSort with 2048.numbers go()
    run TableSort with 1024.numbers and 0.75.delay
}

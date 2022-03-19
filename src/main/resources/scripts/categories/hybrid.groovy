import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Hybrid Sorts', 32) {
    run HybridCombSort with 1024.numbers run()
    run IntroCircleSortRecursive with 1024.numbers run()
    run IntroCircleSortIterative with 1024.numbers run()
    run BinaryMergeSort with 2048.numbers run()
    run MergeInsertionSort with 2048.numbers and 1.75.speed
    run WeaveMergeSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.65 : 6.5).speed)
    run TimSort with 2048.numbers run()
    run CocktailMergeSort with 2048.numbers run()
    run LaziestSort with 1024.numbers run()
    run WikiSort with 2048.numbers run()
    run GrailSort with 2048.numbers run()
    run AdaptiveGrailSort with 2048.numbers run()
    run UnstableGrailSort with 2048.numbers run()
    run SqrtSort with 2048.numbers run()
    run KotaSort with 2048.numbers run()
    run EctaSort with 2048.numbers run()
    run ParallelBlockMergeSort with 2048.numbers run()
    run ParallelGrailSort with 2048.numbers run()
    run FlanSort with 2048.numbers run()
    run RemiSort with 2048.numbers run()
    run ImprovedBlockSelectionSort with 2048.numbers run()
    run MedianMergeSort with 2048.numbers run()
    run BufferPartitionMergeSort with 2048.numbers run()
    run IntroSort with 2048.numbers run()
    run OptimizedBottomUpMergeSort with 2048.numbers run()
    run OptimizedDualPivotQuickSort with 2048.numbers and 0.75.speed
    run OptimizedWeaveMergeSort with 1024.numbers and 0.4.speed
    run StacklessHybridQuickSort with 2048.numbers and 0.75.speed
    run StacklessDualPivotQuickSort with 2048.numbers and 0.75.speed
    run PDQBranchedSort with 2048.numbers and 0.75.speed
    run PDQBranchlessSort with 2048.numbers and 0.75.speed
    run DropMergeSort with 2048.numbers and 0.75.speed
}

import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Hybrid Sorts', 32) {
    run HybridCombSort go 1024.numbers
    run IntroCircleSortRecursive go 1024.numbers
    run IntroCircleSortIterative go 1024.numbers
    run BinaryMergeSort go 2048.numbers
    run MergeInsertionSort go 2048.numbers, 1.75.speed
    run WeaveMergeSort go 2048.numbers, ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.65 : 6.5).speed)
    run TimSort go 2048.numbers
    run CocktailMergeSort go 2048.numbers
    run LaziestSort go 1024.numbers
    run WikiSort go 2048.numbers
    run GrailSort go 2048.numbers
    run AdaptiveGrailSort go 2048.numbers
    run UnstableGrailSort go 2048.numbers
    run SqrtSort go 2048.numbers
    run KotaSort go 2048.numbers
    run EctaSort go 2048.numbers
    run ParallelBlockMergeSort go 2048.numbers
    run ParallelGrailSort go 2048.numbers
    run FlanSort go 2048.numbers
    run RemiSort go 2048.numbers
    run ImprovedBlockSelectionSort go 2048.numbers
    run MedianMergeSort go 2048.numbers
    run BufferPartitionMergeSort go 2048.numbers
    run IntroSort go 2048.numbers
    run OptimizedBottomUpMergeSort go 2048.numbers
    run OptimizedDualPivotQuickSort go 2048.numbers, 0.75.speed
    run OptimizedWeaveMergeSort go 1024.numbers, 0.4.speed
    run StacklessHybridQuickSort go 2048.numbers, 0.75.speed
    run StacklessDualPivotQuickSort go 2048.numbers, 0.75.speed
    run PDQBranchedSort go 2048.numbers, 0.75.speed
    run PDQBranchlessSort go 2048.numbers, 0.75.speed
    run DropMergeSort go 2048.numbers, 0.75.speed
}

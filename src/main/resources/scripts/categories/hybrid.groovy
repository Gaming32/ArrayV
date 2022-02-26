import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Hybrid Sorts', 32) {
    run HybridCombSort with 1024.numbers go()
    run IntroCircleSortRecursive with 1024.numbers go()
    run IntroCircleSortIterative with 1024.numbers go()
    run BinaryMergeSort with 2048.numbers go()
    run MergeInsertionSort with 2048.numbers and 1.75.delay
    run(WeaveMergeSort, numbers: 2048, delay: arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.65 : 6.5).go()
    run TimSort with 2048.numbers go()
    run CocktailMergeSort with 2048.numbers go()
    run LaziestSort with 1024.numbers go()
    run WikiSort with 2048.numbers go()
    run GrailSort with 2048.numbers go()
    run AdaptiveGrailSort with 2048.numbers go()
    run UnstableGrailSort with 2048.numbers go()
    run SqrtSort with 2048.numbers go()
    run KotaSort with 2048.numbers go()
    run EctaSort with 2048.numbers go()
    run ParallelBlockMergeSort with 2048.numbers go()
    run ParallelGrailSort with 2048.numbers go()
    run FlanSort with 2048.numbers go()
    run RemiSort with 2048.numbers go()
    run ImprovedBlockSelectionSort with 2048.numbers go()
    run MedianMergeSort with 2048.numbers go()
    run BufferPartitionMergeSort with 2048.numbers go()
    run IntroSort with 2048.numbers go()
    run OptimizedBottomUpMergeSort with 2048.numbers go()
    run OptimizedDualPivotQuickSort with 2048.numbers and 0.75.delay
    run OptimizedWeaveMergeSort with 1024.numbers and 0.4.delay
    run StacklessHybridQuickSort with 2048.numbers and 0.75.delay
    run StacklessDualPivotQuickSort with 2048.numbers and 0.75.delay
    run PDQBranchedSort with 2048.numbers and 0.75.delay
    run PDQBranchlessSort with 2048.numbers and 0.75.delay
    run DropMergeSort with 2048.numbers and 0.75.delay
}

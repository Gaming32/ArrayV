import io.github.arrayv.prompts.SortPrompt

final def CATEGORY = 'Concurrent Sorts'

SortPrompt.setSortThreadForCategory(CATEGORY, 22) {
    // Other
    run FoldSort with 1024.numbers and 1.delay
    run CreaseSort with 1024.numbers and 1.delay
    run MatrixSort with 256.numbers and 0.667.delay

    // Recursive
    run BitonicSortRecursive with 1024.numbers and 1.delay
    run OddEvenMergeSortRecursive with 1024.numbers and 1.delay
    run PairwiseSortRecursive with 1024.numbers and 1.delay
    run BoseNelsonSortRecursive with 1024.numbers and 1.delay
    run WeaveSortRecursive with 1024.numbers and 1.delay
    run DiamondSortRecursive with 1024.numbers and 1.delay
    run PairwiseMergeSortRecursive with 1024.numbers and 1.delay

    // Parallel
    run BitonicSortParallel with 1024.numbers and 1.delay
    run OddEvenMergeSortParallel with 1024.numbers and 1.delay
    run BoseNelsonSortParallel with 1024.numbers and 1.delay
    run WeaveSortParallel with 1024.numbers and 1.delay

    // Iterative
    run BitonicSortIterative with 1024.numbers and 1.delay
    run OddEvenMergeSortIterative with 1024.numbers and 1.delay
    run PairwiseSortIterative with 1024.numbers and 1.delay
    run BoseNelsonSortIterative with 1024.numbers and 1.delay
    run WeaveSortIterative with 1024.numbers and 1.delay
    run MergeExchangeSortIterative with 1024.numbers and 1.delay
    run DiamondSortIterative with 1024.numbers and 1.delay
    run PairwiseMergeSortIterative with 1024.numbers and 1.delay
}

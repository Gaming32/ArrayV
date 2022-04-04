import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Concurrent Sorts', 22) {
    // Other
    run FoldSort go 1024.numbers
    run CreaseSort go 1024.numbers
    run MatrixSort go 256.numbers, 0.667.speed

    // Recursive
    run BitonicSortRecursive go 1024.numbers
    run OddEvenMergeSortRecursive go 1024.numbers
    run PairwiseSortRecursive go 1024.numbers
    run BoseNelsonSortRecursive go 1024.numbers
    run WeaveSortRecursive go 1024.numbers
    run DiamondSortRecursive go 1024.numbers
    run PairwiseMergeSortRecursive go 1024.numbers

    // Parallel
    run BitonicSortParallel go 1024.numbers
    run OddEvenMergeSortParallel go 1024.numbers
    run BoseNelsonSortParallel go 1024.numbers
    run WeaveSortParallel go 1024.numbers

    // Iterative
    run BitonicSortIterative go 1024.numbers
    run OddEvenMergeSortIterative go 1024.numbers
    run PairwiseSortIterative go 1024.numbers
    run BoseNelsonSortIterative go 1024.numbers
    run WeaveSortIterative go 1024.numbers
    run MergeExchangeSortIterative go 1024.numbers
    run DiamondSortIterative go 1024.numbers
    run PairwiseMergeSortIterative go 1024.numbers
}

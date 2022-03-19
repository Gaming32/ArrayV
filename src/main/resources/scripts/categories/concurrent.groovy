import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Concurrent Sorts', 22) {
    // Other
    run FoldSort with 1024.numbers run()
    run CreaseSort with 1024.numbers run()
    run MatrixSort with 256.numbers and 0.667.speed

    // Recursive
    run BitonicSortRecursive with 1024.numbers run()
    run OddEvenMergeSortRecursive with 1024.numbers run()
    run PairwiseSortRecursive with 1024.numbers run()
    run BoseNelsonSortRecursive with 1024.numbers run()
    run WeaveSortRecursive with 1024.numbers run()
    run DiamondSortRecursive with 1024.numbers run()
    run PairwiseMergeSortRecursive with 1024.numbers run()

    // Parallel
    run BitonicSortParallel with 1024.numbers run()
    run OddEvenMergeSortParallel with 1024.numbers run()
    run BoseNelsonSortParallel with 1024.numbers run()
    run WeaveSortParallel with 1024.numbers run()

    // Iterative
    run BitonicSortIterative with 1024.numbers run()
    run OddEvenMergeSortIterative with 1024.numbers run()
    run PairwiseSortIterative with 1024.numbers run()
    run BoseNelsonSortIterative with 1024.numbers run()
    run WeaveSortIterative with 1024.numbers run()
    run MergeExchangeSortIterative with 1024.numbers run()
    run DiamondSortIterative with 1024.numbers run()
    run PairwiseMergeSortIterative with 1024.numbers run()
}

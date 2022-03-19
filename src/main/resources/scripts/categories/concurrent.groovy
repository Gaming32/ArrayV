import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Concurrent Sorts', 22) {
    // Other
    run FoldSort with 1024.numbers go()
    run CreaseSort with 1024.numbers go()
    run MatrixSort with 256.numbers and 0.667.speed

    // Recursive
    run BitonicSortRecursive with 1024.numbers go()
    run OddEvenMergeSortRecursive with 1024.numbers go()
    run PairwiseSortRecursive with 1024.numbers go()
    run BoseNelsonSortRecursive with 1024.numbers go()
    run WeaveSortRecursive with 1024.numbers go()
    run DiamondSortRecursive with 1024.numbers go()
    run PairwiseMergeSortRecursive with 1024.numbers go()

    // Parallel
    run BitonicSortParallel with 1024.numbers go()
    run OddEvenMergeSortParallel with 1024.numbers go()
    run BoseNelsonSortParallel with 1024.numbers go()
    run WeaveSortParallel with 1024.numbers go()

    // Iterative
    run BitonicSortIterative with 1024.numbers go()
    run OddEvenMergeSortIterative with 1024.numbers go()
    run PairwiseSortIterative with 1024.numbers go()
    run BoseNelsonSortIterative with 1024.numbers go()
    run WeaveSortIterative with 1024.numbers go()
    run MergeExchangeSortIterative with 1024.numbers go()
    run DiamondSortIterative with 1024.numbers go()
    run PairwiseMergeSortIterative with 1024.numbers go()
}

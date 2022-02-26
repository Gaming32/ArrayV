import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Merge Sorts', 17) {
    run MergeSort with 2048.numbers and 1.5.delay
    run BottomUpMergeSort with 2048.numbers and 1.5.delay
    run MergeSortParallel with 2048.numbers and 1.5.delay
    run IterativeTopDownMergeSort with 2048.numbers and 1.5.delay
    run WeavedMergeSort with 2048.numbers and 1.5.delay
    run TwinSort with 2048.numbers and 1.5.delay
    run PDMergeSort with 2048.numbers go()
    run InPlaceMergeSort with 2048.numbers and 1.5.delay
    run ImprovedInPlaceMergeSort with 2048.numbers and 1.5.delay
    run LazyStableSort with 256.numbers and 0.2.delay
    run BlockSwapMergeSort with 256.numbers and 0.1.delay
    run RotateMergeSort with 512.numbers and 0.2.delay
    run RotateMergeSortParallel with 512.numbers and 0.2.delay
    run AndreySort with 2048.numbers go()
    run NewShuffleMergeSort with 1024.numbers go()
    run StrandSort with 2048.numbers go()
    run BufferedStoogeSort with 256.numbers and 0.2.delay
}

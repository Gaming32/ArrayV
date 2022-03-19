import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Merge Sorts', 17) {
    run MergeSort with 2048.numbers and 1.5.speed
    run BottomUpMergeSort with 2048.numbers and 1.5.speed
    run MergeSortParallel with 2048.numbers and 1.5.speed
    run IterativeTopDownMergeSort with 2048.numbers and 1.5.speed
    run WeavedMergeSort with 2048.numbers and 1.5.speed
    run TwinSort with 2048.numbers and 1.5.speed
    run PDMergeSort with 2048.numbers go()
    run InPlaceMergeSort with 2048.numbers and 1.5.speed
    run ImprovedInPlaceMergeSort with 2048.numbers and 1.5.speed
    run LazyStableSort with 256.numbers and 0.2.speed
    run BlockSwapMergeSort with 256.numbers and 0.1.speed
    run RotateMergeSort with 512.numbers and 0.2.speed
    run RotateMergeSortParallel with 512.numbers and 0.2.speed
    run AndreySort with 2048.numbers go()
    run NewShuffleMergeSort with 1024.numbers go()
    run StrandSort with 2048.numbers go()
    run BufferedStoogeSort with 256.numbers and 0.2.speed
}

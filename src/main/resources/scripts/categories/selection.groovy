import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Selection Sorts', 18) {
    run SelectionSort with 128.numbers and 0.01.delay
    run DoubleSelectionSort with 128.numbers and 0.01.delay
    run StableSelectionSort with 128.numbers and 0.5.delay
    run CycleSort with 128.numbers and 0.01.delay
    run StableCycleSort with 128.numbers and 0.01.delay
    run BingoSort with 128.numbers and 0.1.delay
    run MaxHeapSort with 2048.numbers and 1.5.delay
    run MinHeapSort with 2048.numbers and 1.5.delay
    run FlippedMinHeapSort with 2048.numbers and 1.5.delay
    run BaseNMaxHeapSort with 2048.numbers, 4.buckets and 1.5.delay
    run TriangularHeapSort with 2048.numbers and 1.5.delay
    run WeakHeapSort with 2048.numbers go()
    run TernaryHeapSort with 2048.numbers go()
    run SmoothSort with 2048.numbers and 1.5.delay
    run PoplarHeapSort with 2048.numbers go()
    run TournamentSort with 2048.numbers and 1.5.delay
    run ClassicTournamentSort with 2048.numbers and 1.5.delay
    run AsynchronousSort with 1024.numbers and 1.5.delay
}

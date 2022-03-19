import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Selection Sorts', 18) {
    run SelectionSort with 128.numbers and 0.01.speed
    run DoubleSelectionSort with 128.numbers and 0.01.speed
    run StableSelectionSort with 128.numbers and 0.5.speed
    run CycleSort with 128.numbers and 0.01.speed
    run StableCycleSort with 128.numbers and 0.01.speed
    run BingoSort with 128.numbers and 0.1.speed
    run MaxHeapSort with 2048.numbers and 1.5.speed
    run MinHeapSort with 2048.numbers and 1.5.speed
    run FlippedMinHeapSort with 2048.numbers and 1.5.speed
    run BaseNMaxHeapSort with 2048.numbers, 4.buckets and 1.5.speed
    run TriangularHeapSort with 2048.numbers and 1.5.speed
    run WeakHeapSort with 2048.numbers go()
    run TernaryHeapSort with 2048.numbers go()
    run SmoothSort with 2048.numbers and 1.5.speed
    run PoplarHeapSort with 2048.numbers go()
    run TournamentSort with 2048.numbers and 1.5.speed
    run ClassicTournamentSort with 2048.numbers and 1.5.speed
    run AsynchronousSort with 1024.numbers and 1.5.speed
}

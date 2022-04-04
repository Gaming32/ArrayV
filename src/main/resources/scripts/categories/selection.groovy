import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Selection Sorts', 18) {
    run SelectionSort go 128.numbers, 0.01.speed
    run DoubleSelectionSort go 128.numbers, 0.01.speed
    run StableSelectionSort go 128.numbers, 0.5.speed
    run CycleSort go 128.numbers, 0.01.speed
    run StableCycleSort go 128.numbers, 0.01.speed
    run BingoSort go 128.numbers, 0.1.speed
    run MaxHeapSort go 2048.numbers, 1.5.speed
    run MinHeapSort go 2048.numbers, 1.5.speed
    run FlippedMinHeapSort go 2048.numbers, 1.5.speed
    run BaseNMaxHeapSort go 2048.numbers, 4.buckets, 1.5.speed
    run TriangularHeapSort go 2048.numbers, 1.5.speed
    run WeakHeapSort go 2048.numbers
    run TernaryHeapSort go 2048.numbers
    run SmoothSort go 2048.numbers, 1.5.speed
    run PoplarHeapSort go 2048.numbers
    run TournamentSort go 2048.numbers, 1.5.speed
    run ClassicTournamentSort go 2048.numbers, 1.5.speed
    run AsynchronousSort go 1024.numbers, 1.5.speed
}

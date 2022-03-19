import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Insertion Sorts', 14) {
    run InsertionSort with 128.numbers and 0.005.speed
    run DoubleInsertionSort with 128.numbers and 0.002.speed
    run BinaryInsertionSort with 128.numbers and 0.025.speed
    run ShellSort with 256.numbers and 0.1.speed
    run RecursiveShellSort with 256.numbers and 0.1.speed
    run ShellSortParallel with 256.numbers and 0.1.speed
    run SimplifiedLibrarySort with 2048.numbers run()
    run LibrarySort with 2048.numbers run()
    run PatienceSort with 2048.numbers run()
    run ClassicTreeSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5).speed)
    run TreeSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5).speed)
    run AATreeSort with 2048.numbers run()
    run AVLTreeSort with 2048.numbers run()
    run SplaySort with 2048.numbers run()
}

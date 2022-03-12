import io.github.arrayv.prompts.SortPrompt
import io.github.arrayv.utils.Shuffles

SortPrompt.setSortThreadForCategory('Insertion Sorts', 14) {
    run InsertionSort with 128.numbers and 0.005.delay
    run DoubleInsertionSort with 128.numbers and 0.002.delay
    run BinaryInsertionSort with 128.numbers and 0.025.delay
    run ShellSort with 256.numbers and 0.1.delay
    run RecursiveShellSort with 256.numbers and 0.1.delay
    run ShellSortParallel with 256.numbers and 0.1.delay
    run SimplifiedLibrarySort with 2048.numbers go()
    run LibrarySort with 2048.numbers go()
    run PatienceSort with 2048.numbers go()
    run ClassicTreeSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5).delay)
    run TreeSort with 2048.numbers and ((arrayv.arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 5).delay)
    run AATreeSort with 2048.numbers go()
    run AVLTreeSort with 2048.numbers go()
    run SplaySort with 2048.numbers go()
}

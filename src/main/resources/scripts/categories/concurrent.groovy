import io.github.arrayv.prompts.SortPrompt

final def CATEGORY = 'Concurrent Sorts'

SortPrompt.setSortThreadForCategory(CATEGORY, 3) {
    // Other
    run FoldSort with 1024.numbers and 1.delay
    run CreaseSort with 1024.numbers and 1.delay
    run MatrixSort with 256.numbers and 0.667.delay
}

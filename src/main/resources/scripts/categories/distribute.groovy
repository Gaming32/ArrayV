import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory("Distribution Sorts") {
    runGroup(1) {
        run CountingSort with 2048.numbers and 1.5.delay
    }
}

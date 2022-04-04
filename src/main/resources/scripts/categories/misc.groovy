import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Miscellaneous Sorts', 2) {
    run PancakeSort go 128.numbers, 0.015.speed
    run BurntPancakeSort go 128.numbers, 0.015.speed
}

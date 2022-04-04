import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Distribution Sorts', 18) {
    run CountingSort go 2048.numbers, 1.5.speed
    run PigeonholeSort go 2048.numbers, 1.5.speed
    run GravitySort go 1024.numbers, 0.5.speed
    run ClassicGravitySort go 1024.numbers
    run StaticSort go 2048.numbers
    run IndexSort go 2048.numbers
    run AmericanFlagSort go 2048.numbers, 128.buckets, 0.75.speed
    run StacklessAmericanFlagSort go 2048.numbers, 128.buckets, 0.75.speed
    run LSDRadixSort go 2048.numbers, 4.buckets, 1.5.speed

    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    run InPlaceLSDRadixSort go 2048.numbers, 10.buckets
    arrayv.sounds.softerSounds = oldSofterSounds

    run MSDRadixSort go 2048.numbers, 4.buckets, 1.25.speed
    run FlashSort go 2048.numbers
    run BinaryQuickSortIterative go 2048.numbers
    run BinaryQuickSortRecursive go 2048.numbers
    run StacklessBinaryQuickSort go 2048.numbers
    run ShatterSort go 2048.numbers, 128.buckets
    run SimpleShatterSort go 2048.numbers, 128.buckets
    run TimeSort go 512.numbers, 10.buckets, 0.05.speed
}

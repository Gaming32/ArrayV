import io.github.arrayv.prompts.SortPrompt

SortPrompt.setSortThreadForCategory('Distribution Sorts', 18) {
    run CountingSort with 2048.numbers and 1.5.speed
    run PigeonholeSort with 2048.numbers and 1.5.speed
    run GravitySort with 1024.numbers and 0.5.speed
    run ClassicGravitySort with 1024.numbers run()
    run StaticSort with 2048.numbers run()
    run IndexSort with 2048.numbers run()
    run AmericanFlagSort with 2048.numbers, 128.buckets and 0.75.speed
    run StacklessAmericanFlagSort with 2048.numbers, 128.buckets and 0.75.speed
    run LSDRadixSort with 2048.numbers, 4.buckets and 1.5.speed

    def oldSofterSounds = arrayv.sounds.softerSounds
    arrayv.sounds.softerSounds = true
    run InPlaceLSDRadixSort with 2048.numbers and 10.buckets
    arrayv.sounds.softerSounds = oldSofterSounds

    run MSDRadixSort with 2048.numbers, 4.buckets and 1.25.speed
    run FlashSort with 2048.numbers run()
    run BinaryQuickSortIterative with 2048.numbers run()
    run BinaryQuickSortRecursive with 2048.numbers run()
    run StacklessBinaryQuickSort with 2048.numbers run()
    run ShatterSort with 2048.numbers, 128.buckets run()
    run SimpleShatterSort with 2048.numbers and 128.buckets
    run TimeSort with 512.numbers, 10.buckets and 0.05.speed
}

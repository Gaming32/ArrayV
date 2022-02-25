# ArrayV

![Discord](https://img.shields.io/discord/592082838791127075?color=%237289DA&label=Discord&logo=discord&logoColor=white)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/Gaming32/ArrayV/maven/main)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/Gaming32/ArrayV/checkstyle/main?label=checkstyle)

Over 200 sorting algorithms animated with 15 unique graphic designs

[Join the Discord server!](https://discord.gg/thestudio)

To compile use:
```shell
./mvnw clean verify
```

To run use:
```shell
./run
```
Alternatively, you can double click on "run" or "run.bat" on Windows.

You can always download the most up-to-date pre-built JAR from [GitHub Actions](https://nightly.link/Gaming32/ArrayV/workflows/maven/main/standalone-jar.zip) (link via [nightly.link](https://nightly.link)).

## Legacy changelog

### 6/8/2020 - Version 3.5
- NEW VISUALS: Sine Wave and Wave Dots!!
- New sort: Bogobogosort
- The bogo shuffle method is now unbiased
- MultipleSortThreads further refactored
- Visuals, VisualStyles enum, and Renderer significantly refactored (more to come!)

### 6/4/2020 - Version 3.2
- New sort: Optimized Cocktail Shaker Sort
- Significant refactoring for MultipleSortThreads and RunAllSorts
- "Run All" button approx. time simplified
- Modified delays for Binary Gnomesort
- Documentation of GCC's median-of-three pivot selection in Introsort

### 6/3/2020 - Version 3.12
- Counting Sort fixed
- Optimized Bubblesort now optimized for already sorted inputs
- Speeds for Quicksorts and Weave Merge during "Run All Sorts" improved

### 6/2/2020 - Version 3.11
- Minor update to MIT license
- Fixed typo in Flipped Min Heapsort
- Improved highlights on Heapsorts (Already sorted heaps now display redundant comparisons)
- Bug fix for Patiencesort on reversed arrays
- Quicksorts exhibiting worst-case behavior during "Run All Sorts" run much faster
- Same tweak as above to Weave Merge Sort

### 5/30/2020 - Version 3.1
- Error messages with detailed information will now appear within the program!
- Sound effects are now consistent on all platforms
- New sort: "Flipped Min Heap Sort" by 'landfillbaby'!
- Minor changes to code organization
- New webhook to my Discord server! Check it out here: https://discord.com/invite/2xGkKC2

### 5/22/2020 - Version 3.01
- Quick bug fix to the "Linked Dots" visual;
  The first line is no longer horizontal.

### 5/21/2020 - Version 3.0 is now released!
- Sound effects are much more pleasant at slower speeds
- Revamped "Run All Sorts" (It is now easier to create your own sequence of sorts!)
- More accurate delay algorithm
- Improved random shuffle algorithm (now with 0% bias!)
- Cleaner statistics
- Sort an array up to 16,384 (2^14) numbers!
- The "green sweep" animation also verifies an array is properly sorted after watching a sort.
  If a sort fails, a warning message pops up, highlighting where the first out-of-order item is.
- Minor tweak to the sort time method. It should be a slight bit more accurate now.
- Slowsort and Sillysort's comparisons are now shown.
- Gravity Sort has a more detailed visual now
- Pancake Sorting is fixed
- Counting Sort is fixed
- Holy Grail Sort is enabled, but just note that it's a mock algorithm; not finished yet.
- "Auxillary" typo fixed; program now says 'Writes to Auxiliary Array(s)'
- Bug fixes and minor tweaks
  - Minor fixes to "Skip Sort" button
  - Weird static line bug with linked dots squashed
  - Other miscellaneous fixes and changes here and there

### 10/19/2019 - Version 2.1
- Both Odd-Even Mergesorts now display comparisons
- PDQSort's Insertion Sorts have been slowed down
- New sorts, courtesy of Piotr Grochowski (https://github.com/PiotrGrochowski/ArrayVisualizer):
  - Iterative Pairwise Sorting Network
  - Recursive Pairwise Sorting Network
  - Recursive Combsort

### 10/13/2019 - Version 2.0 is now released!
- Now includes 73 sorting algorithms, with 2 more that will be finished in the future
  - NEW SORTS:
    - Unoptimized Bubble Sort
    - Rotation-based In-Place Merge Sort
    - "Lazy Stable Sort" from Andrey Astrelin's GrailSort
    - Grail sorting with a static buffer
    - Grail sorting with a dynamic buffer
    - Andrey Astrelin's "SqrtSort"
    - CircleSort
    - Introspective CircleSort
    - Orson Peters' "Pattern-Defeating Quicksort" (PDQSort)
    - Branchless PDQSort
    - Morwenn's implementation of "Poplar Heapsort"
    - Recursive Binary Quicksort
    - Iterative Binary Quicksort
    - Iterative Bitonic Sort
    - Iterative Odd-Even Mergesort
    - "Bubble Bogosort"
    - "Exchange Bogosort"
    - Treesort
    - Optimized Gnomesort with Binary Search
    - "Cocktail Mergesort" (https://www.youtube.com/watch?v=fWubJgIWyxQ)
    - NOTE: "Quick Shell Sort" has been removed.
- Significantly refactored code, more object-oriented
- Optimized visuals -- the program runs smoother than ever!
- Plug-and-play functionality -- using classgraph, you can now easily add your own sorting algorithms to the program! Documentation on that will be available in the future.
- Sort delay system redesigned -- you can now change the speed of the program in the middle of a delayed compare or swap
- Speed dialogue is now disabled while other windows are open
- WikiSort no longer gets stuck on sorting its internal buffer
- Tweaks to TimSort, mostly reimplementing its binary insertion sort
- Binary Insertion Sort is now stable
- The write/swap counts for inputs already sorted are fixed
- The main/auxillary array write counts for Bottom-up Merge are fixed
- Shuffling the array now clears the statistics
- The highest pitches of the program's sound effects are fixed
- Speeds for the "green sweep" and shuffling animations have been tweaked
- Shatter Sort's highlights slightly tweaked
- GrailSort's highlights slightly tweaked

**KNOWN BUGS:**
- Certain sorts (comb sort, radix sorts) cause the program to forget the current speed
- Certain sorts do not work with the "Skip Sort" button
- Missing soundfont
- SkaSort and HolyGrailSort produce errors -- this is normal, they aren't finished yet
- No circular pointer -- will be fixed soon

**PLANS FOR FUTURE RELEASES:**
- Javadocs!!
- SkaSort
- "Holy Grail Sort"
- Options to:
  - Enter in your own set of numbers
  - Select CombSort gap sequence
  - Select ShellSort gap sequence
  - Change TimSort "minrun" value
  - Change IntroSort threshold for insertion/heap sort
  - Change Simple Shatter Sort rate(?)
  - Stop Run All Sorts(?)
  - Stop TimeSort(?)
- Pre-shuffled arrays
- Organize list of sorts into more categories
- Run All Sorts in specific category
- Subheadings for customizable sorts (e.g. display the number of buckets during a bucket sort)
- "Pipe organ" distribution (half ascending, half descending)
- Fixed circular pointer with much cleaner math
- Toogle between pointer and black bar with circular visuals
- Refactor/reorganize prompts and frames
- Cleaner:
  - Tree Sort
  - getters/setters
  - method parameters
- Small organizational changes

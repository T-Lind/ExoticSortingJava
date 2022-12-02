# <u>Exotic Sorting in Java</u>
### For the 2022 CS3 Sorting Competition
##### Authors: Tiernan Lindauer, Julianne Insall

### The Optimal Solution:

This repository contains multiple different approaches to beating `Arrays.ParallelSort()` on a sort of 1B items with non-negative 32 bit integers.

The submission provided is present in the `radix_sorting` folder, in which the most optimized version is in `radix_sort/SingularSortingAnalysis.java`.
It sorts these 1B items in ~2.9s on average.
This optimized approach was achieved through `radix_sorting/GridSearchSortingAnalysis.java` that tests every combination of threads and bits used within the specified ranges.

#### Prior Art:

This `radix_sorting` is based on the code from <https://github.com/sverrbb/parallel-radix-sort/blob/main/Main.java>, however significant improvements have been made.
The average time this code takes to execute is ~3.6s, while this code takes only ~2.9s for 1B items.
This significant improvement in time is due to the numerous method calls that have been reduced and whose functions have been placed in-line with the rest of the code.
Much of the member data of each class has also been reduced, and consigned to individual methods.
Beyond that, the grid-search algorithm is vital in optimizing the time, because it can determine the absolute minimum of time possible with this algorithm.
<p>
Additionally, the code has been thoroughly documented through the use of line comments and Javadoc. A file-writing system has also been put in place that saves data from each run, as well as many computed statistics.

#### Team Contributions:
Tiernan wrote the code for the different attempts to beating `Arrays.parallelSort()`, and optimized the multithread radix sort from the link mentioned above.
Julianne worked to research different sorting methods that could beat `Arrays.parallelSort()` and explain how they're

#### Project Architecture
Inside of the folder `radix_sorting` are the two analysis classes, which run the sorting themselves and analyze the performance of each sort.
A `Util` class also exists, which contains code that performs a definitive check on whether the array is sorted or not, functions to find the average/min/st. dev of a dataset, and a function to write the machine's statistics to a file.
Inside of `radix_sorting` is `radix_sorting/data` and `radix_sorting/depricated`.
`radix_sorting/data` contains logs of trials, and is divided into further sub-folders of when they were run and on what machine
The depricated folder simply contains old code that is no longer used.


### Other Attempts:

The `counting_sort/ParallelizedCountingSort.java` class, whose implementation is hidden in `counting_sort/Sort.java`, does beat ParallelSort however only on smaller arrays, it runs out of memory

A parallel rank sort, which yielded good times as well (comprable to `Arrays.parallelSort()`) is present in the `rank_sort` folder.

A multi-pivot quicksort is present in the `multi_pivot_quicksort` folder

Additionally, a recursive quicksort as attempted in `recursive_quicksort`

Many of these approaches were tried in C++, and some in C, however the difficulty of multi-threading in these languages make fast sorts difficult, but potentially faster than comprable ones in Java.

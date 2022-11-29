# Exotic Sorting in Java
### For the CS3 2022 Sorting Competition
##### Author: Tiernan Lindauer

### The Optimal Solution:

This repository contains multiple different approaches to beating `Arrays.ParallelSort()` on a sort of 1B items with non-negative 32 bit integers.

The submission provided is present in the `radix_sorting` folder, in which the most optimized version is in `radix_sort/BasicSortingAnalysis.java`.
This optimized approach was achieved through `radix_sorting/GridSearchSortingAnalysis.java` that trials every combination of threads and bits used within the specified ranges.


### Other Attempts:

The `counting_sort/ParallelizedCountingSort.java` class, whose implementation is hidden in `counting_sort/Sort.java`, does beat ParallelSort however only on smaller arrays, it runs out of memory

A parallel rank sort, which yielded good times as well (comprable to `Arrays.parallelSort()`) is present in the `rank_sort` folder.

A multi-pivot quicksort is present in the `multi_pivot_quicksort` folder

Additionally, a recursive quicksort as attempted in `recursive_quicksort`

Many of these approaches were tried in C++, and some in C, however the difficulty of multi-threading in these languages make fast sorts difficult, but potentially faster than comprable ones in Java.

# <u>Exotic Sorting in Java</u>
### For the 2022 CS3 Sorting Competition
##### Author: Tiernan Lindauer

### The Optimal Solution:

This repository contains multiple different approaches to beating `Arrays.ParallelSort()` on a sort of 1B items with non-negative 32 bit integers.

The submission provided is present in the `radix_sorting` folder, in which the most optimized version is in `radix_sort/SingularSortingAnalysis.java`.
It sorts these 1B items in ~2.9s on average.
This optimized approach was achieved through `radix_sorting/GridSearchSortingAnalysis.java` that tests every combination of threads and bits used within the specified ranges.

#### Parallel Radix Sort:
A parallel radix sort is a variant of the radix sort algorithm that is designed to take advantage of multiple processors or cores in a computer system to improve the performance and speed of the sorting process. This is achieved by dividing the list of numbers to be sorted into smaller sub-lists, and then sorting each sub-list in parallel using multiple processors or cores.

The basic steps involved in a parallel radix sort are similar to those of a regular radix sort, but they are applied to each sub-list in parallel. Each sub-list would then be sorted independently using the radix sort algorithm, starting from the least significant digit and moving towards the most significant digit.

Once all of the sub-lists have been sorted, they are concatenated to form the final, sorted list of numbers.

The key advantage of a parallel radix sort is that it can take advantage of multiple processors or cores to sort the numbers more quickly and efficiently than a regular radix sort. This can be particularly useful for sorting large lists of numbers, or for sorting numbers that have a large number of digits. However, the performance improvements of a parallel radix sort will depend on the specific hardware and software being used, as well as the size and structure of the list of numbers being sorted.ontains old code that is no longer used.


### Other Attempts:

The `counting_sort/ParallelizedCountingSort.java` class, whose implementation is hidden in `counting_sort/Sort.java`, does beat ParallelSort however only on smaller arrays, it runs out of memory

A parallel rank sort, which yielded good times as well (comprable to `Arrays.parallelSort()`) is present in the `rank_sort` folder.

A multi-pivot quicksort is present in the `multi_pivot_quicksort` folder

Additionally, a recursive quicksort as attempted in `recursive_quicksort`

Many of these approaches were tried in C++, and some in C, however the difficulty of multi-threading in these languages make fast sorts difficult, but potentially faster than comprable ones in Java.

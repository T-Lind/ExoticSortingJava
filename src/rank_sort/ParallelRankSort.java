package rank_sort;

import static rank_sort.ArrayOperations.isSorted;
import static rank_sort.ArrayOperations.randomizeArray;

class ParallelRankSort {
    private static int N_ITEMS = 100_000_000;

    private static int MAX_NUM = Integer.MAX_VALUE - 1;
    private static int N_THREADS = 32;

    private static RankSortThread[] threads;


    public static void main(String[] args) throws InterruptedException {
        var t0 = System.currentTimeMillis();

        var input = new int[N_ITEMS];
        var output = new int[N_ITEMS];

        randomizeArray(input, N_ITEMS, MAX_NUM);

        threads = new RankSortThread[N_THREADS];

        var t1 = System.currentTimeMillis();

        for (int id = 0; id < N_THREADS; id++) {
            threads[id] = new RankSortThread(input, output, N_THREADS, id);
            threads[id].start();
        }

        for (int i = 0; i < N_THREADS; i++)
            threads[i].join();


        var t2 = System.currentTimeMillis();

        // Printout
        System.out.println("SORTING COMPETITION:");
        System.out.println("--------------------");
        System.out.println("Instantiation time: " + (t1 - t0) + "ms");
        System.out.println("Sorting time: " + (t2 - t1) + "ms");
        System.out.println("Total runtime: " + (t2 - t0) + "ms");
        System.out.print("Comprehensive sorting test output: " + isSorted(input, N_ITEMS) + "\n");

        for (int i = 0; i < 100; i++)
            System.out.print(input[i] + " ");
    }
}

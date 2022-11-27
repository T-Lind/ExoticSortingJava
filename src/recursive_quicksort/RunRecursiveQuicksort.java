package recursive_quicksort;

import static recursive_quicksort.ArrayOperations.randomizeArray;

public class RunRecursiveQuicksort {
    private static int N_ITEMS = 1_000_000;

    private static int MAX_NUM = 1_000_000;

    public static void main(String[] args) {
        var input = new int[N_ITEMS];

        randomizeArray(input, N_ITEMS, MAX_NUM);

        var t1 = System.currentTimeMillis();
        RecursiveQuicksort.startMainTask(input);
        var t2 = System.currentTimeMillis();
        System.out.println((t2-t1)+" milliseconds");
    }
}

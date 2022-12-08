package counting_sort;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements a default counting sort algorithm.
 * It sorts an integer array in ascending order, and includes a
 * helper method to check if the array is sorted.
 */
public class DefaultCountingSort {
    private static int N_ITEMS = 100_000_000;

    private static int MAX_VAL = 2_000_000_000;

    /**
     * This method checks if an array is sorted in ascending order.
     *
     * @param array The array to check
     * @return A boolean indicating whether the array is sorted or not
     */
    static boolean isSorted(int[] array){
        int prev = 0;
        for(int item: array)
            if(item >= prev)
                prev = item;
            else
                return false;
        return true;
    }

    /**
     * This method sorts an array using a counting sort algorithm.
     *
     * @param array The array to sort
     * @param output The array to store the sorted elements in
     * @param count An auxiliary array used for counting
     */
    static void countSort(int[] array, int[] output, int[] count) {
        int i;
        for (i = 1; i <= N_ITEMS; i++)
            count[array[i]]++;

        for (i = 1; i <= MAX_VAL - 1; i++)
            count[i] += count[i - 1];

        for (i = N_ITEMS; i >= 1; i--) {
            output[count[array[i]]] = array[i];
            count[array[i]]--;
        }
    }

    /**
     * The main method initializes an array with random integer values,
     * sorts it using the `countSort` method, measures the time it took
     * to sort the array, and prints the result and whether the array
     * is sorted or not.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        var array = new int[N_ITEMS + 1];
        var output = new int[N_ITEMS + 1];
        var count = new int[MAX_VAL + 1];

        for (int i = 0; i < N_ITEMS; i++)
            array[i] = ThreadLocalRandom.current().nextInt(0, MAX_VAL);

        var t0 = System.currentTimeMillis();
        countSort(array, output, count);
        var t1 = System.currentTimeMillis();


        System.out.println("\nSorting took " + (t1 - t0) + " milliseconds.");
        System.out.println("Array is sorted: "+isSorted(output));
    }
}

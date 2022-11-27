import java.util.concurrent.ThreadLocalRandom;

public class DefaultCountingSort {
    private static int N_ITEMS = 100_000;

    private static int MAX_VAL = 10_000;


    static boolean isSorted(int[] array){
        int prev = 0;
        for(int item: array)
            if(item >= prev)
                prev = item;
            else
                return false;
        return true;
    }

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

    public static void main(String[] args) {
        var array = new int[N_ITEMS + 1];
        var output = new int[N_ITEMS + 1];
        var count = new int[MAX_VAL + 1];

        for (int i = 0; i < N_ITEMS; i++)
            array[i] = ThreadLocalRandom.current().nextInt(0, MAX_VAL);

        var t0 = System.currentTimeMillis();
        countSort(array, output, count);
        var t1 = System.currentTimeMillis();

        System.out.println("Sorting took " + (t1 - t0) + " milliseconds.");
        System.out.println("Array is sorted: "+isSorted(output));
    }
}

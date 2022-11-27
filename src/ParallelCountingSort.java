import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelCountingSort {
    private static int N_ITEMS = 10_000_000;
    private static int N_THREADS = 2;

    private static int MAX_VAL = 10_000_000;


    private static Thread[] sortThreads;
    private static SortThread[] sortObjects;


    static boolean isSorted(int[] array) {
        int nIncorrect = 0;
        int zeroCtr = 0;
        for (int i = 1; i < N_ITEMS; i++) {
            if (array[i] < array[i - 1] && nIncorrect < 10) {
                System.out.println("Index: " + i + ", values: " + array[i - 1] + " " + array[i]);
                nIncorrect++;
            }
            if(array[i] == 0)
                zeroCtr++;
        }
        if(zeroCtr==array.length) {
            System.out.println("Oh no! The array is all zeros!");
            return false;
        }

        if (nIncorrect == 0)
            return true;
        System.out.println("Number of mis-sorts: " + nIncorrect);
        return false;
    }


    static void countSort(int[] array, int[] output, int[] count) {
        int i;
        for (i = 1; i < N_ITEMS; i++)
            count[array[i]]++;

        for (i = 1; i < MAX_VAL; i++)
            count[i] += count[i - 1];

        for (i = N_ITEMS; i >= 1; i--) {
            output[count[array[i]]] = array[i];
            count[array[i]]--;
        }
    }

    static int[] bounds(int id){
        int lowerBound = (int) (N_ITEMS * (float) id / N_THREADS + 1);
        int upperBound = (int) (N_ITEMS * (float) id / N_THREADS + (float) N_ITEMS / N_THREADS);
        return new int[]{lowerBound, upperBound};
    }

    public static void main(String[] args) {
        var t0 = System.currentTimeMillis();

        var array = new int[N_ITEMS + 1];
        var output = new int[N_ITEMS + 1];
        var count = new int[MAX_VAL + 1];

        sortThreads = new Thread[N_THREADS];
        sortObjects = new SortThread[N_THREADS];
        for (short i = 0; i < N_THREADS; i++) {
            int[] limits = bounds(i);
            sortObjects[i] = new SortThread(Arrays.copyOfRange(array, limits[0], limits[1]+1));
            sortThreads[i] = new Thread(sortObjects[i]);
        }


        // Start sorting
        var t1 = System.currentTimeMillis();

        for (int i = 0; i < N_THREADS; i++)
            sortThreads[i].start();


        while (true) {
            boolean finished = true;
            for (int i = 0; i < N_THREADS; i++)
                if (finished && sortThreads[i].isAlive())
                    finished = false;
            if (finished)
                break;
        }

        int[] result = sortObjects[0].array;

        var t2 = System.currentTimeMillis();


        System.out.println("\nSORTING COMPETITION PROGRAM:");
        System.out.println("---------------------------");
        System.out.println("Items sorted: " + N_ITEMS);
        System.out.println("Max value: " + MAX_VAL);
        System.out.println("Initialization took " + (t1 - t0) + " milliseconds.");
        System.out.println("Prelim took " + (t2 - t1) + " milliseconds.");
        System.out.println("All of sorting took " + (t2 - t1) + " milliseconds.");
        System.out.println("Total time took " + (t2 - t0) + " milliseconds.");
        System.out.println("Array is sorted: " + isSorted(result));
    }


    private static final class SortThread implements Runnable {
        public int[] array, output, count;
        public SortThread(int[] array) {
            this.array = array;
            System.out.println("Length: "+array.length);
            this.count = new int[MAX_VAL + 1];
            this.output = new int[array.length + 1];
        }

        @Override
        public void run() {
            countSort(array, output, count);
            isSorted(output);
        }
    }
}

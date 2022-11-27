import java.util.concurrent.ThreadLocalRandom;

public class ParallelCountingSort {
    private static int N_ITEMS = 100_000_000;
    private static int N_THREADS = 16;

    private static int MAX_VAL = 100_000_000;

    private static int[] array;
    private static int[] count;
    private static int[] output;

    private static Thread[] sortThreads, countThreads;


    static boolean isSorted(int[] array) {
        int prev = 0;
        for (int item : array)
            if (item >= prev)
                prev = item;
            else
                return false;
        return true;
    }

    static void countArray(int[] array, int[] count) {
        int i;
        for (i = 1; i <= N_ITEMS; i++)
            count[array[i]]++;

        for (i = 1; i <= MAX_VAL - 1; i++)
            count[i] += count[i - 1];
    }

    static void countArray(int[] array, int[] count, short id) {
        int lowerBound = id / N_THREADS + 1;
        int upperBound = id / N_THREADS + 1 / N_THREADS;


        int i;
        for (i = lowerBound; i <= upperBound; i++)
            count[array[i]]++;

        for (i = lowerBound; i <= upperBound - 1; i++)
            count[i] += count[i - 1];
    }

    static void sort(int[] array, int[] output, int[] count, short id) {
        int lowerBound = id / N_THREADS + 1;
        int upperBound = id / N_THREADS + 1 / N_THREADS;

        for (int i = upperBound; i >= lowerBound; i--) {
            output[count[array[i]]] = array[i];
            count[array[i]]--;
        }
    }

    @Deprecated
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
        var t0 = System.currentTimeMillis();

        array = new int[N_ITEMS + 1];
        output = new int[N_ITEMS + 1];
        count = new int[MAX_VAL + 1];

        sortThreads = new Thread[N_THREADS];
        countThreads = new Thread[N_THREADS];

        for (short i = 0; i < N_THREADS; i++)
            countThreads[i] = new Thread(new CountThread(i));
        for (short i = 0; i < N_THREADS; i++)
            sortThreads[i] = new Thread(new SortThread(i));

        for (int i = 0; i < N_ITEMS; i++)
            array[i] = ThreadLocalRandom.current().nextInt(0, MAX_VAL);


        var t1 = System.currentTimeMillis();
        for (int i = 0; i < N_THREADS; i++) {
            countThreads[i].start();
        }

        while (true) {
            boolean finished = true;
            for (int i = 0; i < N_THREADS; i++)
                if (finished && countThreads[i].isAlive())
                    finished = false;
            if (finished)
                break;
        }

        // Second stage of sorting
        var t2 = System.currentTimeMillis();
        for (int i = 0; i < N_THREADS; i++) {
            sortThreads[i].start();
        }

        while (true) {
            boolean finished = true;
            for (int i = 0; i < N_THREADS; i++)
                if (finished && sortThreads[i].isAlive())
                    finished = false;
            if (finished)
                break;
        }

        var t3 = System.currentTimeMillis();

        System.out.println("SORTING COMPETITION PROGRAM:");
        System.out.println("---------------------------");
        System.out.println("Items sorted: " + N_ITEMS);
        System.out.println("Max value: " + MAX_VAL);
        System.out.println("Initialization took " + (t1 - t0) + " milliseconds.");
        System.out.println("Prelim took " + (t2 - t1) + " milliseconds.");
        System.out.println("All of sorting took " + (t3 - t1) + " milliseconds.");
        System.out.println("Total time took " + (t3 - t0) + " milliseconds.");
        System.out.println("Array is sorted: " + isSorted(output));
    }

    private static final class SortThread implements Runnable {
        public short id;

        public SortThread(short id) {
            this.id = id;
        }

        @Override
        public void run() {
            sort(array, output, count, id);
        }
    }

    private static final class CountThread implements Runnable {
        public short id;

        public CountThread(short id) {
            this.id = id;
        }

        @Override
        public void run() {
            countArray(array, count, id);
        }
    }
}

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelCountingSort {
    private static int N_ITEMS = 100_000_000;
    private static int N_THREADS = 4;

    private static int MAX_VAL = 100_000_000;

    private static int[] array;
    private static int[] count;
    private static int[] output;

    private static Thread[] sortThreads, countThreads;


    static boolean isSorted(int[] array) {
        int i=1;
        while(i < N_ITEMS) {
            if (array[i] < array[i - 1]) {
                System.out.println("Index: " + i + ", values: " + array[i - 1] + " " + array[i]);
                return false;
            }
            i++;
        }
        return true;
    }

    static void countArray() {
        int i;
        for (i = 1; i <= N_ITEMS; i++)
            count[array[i]]++;

        for (i = 1; i <= MAX_VAL - 1; i++)
            count[i] += count[i - 1];
    }

    static void countArray(int id) {
        int lowerBound = (int) (N_ITEMS * (float) id / N_THREADS + 1);
        int upperBound = (int) (N_ITEMS * (float) id / N_THREADS + (float)N_ITEMS / N_THREADS);



        int i;
        for (i = lowerBound; i <= upperBound; i++)
            count[array[i]]++;

        for (i = lowerBound; i <= upperBound - 1; i++)
            count[i] += count[i - 1];

    }

    static void sort(int id) {
        int lowerBound = (int) (N_ITEMS * (float) id / N_THREADS + 1);
        int upperBound = (int) (N_ITEMS * (float) id / N_THREADS + (float)N_ITEMS / N_THREADS);


        for (int i = upperBound; i >= lowerBound; i--) {
            output[count[array[i]]] = array[i];
            count[array[i]]--;
        }
    }

    static void sort(){
        for (int i = N_ITEMS-1; i >= 1; i--) {
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

        for (int i = 0; i < N_ITEMS; i++)
            array[i] = ThreadLocalRandom.current().nextInt(0, MAX_VAL);

        for (short i = 0; i < N_THREADS; i++)
            countThreads[i] = new Thread(new CountThread(i));
        for (short i = 0; i < N_THREADS; i++)
            sortThreads[i] = new Thread(new SortThread(i));






        var t1 = System.currentTimeMillis();
//        for (int i = 0; i < N_THREADS; i++)
//            countThreads[i].start();
        countArray(0);
        countArray(1);
        countArray(2);
        countArray(3);
//        countArray();



//        while (true) {
//            boolean finished = true;
//            for (int i = 0; i < N_THREADS; i++)
//                if (finished && countThreads[i].isAlive())
//                    finished = false;
//            if (finished)
//                break;
//        }

        // Second stage of sorting
        var t2 = System.currentTimeMillis();
//        for (int i = N_THREADS-1;i>=0;i--)
//            sortThreads[i].start();

        sort(3);
        sort(2);
        sort(1);
        sort(0);
//        sort();

//        while (true) {
//            boolean finished = true;
//            for (int i = 0; i < N_THREADS; i++)
//                if (finished && sortThreads[i].isAlive())
//                    finished = false;
//            if (finished)
//                break;
//        }

        var t3 = System.currentTimeMillis();


        System.out.println("\nSORTING COMPETITION PROGRAM:");
        System.out.println("---------------------------");
        System.out.println("Items sorted: " + N_ITEMS);
        System.out.println("Max value: " + MAX_VAL);
        System.out.println("Initialization took " + (t1 - t0) + " milliseconds.");
        System.out.println("Prelim took " + (t2 - t1) + " milliseconds.");
        System.out.println("All of sorting took " + (t3 - t1) + " milliseconds.");
        System.out.println("Total time took " + (t3 - t0) + " milliseconds.");
        System.out.println("Array is sorted: " + isSorted(output));

        System.out.print("Count from 1000-1010: ");
        for(int i=1000;i<1010;i++)
            System.out.print(count[i]+" ");
        System.out.print("\nOutput: ");
        for(int i=1000;i<1010;i++)
            System.out.print(output[i]+" ");
    }

    private static final class SortThread implements Runnable {
        public short id;

        public SortThread(short id) {
            this.id = id;
        }

        @Override
        public void run() {
            sort(id);
        }
    }

    private static final class CountThread implements Runnable {
        public short id;

        public CountThread(short id) {
            this.id = id;
        }

        @Override
        public void run() {
            countArray(id);
        }
    }
}

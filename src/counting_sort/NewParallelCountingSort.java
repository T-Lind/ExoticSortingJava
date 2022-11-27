package counting_sort;

import recursive_quicksort.RecursiveQuicksort;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import static counting_sort.ArrayOperations.randomizeArray;
import static java.lang.Math.floor;

public class NewParallelCountingSort {
    private static int N_ITEMS = 1_000;
    private static int MAX_VAL = 1_000;

    private static int N_THREADS = 4;

    private static Thread[] threads;
    private static CountSortThread[] sorts;

    public static void main(String[] args) throws InterruptedException {
        threads = new Thread[N_THREADS];
        sorts = new CountSortThread[N_THREADS];

        var array = new int[N_ITEMS];
        var output = new int[N_ITEMS];

        randomizeArray(array, N_ITEMS, MAX_VAL);

        for (int i = 1; i <= N_THREADS; i++)
            sorts[i - 1] = new CountSortThread(i, array);
        for (int i = 0; i < N_THREADS; i++)
            threads[i] = new Thread(sorts[i]);


        var t0 = System.currentTimeMillis();
        for (int i = 0; i < N_THREADS; i++)
            threads[i].start();

        boolean exited = false;
        while (!exited) {
            exited = true;
            for (int i = 0; i < N_THREADS; i++)
                if (threads[i].isAlive())
                    exited = false;
        }

        int appendingThread = 0;
        for (int i = 0; i < N_ITEMS; i++) {
            output[i] = sorts[appendingThread].output[i - sorts[appendingThread].start];
        }


        var t1 = System.currentTimeMillis();
        System.out.println("Sorting time: " + (t1 - t0) + " milliseconds");

        for (int i = 0; i < 10; i++)
            System.out.println(sorts[0].output[i] + " ");
    }


    private static void countSort(int[] array, int[] output, int[] count, int size) {
        int i;
        for (i = 1; i <= size; i++)
            count[array[i]]++;

        for (i = 1; i <= MAX_VAL - 1; i++)
            count[i] += count[i - 1];

        for (i = size; i >= 1; i--) {
            output[count[array[i]]] = array[i];
            count[array[i]]--;
        }
    }


    private static class CountSortThread implements Runnable {
        public int[] array, output, count;
        private int size, width;

        public int start, end;

        public CountSortThread(int id, int[] input) { // id is 1-4 incl.
            width = MAX_VAL / N_THREADS;
            size = 0;

            start = (id - 1) * width;
            end = id * (width + 1);

            for (int i = 0; i < N_ITEMS; i++)
                if (start <= input[i] && input[i] < end)
                    size++;

            array = new int[size + 1];
            output = new int[size + 1];
            count = new int[N_ITEMS + 1];

            int addIdx = 0;
            for (int i = 0; i < N_ITEMS; i++) {
                if ((id - 1) * width <= input[i] && input[i] < id * (width + 1)) {
                    array[addIdx] = input[i];
                    addIdx++;
                }
            }
        }

        @Override
        public void run() {
            countSort(array, output, count, size);
        }
    }
}

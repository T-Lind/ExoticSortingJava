package radix_sorting;

import java.util.concurrent.CyclicBarrier;

/**
 * Hyper-optimized class to sort a static array in parallel through a radix sort
 */
public class ParallelRadixSort {

    /**
     * Variables containing various constants used throughout the sort
     */
    private final int N_THREADS, SIZE_OF_SEGMENT, REMAINDER, USE_BITS;

    /**
     * Arrays containing the sorting data
     */
    private final int[] array;
    private int[] arrayCopy;

    /**
     * Lists containing the count
     */
    private final int[][] countList;
    private final int[][]digitPointList;

    /**
     * Contains the objects sorting the array
     */
    private final Thread[] threads;

    /**
     * Allows a set of threads to all wait for each other to reach a common barrier point
     */
    private final CyclicBarrier cb1, cb2;

    /**
     * Index for threads to start sorting at
     */
    private int start;


    /**
     * Create the object used to sort the given array
     *
     * @param array     the array to sort IN PLACE
     * @param N_THREADS the number of threads to use when sorting
     * @param USE_BITS  number of bits to initially sort by (excess goes to remainder)
     * @param N_ITEMS   number of items to sort to remove array.length call
     */
    public ParallelRadixSort(int[] array, int N_THREADS, int USE_BITS, int N_ITEMS) {
        this.array = array;
        this.N_THREADS = N_THREADS;
        this.USE_BITS = USE_BITS;

        cb1 = new CyclicBarrier(N_THREADS + 1);
        cb2 = new CyclicBarrier(N_THREADS);

        SIZE_OF_SEGMENT = N_ITEMS / N_THREADS;
        REMAINDER = N_ITEMS % N_THREADS;
        start = 0;

        threads = new Thread[REMAINDER + (N_THREADS - REMAINDER)];

        countList = new int[N_THREADS][];
        arrayCopy = new int[array.length];
        digitPointList = new int[N_THREADS][];

        initVariables();
    }


    /**
     * Sort the given array through a parallel radix sort
     */
    public void radixSort() {
        for (Thread thread : threads) thread.start();

        // Synchronize threads when done - wait for N_THREADS + 1 (aka all threads, including main)
        sync(cb1);
    }


    /**
     * Synchronize threads using a Cyclic Barrier - waits for all threads to reach a point and then continues
     */
    public void sync(CyclicBarrier cb) {
        try {
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Class that sorts parts of the array in the thread assigned
     */
    private class Worker implements Runnable {
        /**
         * Constant variables that contain which number of thread it is, and sorting bounds
         */
        private final int id, start, end;

        /**
         * Arrays used in the sorting process
         */
        private int[] array, arrayCopy, digitPointer;

        /**
         * Constructs the worker class on thread initialization
         *
         * @param id        the index of the thread this worker is in
         * @param array     the array to be sorted
         * @param arrayCopy the modified copy of the array to be sorted
         * @param start     the start index to sort at
         * @param end       the end index to sort at
         */
        private Worker(int id, int[] array, int[] arrayCopy, int start, int end) {
            this.id = id;
            this.array = array;
            this.arrayCopy = arrayCopy;
            this.start = start;
            this.end = end;
        }


        /**
         * "Meat" of the radix sort:
         * <p>
         * Algorithm used:
         * 1. Count digit values in array[]
         * 2. Sums up accumulated values
         * 3. Move numbers form array[] to arrayCopy[]
         */
        public void run() {
            // Establish the maximum value used in the array
            int maxValue = Integer.MAX_VALUE - 1;

            // How bit shift operator works: takes binary number and moves the number
            // second argument left. >> does the reverse, but still uses the second arg.
            int maxBits = 0;
            while (maxValue >= 1L << maxBits) maxBits += 1;

            // Determine the number of digits to analyze
            int digits = Math.max(1, maxBits / USE_BITS);
            int[] digitLen = new int[digits];

            // Splits the number into two parts
            int remainder = maxBits % USE_BITS;
            int bits = maxBits / digits;

            int index = digitLen.length - 1;
            // Loop through the digit array to assign the non-remainder value to digitLen
            for (int i = 0; i < index; i++)
                digitLen[i] = bits;

            // Assign to the end the array the sum of the dividend and remainder
            digitLen[index] = bits + remainder;


            int shift = 0;

            for (int j : digitLen) {
                // Ex. if j=2 then 1 << 2 = 4
                int length = (1 << j);

                int mask = length - 1;
                int[] count = new int[length];

                for (int i = start; i < end; i++)
                    count[array[i] >>> shift & mask]++;


                countList[id] = count;
                digitPointer = new int[count.length];
                digitPointList[id] = digitPointer;

                sync(cb2);

                // Sums up accumulated values
                int sum = 0;
                for (int i = 0; i < count.length; i++) {
                    for (int k = 0; k < countList.length; k++) {
                        digitPointList[k][i] = sum;
                        sum += countList[k][i];
                    }
                }
                sync(cb2);

                // Move numbers form a[] to b[]
                for (int idx = start; idx < end; idx++) {
                    int m = digitPointer[(array[idx] >>> shift) & mask]++;
                    arrayCopy[m] = array[idx];
                }

                sync(cb2);

                var temp = array;
                array = arrayCopy;
                arrayCopy = temp;
                shift += j;
            }

            System.arraycopy(array, start, arrayCopy, start, end - start);
            sync(cb1);
        }
    }

    private void initVariables() {
        char idx = 0;
        for (int i = 0; i < REMAINDER; i++) {
            int end = start + SIZE_OF_SEGMENT + 1;
            threads[idx] = new Thread(new Worker(i, array, arrayCopy, start, end));
            start += SIZE_OF_SEGMENT + 1;
            idx++;
        }

        for (int j = REMAINDER; j < N_THREADS; j++) {
            int end = start + SIZE_OF_SEGMENT;
            threads[idx] = new Thread(new Worker(j, array, arrayCopy, start, end));
            start += SIZE_OF_SEGMENT;
            idx++;
        }
    }
}

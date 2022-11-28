package radix_sorting;

import java.util.concurrent.CyclicBarrier;

public class ParallelRadixSortUpgraded {

    private final int N_THREADS, sizeOfSegment, remainder, USE_BITS;
    private final int[] array, arrayCopy;
    private final int[][] countList, digPointList;
    private final CyclicBarrier cb1, cb2;
    private int start;


    public ParallelRadixSortUpgraded(int[] a, int N_THREADS, int USE_BITS, int N_ITEMS) {
        this.array = a;
        this.N_THREADS = N_THREADS;
        this.USE_BITS = USE_BITS;

        arrayCopy = new int[N_ITEMS];
        countList = new int[N_THREADS][];
        digPointList = new int[N_THREADS][];

        cb1 = new CyclicBarrier(N_THREADS + 1);
        cb2 = new CyclicBarrier(N_THREADS);

        sizeOfSegment = N_ITEMS / N_THREADS;
        remainder = N_ITEMS % N_THREADS;
        start = 0;
    }


    /**
     * Starts k number of threads
     * Give each thread a segment to sort
     */
    public void radixSort() {
        for (int i = 0; i < remainder; i++) {
            int end = start + sizeOfSegment + 1;
            new Thread(new Worker(i, array, arrayCopy, start, end)).start();
            start += sizeOfSegment + 1;
        }

        for (int j = remainder; j < N_THREADS; j++) {
            int end = start + sizeOfSegment;
            new Thread(new Worker(j, array, arrayCopy, start, end)).start();
            start += sizeOfSegment;
        }

        sync(cb1);
    }


    /* Synchronize threads using a Cyclic Barrier */
    public void sync(CyclicBarrier cb) {
        try {
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Inner Worker class for doing the parallelization
    private class Worker implements Runnable {

        private final int id, start, end;
        private int[] array, arrayCopy, digPointer;

        /* Constructor - Worker class*/
        private Worker(int id, int[] a, int[] b, int start, int end) {
            this.id = id;
            this.array = a;
            this.arrayCopy = b;
            this.start = start;
            this.end = end;
        }


        /**
         * 1. Finds maxValue in a[]
         * 2. Count digit values in a[]
         * 3. Sums up accumulated values
         * 4. Move numbers form a[] to b[]
         */
        public void run() {
            int maxValue = Integer.MAX_VALUE - 1;
            int[] digLen = getDigitsLength(maxValue);
            int shift = 0;

            for (int j : digLen) {
                int mask = (1 << j) - 1;

                // Count digit values in a[]
                int[] count = getCount(mask, array, shift);
                sync(cb2);

                // Sums up accumulated values
                sumCount(count);
                sync(cb2);

                // Move numbers form a[] to b[]
                moveNumbers(digPointer, shift, mask);
                sync(cb2);

                int[] temp = array;
                array = arrayCopy;
                arrayCopy = temp;
                shift += j;
            }

            System.arraycopy(array, start, arrayCopy, start, end - start);
            sync(cb1);
        }


        // Move numbers form a[] to b[]
        private void moveNumbers(int[] digPointer, int shift, int mask) {
            for (int index = start; index < end; index++) {
                int m = digPointer[(array[index] >>> shift) & mask]++;
                arrayCopy[m] = array[index];
            }
        }


        // Get digit length of maxValue
        private int[] getDigitsLength(int maxValue) {
            int maxBits = 0;
            while (maxValue >= 1L << maxBits) {
                maxBits += 1;
            }
            int digits = Math.max(1, maxBits / USE_BITS);
            int[] digLen = new int[digits];
            int rest = maxBits % USE_BITS;
            int bits = maxBits / digits;

            for (int i = 0; i < digLen.length - 1; i++) {
                digLen[i] = bits;
            }
            int index = digLen.length - 1;
            digLen[index] = bits + rest;

            return digLen;
        }


        /* Count digit values in a[] */
        private int[] getCount(int mask, int[] a, int shift) {
            int length = mask + 1;
            int[] count = new int[length];

            for (int i = start; i < end; i++) {
                count[a[i] >>> shift & mask]++;
            }

            countList[id] = count;
            digPointer = new int[count.length];
            digPointList[id] = digPointer;
            return count;
        }


        /* Sums up accumulated values in countList */
        private void sumCount(int[] count) {
            int sum = 0;
            for (int i = 0; i < count.length; i++) {
                for (int j = 0; j < countList.length; j++) {
                    digPointList[j][i] = sum;
                    sum += countList[j][i];
                }
            }
        }
    }
}

package radix_sorting;

import java.util.concurrent.CyclicBarrier;
/**
 * ParallelRadixSort class
 */
public class ParallelRadixSort {

    int k;
    int maxValue;
    int useBits;
    int[] a;
    int[] b;
    int[][] countList;
    int[][] digPointList;
    CyclicBarrier cb1;
    CyclicBarrier cb2;



    /**
     * Constructor
     * @param a - array to be sorted
     * @param k - number of threads to be used
     * @param useBits - number of bits to use
     */
    public ParallelRadixSort(int[] a, int k, int useBits) {
        this.a = a;
        this.b = new int[a.length];
        this.k = k;
        this.useBits = useBits;
        this.countList = new int[k][];
        this.digPointList = new int[k][];
        cb1 = new CyclicBarrier(k + 1);
        cb2 = new CyclicBarrier(k);
    }



    /**
     * Starts k number of threads
     * Give each thread a segment to sort
     */
    public void radixSort() {
        int sizeOfSegment = a.length / k;
        int remainder = a.length % k;
        int start = 0;

        for (int i = 0; i < remainder; i++) {
            int end = start + sizeOfSegment + 1;
            new Thread(new Worker(i, a, b, start, end)).start();
            start += sizeOfSegment + 1;
        }

        for (int j = remainder; j < k; j++) {
            int end = start + sizeOfSegment;
            new Thread(new Worker(j, a, b, start, end)).start();
            start += sizeOfSegment;
        }

        sync(cb1);
    }



    /* Synchronize threads using a Cyclic Barrier */
    public void sync(CyclicBarrier cb){
        try {
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /* Inner Worker class for doing the parallelization */
    private class Worker implements Runnable {

        int id;
        int start;
        int end;
        int[] a;
        int[] b;
        int[] digPointer;

        /* Constructor - Worker class*/
        private Worker(int id, int[] a, int[] b, int start, int end) {
            this.id = id;
            this.a = a;
            this.b = b;
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
            // Finds max value in a[]
            int maxValue = getMax();
            int[] digLen = getDigitsLength(maxValue);
            int shift = 0;

            for (int i = 0; i < digLen.length; i++) {
                int mask = (1 << digLen[i]) - 1;

                // Count digit values in a[]
                int[] count = getCount(mask, a, shift);
                sync(cb2);

                // Sums up accumulated values
                sumCount(count);
                sync(cb2);

                // Move numbers form a[] to b[]
                moveNumbers(digPointer, shift, mask);
                sync(cb2);

                // Copy array a[] and b[]
                swapArrays();
                shift += digLen[i];
           }

            // Copy array a[] into b[]
            copyArray();
            sync(cb1);
        }



        /* Swap arrays a[] and b[] */
        private void swapArrays(){
            int[] temp = a;
            a = b;
            b = temp;
        }



        /* Move numbers form a[] to b[] */
        private void moveNumbers(int[] digPointer, int shift, int mask){
            for (int index = start; index < end; index++) {
                int m = digPointer[(a[index] >>> shift) & mask]++;
                b[m] = a[index];
            }
        }



        /* Get digit length of maxValue */
        private int[] getDigitsLength(int maxValue){
            int maxBits = 0;
            while (maxValue >= 1L << maxBits){
                maxBits += 1;
            }
            int digits = Math.max(1, maxBits / useBits);
            int[] digLen = new int[digits];
            int rest = maxBits % useBits;
            int bits = maxBits / digits;

            for (int i = 0; i < digLen.length - 1; i++) {
                digLen[i] = bits;
            }
            int index = digLen.length - 1;
            digLen[index] = bits + rest;

            return digLen;
        }



        /* Count digit values in a[] */
        private int[] getCount(int mask, int[] a, int shift){
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
        private void sumCount(int[] count){
            int sum = 0;
            for (int i = 0; i < count.length; i++) {
                for (int j = 0; j < countList.length; j++) {
                    digPointList[j][i] = sum;
                    sum += countList[j][i];
                }
            }
        }



        /* Copy thread-segment of a[] into b[] */
        private void copyArray(){
            for(int j = start; j < end; j++){
                b[j] = a[j];
            }
        }



        /**
         * Get max value in a[]
         * Iterate through segment and compare to global maxValue
         * Waits for threads to synchronize and returns maxValue
         */
        private int getMax() {
            int segMax = a[start];
            for (int i = start; i < end; i++) {
                if (a[i] > segMax){
                    segMax = a[i];
                }
            }
            if(segMax > maxValue){
                maxValue = segMax;
            }
            sync(cb2);
            return maxValue;
        }
    }
}

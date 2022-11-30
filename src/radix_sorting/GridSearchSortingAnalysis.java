package radix_sorting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GridSearchSortingAnalysis {
    private static final int N_ITEMS = 1_000_000_000;

    /**
     * Number of trials used when analyzing a config of bits used and threads used
     */
    private static final char N_TRIALS = 5;


    // Bounds of specifications that will be analyzed, inclusive on both ends
    private static final char MIN_N_THREADS = 9, MAX_N_THREADS = 16;
    private static final char MIN_USE_BITS = 7, MAX_USE_BITS = 10;

    /*
        RECORD DATA:
        ------------

        SCHOOL COMPUTER:
        ------
        BEST TIME 100M = 269ms;
        BEST TIME 1B = 2783ms;
        BEST AVERAGE TIME ACROSS 10 TRIALS 100M = 283.1ms;
        BEST AVERAGE TIME ACROSS 10 TRIALS 1B = 2828ms;

        USE 12 THREADS 1B
        USE 4 THREADS 100M

        HOME COMPUTER (SLOWER):
        ------
        BEST TIME 1M: 6ms (14 THREADS, 6 BITS)
        BEST TIME 10M: 115ms (10 THREADS, 6 BITS)
    */

    public static void main(String[] args) throws IOException {

        // Create a new file labeled with the time since the epoch in ms
//        var fstream = new FileWriter("C:\\Users\\zenith\\IdeaProjects\\Sorting\\src\\radix_sorting\\data\\gridsearch_run_" + System.currentTimeMillis() + ".txt");
        var fstream = new FileWriter("C:\\Users\\lindauer_927142\\IdeaProjects\\ExoticSortingJava\\src\\radix_sorting\\data\\run_" + System.currentTimeMillis() + ".txt");

        var fileWriter = new BufferedWriter(fstream);

        // Label the processor statistics the new file
        Util.writeMachineStatistics(fileWriter);

        // Store data pertaining to the average, min, st. dev of the current run and threads/bits used to achieve it
        double[] data = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0};

        // Loop through every combo of threads and bits given through the constants specified
        for (short threads = MIN_N_THREADS; threads <= MAX_N_THREADS; threads++) {
            for (short bits = MIN_USE_BITS; bits <= MAX_USE_BITS; bits++) {
                var times = averageOfRuns(threads, bits);
                var average = avg(times);
                var minimum = min(times);
                var stDev = standardDeviation(times);

                // If this run is the new best, write data to the array and print it/write to file
                // Measuring best by the average across N_TRIALS of runs in this case
                if (average < data[0]) {
                    data[0] = average;
                    data[1] = minimum;
                    data[2] = stDev;
                    data[3] = threads;
                    data[4] = bits;

                    String writeString = "Items sorted: " + N_ITEMS + ", Threads: " + threads + ", Bits used: " + bits + ", run average: " + average + "ms, st. dev: " + stDev + "ms, min: " + minimum + "ms";
                    System.out.println(writeString);
                    fileWriter.write(writeString);
                    fileWriter.newLine();
                }
                System.out.println("Iteration complete");
            }
        }

        // Print telemetry on the best configuration of threads / bits used and the timing data from it
        String writeString = "Best configuration: Threads: " + data[3] + ", Bits used: " + data[4] + ", run average: " + data[0] + "ms, st. dev: " + data[2] + "ms, min: " + data[1] + "ms";
        System.out.println(writeString);
        fileWriter.write(writeString);
        fileWriter.newLine();

        fileWriter.close();
    }

    /**
     * Calculates the average, min, and st. dev of sorting based on the given threads and use bits value
     *
     * @param nThreads number of threads to test with
     * @param useBits  number of bits to apply before remainder
     * @return an array of the average, min, and st. dev values
     */
    private static short[] averageOfRuns(int nThreads, short useBits) {
        var times = new short[N_TRIALS];
        var array = new int[N_ITEMS];

        // Run N_TRIALS of the sorting algorithm, averaging and finding the min to write to the file and print to the screen
        for (int i = 0; i < N_TRIALS; i++) {
            Util.randomizeArray(array, Integer.MAX_VALUE - 1);
            // Instantiate the sorting algorithm. Creates some int variables but nothing too large, sorting runs on .radixSort()

            // Run the sorting algorithm
            var before = System.currentTimeMillis();
            var sorting = new ParallelRadixSort(array, nThreads, useBits, N_ITEMS);
            sorting.radixSort();
            var after = System.currentTimeMillis();

            times[i] = (short) (after - before);

        }

        return times;
    }

    /*
        Various helper functions to calculate the min, avg, and st. dev from an array of shorts
     */

    private static int min(short... values) {
        int min = values[0];
        for (int i = 1; i < values.length; i++)
            if (values[i] < min)
                min = values[i];
        return min;
    }

    private static double avg(short... values) {
        int sum = 0;
        for (short item : values)
            sum += item;
        return sum / (double) values.length;
    }

    private static double standardDeviation(short... values) {
        double summation = 0;
        double mean = avg(values);
        for (short item : values)
            summation += (item - mean) * (item - mean);
        return Math.sqrt(summation / (values.length));
    }
}

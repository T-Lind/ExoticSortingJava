package radix_sorting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BasicSortingAnalysis {
    private static final int N_ITEMS = 1_000_000;
    private static final short N_TRIALS = 10;
    private static final short N_THREADS = 12;
    private static final short USE_BITS = 6;

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
        var times = new short[N_TRIALS];
        var array = new int[N_ITEMS];

        // Home computer filepath
//        var fstream = new FileWriter("C:\\Users\\zenith\\IdeaProjects\\Sorting\\src\\radix_sorting\\data\\run_" + System.currentTimeMillis() + ".txt");
        // School laptop filepath
        var fstream = new FileWriter("Z:\\My Drive\\ComputerScience\\IdeaProjects\\ExoticSortingJava\\src\\radix_sorting\\data\\run_"+System.currentTimeMillis()+".txt");
        var fileWriter = new BufferedWriter(fstream);

        // Label the processor statistics in a new file that's created with the name as the current time in milliseconds
        final var MB = 1024 * 1024;
        var rt = Runtime.getRuntime();
        fileWriter.write("Machine statistics:");
        fileWriter.newLine();
        fileWriter.write("Max memory: " + rt.maxMemory() / MB + "MB, Available processors: " + rt.availableProcessors());
        fileWriter.newLine();
        fileWriter.newLine();

        // Run N_TRIALS of the sorting algorithm, averaging and finding the min to write to the file and print to the screen
        for (int i = 0; i < N_TRIALS; i++) {
            UtilOperations.randomizeArray(array, Integer.MAX_VALUE - 1);
            // Instantiate the sorting algorithm. Creates some int variables but nothing too large, sorting runs on .radixSort()
            var sorting = new ParallelRadixSort(array, N_THREADS, USE_BITS, N_ITEMS);

            // Run the sorting algorithm
            var before = System.currentTimeMillis();
            sorting.radixSort();
            var after = System.currentTimeMillis();

            times[i] = (short) (after - before);

            // Write the run's data right now
            fileWriter.write("Items sorted: " + N_ITEMS + " Threads: " + N_THREADS + " Bits: " + USE_BITS + " Trials: " + N_TRIALS + " Time: " + times[i] + "ms");
            fileWriter.newLine();
        }
        double min = min(times);
        double average = avg(times);
        double stDev = standardDeviation(times);

        // Write the min and average to the file
        fileWriter.write("Minimum time of " + min + "ms, average time of: " + average + "ms, st. dev: "+stDev);

        // Telemetry printout
        System.out.println("Minimum time of " + min + "ms");
        System.out.println("Average time of " + average + "ms");
        System.out.println("Standard deviation of " + stDev + "ms");
        System.out.println("Comprehensive sorting check returned: " + UtilOperations.isSorted(array, N_ITEMS, fileWriter));
        fileWriter.close();
    }

    public static int min(short... values) {
        int min = values[0];
        for (int i = 1; i < values.length; i++)
            if (values[i] < min)
                min = values[i];
        return min;
    }

    public static double avg(short... values) {
        int sum = 0;
        for (short item : values)
            sum += item;
        return sum / (double) values.length;
    }

    public static double standardDeviation(short... values) {
        double summation = 0;
        double mean = avg(values);
        for(short item : values)
            summation += (item - mean)*(item - mean);
        return Math.sqrt(summation/(values.length));
    }
}

package radix_sorting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Various common utility functions used throughout sorting programs
 */
public class Util {
    /**
     * Used for calculating machine statistics
     */
    private static final int MB = 1024 * 1024;

    static void randomizeArray(int[] input, int MAX_NUM) {
        for (int i = 0; i < input.length; i++)
            input[i] = ThreadLocalRandom.current().nextInt(0, MAX_NUM);
    }

    /**
     * Write memory available to use and processors to use on this machine,
     * gives context to the run data and helps differentiate the best runs
     *
     * @param fileWriter the BufferedWriter object created to write to a file in /data
     * @throws IOException occurs if writing operation failed
     */
    static void writeMachineStatistics(BufferedWriter fileWriter) throws IOException {
        var rt = Runtime.getRuntime();
        fileWriter.write("Machine statistics:");
        fileWriter.newLine();
        fileWriter.write("Max memory: " + rt.maxMemory() / MB + "MB, Available processors: " + rt.availableProcessors());
        fileWriter.newLine();
        fileWriter.newLine();
    }

    /**
     * Perform a comprehensive sort analyzing the regular ascending order, how many zeros are present
     * (ex. all zeros will return a pass to a normal ascending order b/c of <= operator)
     * Also checks how many mis-sorts there are and prints to file & screen the location and contents if there are any
     * - Only prints first 10 of these errors
     *
     * @param array   the array that supposedly is sorted
     * @param N_ITEMS the number of items that have been sorted
     * @param writer  the fileWriter object used to store test results in
     * @return a boolean of if the sort passed
     */
    static boolean isSorted(int[] array, int N_ITEMS, BufferedWriter writer) throws IOException {
        writer.write("Test results:");
        writer.newLine();

        int nIncorrect = 0;
        int zeroCtr = 0;
        boolean overflowError = false;
        for (int i = 1; i < N_ITEMS; i++) {
            if (array[i] < array[i - 1]) {
                if (nIncorrect <= 10) {
                    String writeString = "Index: " + i + ", values: " + array[i - 1] + " " + array[i];
                    writer.write(writeString);
                    writer.newLine();

                    System.out.println(writeString);
                    nIncorrect++;
                } else if (!overflowError) {
                    writer.write("> 10 mis-sorts in the array! Not printing further mis-sorts.");
                    writer.newLine();
                }


            }
            if (array[i] == 0)
                zeroCtr++;
        }
        if (zeroCtr == array.length) {
            String writeString = "The array is all zeros!";
            writer.newLine();
            writer.write(writeString);
            System.out.println(writeString);
            return false;
        }

        if (nIncorrect == 0) {
            writer.write("Test passed completely!");
            writer.newLine();
            return true;
        }
        String writeString = "Number of mis-sorts: " + nIncorrect;
        writer.write(writeString);
        System.out.println(writeString);
        return false;
    }

    /**
     * Deprecated -  use the version with the fileWriter to log data appropriately
     * <p>
     * Perform a comprehensive sort analyzing the regular ascending order, how many zeros are present
     * (ex. all zeros will return a pass to a normal ascending order b/c of <= operator)
     * Also checks how many mis-sorts there are and prints to JUST screen the location and contents if there are any
     * - Prints ALL of the errors present in the sorted array - can cause MACHINE LAG
     *
     * @param array   the array that supposedly is sorted
     * @param N_ITEMS the number of items that have been sorted
     * @return a boolean of if the sort passed
     */
    @Deprecated
    static boolean isSorted(int[] array, int N_ITEMS) {
        int nIncorrect = 0;
        int zeroCtr = 0;
        for (int i = 1; i < N_ITEMS; i++) {
            if (array[i] < array[i - 1]) {
                System.out.println("Index: " + i + ", values: " + array[i - 1] + " " + array[i]);
                nIncorrect++;

            }
            if (array[i] == 0)
                zeroCtr++;
        }
        if (zeroCtr == array.length) {
            System.out.println("Oh no! The array is all zeros!");
            return false;
        }

        if (nIncorrect == 0)
            return true;
        System.out.println("Number of mis-sorts: " + nIncorrect);
        return false;
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

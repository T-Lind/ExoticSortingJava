package radix_sorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RunSorting {
    private static final int N_ITEMS = 1_000_000_000;
    private static final short N_TRIALS = 1;

    private static final short N_THREADS = 12;
    private static final short USE_BITS = 12;

    /*
    RECORD DATA:

    BEST TIME 100M = 269ms;
    BEST TIME 1B = 2783ms;
    BEST AVERAGE TIME ACROSS 10 TRIALS 100M = 283.1ms;
    BEST AVERAGE TIME ACROSS 10 TRIALS 1B = 2828ms;

    USE 12 THREADS 1B
    USE 4 THREADS 100M
    */

    public static void main(String[] args) throws IOException {
        var times = new short[N_TRIALS];
        var array = new int[N_ITEMS];

        var filename = new File("C:\\\\Users\\\\lindauer_927142\\\\Downloads\\\\ExoticSortingJava-master\\\\src\\\\radix_sorting\\\\run_info.txt");
        var scan = new Scanner(filename);

        var totalData = new ArrayList<String>();
        while (scan.hasNextLine())
            totalData.add(scan.nextLine());
        scan.close();

        var fstream = new FileWriter("C:\\Users\\lindauer_927142\\Downloads\\ExoticSortingJava-master\\src\\radix_sorting\\run_info.txt");
        var fileWriter = new BufferedWriter(fstream);

        for (String line : totalData) {
            fileWriter.write(line);
            fileWriter.newLine();
        }

        for (int i = 0; i < N_TRIALS; i++) {
            ArrayOperations.randomizeArray(array, N_ITEMS, Integer.MAX_VALUE - 1);
            var sorting = new ParallelRadixSortUpgraded(array, N_THREADS, USE_BITS, N_ITEMS);

            var before = System.currentTimeMillis();
            sorting.radixSort();
            var after = System.currentTimeMillis();

            times[i] = (short) (after - before);

            var ret_str = "Items sorted: " + N_ITEMS + " Threads: " + N_THREADS + " Bits:" + USE_BITS + " Trials: " + N_TRIALS + " Time: " + times[i] + "ms";
            fileWriter.write(ret_str);
            fileWriter.newLine();
        }
        fileWriter.close();

        System.out.println("Minimum time of " + min(times) + "ms");
        System.out.println("Average time of " + avg(times) + "ms");

        System.out.println("Thorough sorting check returned: " + ArrayOperations.isSorted(array, N_ITEMS));
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
}

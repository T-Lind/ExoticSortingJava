package counting_sort;

import java.util.concurrent.ThreadLocalRandom;

public class ArrayOperations {
    static void randomizeArray(int[] input, int N_ITEMS, int MAX_NUM) {
        for(int i = 0; i < N_ITEMS; i++)
            input[i] = ThreadLocalRandom.current().nextInt(0, MAX_NUM);
    }

    static boolean isSorted(int[] array, int N_ITEMS) {
        int nIncorrect = 0;
        int zeroCtr = 0;
        for (int i = 1; i < N_ITEMS; i++) {
            if (array[i] < array[i - 1] && array[i] > 0 && nIncorrect < 10) {
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
}

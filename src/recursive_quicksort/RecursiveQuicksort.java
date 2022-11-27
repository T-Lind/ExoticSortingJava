package recursive_quicksort;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class RecursiveQuicksort {
    int[] list;
    int threshold;
    int start;
    int end;

    public RecursiveQuicksort(int[] a, int threshold) {
        this.list = a;
        this.threshold = threshold;
        this.start = 0;
        this.end = list.length - 1;
    }

    public static void startMainTask(int[] list) {
        RecursiveAction mainTask = new SortTask(list);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
    }

    private static class SortTask extends RecursiveAction {
        private int[] list;
        private int start;
        private int end;

        public SortTask(int[] list) {
            this.list = list;
            start = 0;
            end = list.length - 1;
        }

        public SortTask(int[] list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        protected void compute() {
            if (start < end) {
                int pivot = partition(list, start, end);
                invokeAll(new SortTask(list, start, pivot), new SortTask(list, pivot + 1, end));
            }

        }

        private int partition(int[] array, int low, int high) {
            int pivot = array[low];
            int i = low - 1;
            int j = high + 1;
            while (true) {
                do {
                    i++;
                } while (array[i] < pivot);
                do {
                    j--;
                } while (array[j] > pivot);
                if (i >= j) return j;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }
}
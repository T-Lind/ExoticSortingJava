package radix_sorting;

public class Sorting {
    private static int N_ITEMS = 100_000_000;

    public static void main(String[] args) {
        int min = 302;
        var array = new int[N_ITEMS];

        for(int i=0;i<10;i++){
            ArrayOperations.randomizeArray(array, N_ITEMS, Integer.MAX_VALUE - 1);
            var sorting = new ParallelRadixSort(array, 4, 8);
            long before = System.currentTimeMillis();
            sorting.radixSort();
            long after = System.currentTimeMillis();
            min = Math.min(min, (int)(after-before));
        }
        System.out.println(min);

//        System.out.println(ArrayOperations.isSorted(array, N_ITEMS));
    }
}

// Java Program to implement merge sort using
// multi-threading
import java.util.Random;


public class MergeSortDriver {
    public static void main(String[] args){
        Random random = new Random();
        int size = 10_000_000;
        Integer[] list = new Integer[size];
        for(int i=0; i<size; i++){
            list[i] = random.nextInt();
        }
        var before = System.currentTimeMillis();
//        Arrays.sort(list);
//        Arrays.parallelSort(list);
        MergeSort.mergeSort(list, 0, size-1);
        var after = System.currentTimeMillis();
        System.out.println(after-before+" milliseconds");
    }
}

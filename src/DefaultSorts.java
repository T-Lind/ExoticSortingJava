import java.util.Arrays;
import java.util.Random;

public class DefaultSorts {
    public static void main(String[] args){
        var random = new Random();
        int size = 10_000_000;
        Integer[] list = new Integer[size];
        for(int i=0; i<size; i++){
            list[i] = random.nextInt();
        }
        var list2 = Arrays.copyOf(list, list.length);


        var before = System.currentTimeMillis();
        Arrays.sort(list);
        var after = System.currentTimeMillis();
        System.out.println(after-before+" milliseconds");

        before = System.currentTimeMillis();
        Arrays.parallelSort(list2);
        after = System.currentTimeMillis();
        System.out.println(after-before+" milliseconds");
    }
}

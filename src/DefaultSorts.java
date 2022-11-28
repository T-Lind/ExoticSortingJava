import java.util.Arrays;

public class DefaultSorts {
    public static void main(String[] args){
        int size = 10_000_000;
        int max_num = 10_000_000;
        var list = new int[size];

        ArrayOperations.randomizeArray(list, size, max_num);

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

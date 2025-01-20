package K1_exercises.ex14;

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{

    public MinMax() {
    }

    T max;
    T min;
    public void update(T element) {
        if (min == null || element.compareTo(min) < 0){
            min = element;
        }
        if (max == null || element.compareTo(max) > 0){
            max = element;
        }
    }

//    @Override
////    public String toString() {
////        return String.format("")
////    }

    public T max(){
        return max;
    }

    public T min(){
        return min;
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
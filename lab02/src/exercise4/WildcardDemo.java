package exercise4;

import java.util.List;

public class WildcardDemo {

    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    public static double sumNumbers(List<? extends Number> numbers) {
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        List<String> strings = List.of("a", "b", "c");
        printList(strings);

        List<Integer> ints = List.of(1, 2, 3);
        printList(ints);
        System.out.println("Sum of ints: " + sumNumbers(ints));

        List<Double> doubles = List.of(1.5, 2.5, 3.5);
        System.out.println("Sum of doubles: " + sumNumbers(doubles));
    }
}

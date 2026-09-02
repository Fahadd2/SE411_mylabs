package exercise2;

import java.util.List;

public class NumberBox<T extends Number> {

    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public double add(T other) {
        return item.doubleValue() + other.doubleValue();
    }

    public static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.setItem(10);
        System.out.println("Integer item: " + intBox.getItem());
        System.out.println("10 + 5 = " + intBox.add(5));

        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.setItem(3.5);
        System.out.println("Double item: " + doubleBox.getItem());
        System.out.println("3.5 + 2.5 = " + doubleBox.add(2.5));

        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        System.out.println("Sum of ints: " + sum(ints));

        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        System.out.println("Sum of doubles: " + sum(doubles));
    }
}

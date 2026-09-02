package exercise1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintableList<T> {

    private final List<T> items;

    public PrintableList(T[] itemsArray) {
        this.items = new ArrayList<>(Arrays.asList(itemsArray));
    }

    public void printAll() {
        for (T item : items) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        String[] names = {"Alice", "Bob", "Charlie"};
        PrintableList<String> stringList = new PrintableList<>(names);
        stringList.printAll();

        Integer[] numbers = {10, 20, 30};
        PrintableList<Integer> intList = new PrintableList<>(numbers);
        intList.printAll();
    }
}

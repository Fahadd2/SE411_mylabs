package exercise3;

import java.util.function.Function;

public class Pipeline<T, R> {

    private final T input;
    private final Function<T, R> transformation;

    private Pipeline(T input, Function<T, R> transformation) {
        this.input = input;
        this.transformation = transformation;
    }

    public static <T> Pipeline<T, T> start(T input) {
        return new Pipeline<>(input, Function.identity());
    }

    public <NewR> Pipeline<T, NewR> addTransformer(Transformer<R, NewR> transformer) {
        return new Pipeline<>(input, transformation.andThen(transformer::transform));
    }

    public R execute() {
        return transformation.apply(input);
    }
}

package exercise3;

public class PipelineDemo {

    public static void main(String[] args) {
        // Same-type (in-place) transformations followed by a type-changing one.
        Pipeline<String, Integer> wordCountPipeline = Pipeline.<String>start("  Hello World  ")
                .addTransformer(String::trim)
                .addTransformer(String::toUpperCase)
                .addTransformer(s -> s.split(" ").length);

        System.out.println("Word count: " + wordCountPipeline.execute());

        // Chain of type-changing transformations: Integer -> Integer -> String.
        Pipeline<Integer, String> numberPipeline = Pipeline.<Integer>start(5)
                .addTransformer(n -> n * n)
                .addTransformer(n -> "Result: " + n);

        System.out.println(numberPipeline.execute());
    }
}

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FizzBuzzKataTDD {

    public static String generateFizzBuzzNumbers(int number) {
        StringBuilder result = new StringBuilder();

        if (number % 3 == 0) {
            result.append("Fizz");
        }
        if (number % 5 == 0) {
            result.append("Buzz");
        }

        return result.length() == 0 ? String.valueOf(number) : result.toString();
    }

    public static List<String> getResultList() {
        return IntStream.range(1, 101)
                .mapToObj(FizzBuzzKataTDD::generateFizzBuzzNumbers)
                .collect(Collectors.toList());

    }
}

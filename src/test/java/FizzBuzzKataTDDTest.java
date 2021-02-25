import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FizzBuzzKataTDDTest {


    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, Fizz",
            "6, Fizz",
            "5, Buzz",
            "10, Buzz",
            "15, FizzBuzz",
            "30, FizzBuzz"
    })
    void testGenerateFizzBuzzNumbers(int input, String expected){
        assertEquals(expected, FizzBuzzKataTDD.generateFizzBuzzNumbers(input));
    }

    @Test
    void testRange(){
        List<String> list = FizzBuzzKataTDD.getResultList();
        assertEquals(100, list.size());
    }
    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, Fizz",
            "6, Fizz",
            "5, Buzz",
            "10, Buzz",
            "15, FizzBuzz",
            "30, FizzBuzz"
    })
    void testFizzInList(int input, String expected){
        assertEquals(expected, FizzBuzzKataTDD.getResultList().get(input - 1));
    }
}



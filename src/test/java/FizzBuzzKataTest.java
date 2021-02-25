import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FizzBuzzKataTest {

    @Test
    void testFizz()
    {
        assertEquals("Fizz", FizzBuzzKata.generateFizzBuzz(3));
    }

    @Test
    void testBuzz()
    {
        assertEquals("Buzz", FizzBuzzKata.generateFizzBuzz(5));
    }

    @Test
    void testFizzBuzz()
    {
        assertEquals("FizzBuzz", FizzBuzzKata.generateFizzBuzz(15));
    }

    @Test
    void testNumber()
    {
        assertEquals("1", FizzBuzzKata.generateFizzBuzz(1));
    }
}

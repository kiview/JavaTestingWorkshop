import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StringCalculatorTest {
    Logger logger = mock(Logger.class);
    StringCalculator.WebService webService = mock(StringCalculator.WebService.class);
    StringCalculator stringCalculator = new StringCalculator(logger, webService);

    @ParameterizedTest
    @CsvSource(delimiter = '#', value = {
            "0# ''",
            "1# 1",
            "4# 2,2",
            "42# 20,20,2"
    })
    void testAdd(int expected, String input) {
        assertEquals(expected, stringCalculator.add(input));
    }

    @Test
    void testDelimiter() {
        // act
        var delim = stringCalculator.parseDelimiter("//!\\n");

        // assert
        assertEquals('!', delim);
    }

    @Test
    void testAddWithDelimiter(){
        assertEquals(5, stringCalculator.add("//!\\n2!3"));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '#', value = {
            "0# ''",
            "1# 1",
            "4# 2,2",
            "42# 20,20,2"
    })
    void testLogging(int expected, String input) {
        // act
        stringCalculator.add(input);

        // assert
        verify(logger).write(String.valueOf(expected));
    }

    @Test
    void testNotifyWebservice() {
        // arrange | given
        var mockWebService = mock(StringCalculator.WebService.class);
        String errorMessage = "Oh noes!";
        doThrow(new RuntimeException(errorMessage)).when(logger).write("1");
        stringCalculator = new StringCalculator(logger, mockWebService);

        // act | when
        stringCalculator.add("1");

        // assert | then
        verify(mockWebService).notify(errorMessage);
    }
}

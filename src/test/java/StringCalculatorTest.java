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
        assertEquals('!', stringCalculator.parseDelimiter("//!\\n"));
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
        stringCalculator.add(input);
        verify(logger).write(String.valueOf(expected));
    }

    @Test
    void testNotifyWebservice() {
        var mockWebService = mock(StringCalculator.WebService.class);
        String errorMessage = "Oh noes!";
        doThrow(new RuntimeException(errorMessage)).when(logger).write("1");
        stringCalculator = new StringCalculator(logger, mockWebService);

        stringCalculator.add("1");

        verify(mockWebService).notify(errorMessage);
    }
}

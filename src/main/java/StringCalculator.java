import java.util.Arrays;
import java.util.regex.Pattern;

public class StringCalculator {

    private final Logger logger;
    private final WebService webService;

    public StringCalculator(Logger logger, WebService webService) {
        this.logger = logger;
        this.webService = webService;
    }

    public int add(String input) {
        if (input.isEmpty()) {
            input = "0";
        }
        String[] numbers = prepareNumbers(input);

        int sum = Arrays.stream(numbers).mapToInt(Integer::parseInt).sum();
        try {
            logger.write(String.valueOf(sum));
        } catch (RuntimeException e) {
            webService.notify(e.getMessage());
        }
        return sum;
    }

    private static String[] prepareNumbers(String input) {
        String[] rows = input.split("\\\\n");
        if (rows.length == 1) {
            return input.split(String.valueOf(parseDelimiter(input)));
        }
            return rows[1].split(String.valueOf(parseDelimiter(input)));
    }

    public static char parseDelimiter(String input) {
        Pattern pattern = Pattern.compile("//(.)\\\\n.*");
        var matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1).toCharArray()[0] : ',';
    }

    public interface WebService {
        void notify(String message);
    }
}

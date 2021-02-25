public class FizzBuzzKata {

    public static String generateFizzBuzz(int fizzBuzzCandidate){
        String output = String.valueOf(fizzBuzzCandidate);

        if (fizzBuzzCandidate % 15 == 0) {
            output = "FizzBuzz";
        } else if (fizzBuzzCandidate % 5 == 0) {
            output = "Buzz";
        } else if (fizzBuzzCandidate % 3 == 0 )
        {
            output = "Fizz";
        }

        return output;
    }

    private static void printFizzBuzzNumbers(){
        for (int i = 1; i <= 100; i++) {
            System.out.println(generateFizzBuzz(i));
        }
    }

    public static void main(String[] args) {

        printFizzBuzzNumbers();
    }
}


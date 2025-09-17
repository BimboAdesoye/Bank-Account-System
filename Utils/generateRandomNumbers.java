package Utils;
import java.util.Random;
import java.util.Set;

public final class generateRandomNumbers {
    static Random random = new Random();

    generateRandomNumbers() {};

    public static String generateNumbers(int bound, Set<String> numbersSet) {
        String numbers;
        do{
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bound; i++) {
                sb.append(random.nextInt(bound));
            }
            numbers = sb.toString();
        } while(numbersSet.contains(numbers));
        numbersSet.add(numbers);
        return numbers;
    }
}

package counter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class CharacterCounter {


    private CharacterCounter() {
    }


    public static Map<Character, Long> count(String input) {

        if (input == null || input.isEmpty()) {
            return Collections.emptyMap();
        }

        return input.chars()
                .mapToObj(unicode -> (char) unicode)
                .collect(groupingBy(Function.identity(), counting()));
    }
}

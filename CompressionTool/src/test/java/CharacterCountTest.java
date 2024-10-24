import counter.CharacterCounter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CharacterCountTest {




    @Test
    void Counter_returns_empty_map_when_empty_input(){
        Map<Character, Long> countMap = CharacterCounter.count("");

        assertTrue(countMap.isEmpty());
    }


    @Test
    void Counter_returns_empty_map_when_null_input(){
        Map<Character, Long> countMap = CharacterCounter.count(null);

        assertTrue(countMap.isEmpty());
    }


    @Test
    void Counter_returns_correct_result_when_valid_input(){

        Map<Character, Long> countMap = CharacterCounter.count("aaabcaaa");

        assertEquals(3, countMap.size());

        assertEquals(6, countMap.get('a'));
    }

}

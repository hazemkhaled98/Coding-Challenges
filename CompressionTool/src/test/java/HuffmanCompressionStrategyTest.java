import algo.huffaman.HuffmanCompressionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HuffmanCompressionStrategyTest {

    private HuffmanCompressionStrategy huffmanCompressionStrategy;

    @BeforeEach
    public void setup() {
        huffmanCompressionStrategy = new HuffmanCompressionStrategy();
    }

    @Test
     void Compress_simple_string() {
        String input = "abbccc";
        String result = huffmanCompressionStrategy.compress(input);

        assertNotNull(result);
        assertTrue(result.contains("### Huffman Codes ###"));
        assertTrue(result.contains("### Encoded Text ###"));
    }

    @Test
    void Single_Character_Repeated() {
        String input = "aaaaaa";
        String result = huffmanCompressionStrategy.compress(input);

        System.out.println(result);

        assertNotNull(result);
        assertTrue(result.contains("a"));
        assertTrue(result.contains("000000"));
    }


    @Test
    void Compress_empty_string() {
        String input = "";
        String result = huffmanCompressionStrategy.compress(input);

        assertNotNull(result);
        assertEquals("### Huffman Codes ###\n-----------------------\n-----------------------\n\n### Encoded Text ###\n\n",
                result);
    }
}

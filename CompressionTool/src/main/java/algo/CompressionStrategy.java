package algo;


public interface CompressionStrategy {

    String compress(String input);

    String decompress(String compressedText);
}

import algo.huffaman.HuffmanCompressionStrategy;
import decoder.Decoder;
import encoder.Encoder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import reader.FileReader;
import reader.Reader;
import writer.FileWriter;
import writer.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IntegrationTest {


    @Test
    void Given_valid_path_the_file_successfully_compressed() throws IOException {
        String inputFile = "src/test/resources/test.text";
        String afterEncodingFile = "src/test/resources/afterEncodingTest.txt";
        String afterDecodingFile = "src/test/resources/afterDecodingTest.txt";

        Path originalPath = Path.of(inputFile);

        Reader inputReader = new FileReader(inputFile);
        Writer inputWriter = new FileWriter(afterEncodingFile);

        Encoder encoder = new Encoder(inputReader, inputWriter);
        encoder.setStrategy(new HuffmanCompressionStrategy());
        Path afterEncodingPath = encoder.compress();

        long compressedLength = Files.size(afterEncodingPath);
        long originalLength = Files.size(originalPath);

//        assertTrue(compressedLength < originalLength,
//                "Compressed file size (" + compressedLength + " bytes) is not smaller than the original file size (" + originalLength + " bytes).");

        Reader outputReader = new FileReader(afterEncodingFile);
        Writer outputWriter = new FileWriter(afterDecodingFile);

        Decoder decoder = new Decoder(outputReader, outputWriter);
        decoder.setStrategy(new HuffmanCompressionStrategy());
        Path afterDecodingPath = decoder.decode();

        byte[] originalFileBytes = Files.readAllBytes(originalPath);
        byte[] afterDecodingBytes = Files.readAllBytes(afterDecodingPath);

        assertArrayEquals(originalFileBytes, afterDecodingBytes, "Decoding failed.");
    }
}

import algo.huffaman.HuffmanCompressionStrategy;
import decoder.Decoder;
import encoder.Encoder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import reader.CompressedReader;
import reader.FileReader;
import reader.Reader;
import writer.CompressedWriter;
import writer.FileWriter;
import writer.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EndToEndTest {


    @Test
    void End_To_End_Compress_And_Decompress() throws IOException {
        String inputFile = "src/test/resources/test.txt";
        String afterEncodingFile = "src/test/resources/afterEncodingTest.txt";
        String afterDecodingFile = "src/test/resources/afterDecodingTest.txt";

        Path originalPath = Path.of(inputFile);

        Reader encoderReader = new FileReader(inputFile);
        Writer encoderWriter = new CompressedWriter(afterEncodingFile);

        Encoder encoder = new Encoder(encoderReader, encoderWriter);
        encoder.setStrategy(new HuffmanCompressionStrategy());
        Path afterEncodingPath = encoder.compress();

        long compressedLength = Files.size(afterEncodingPath);
        long originalLength = Files.size(originalPath);

        assertTrue(compressedLength < originalLength,
                "Compressed file size (" + compressedLength + " bytes) is not smaller than the original file size (" + originalLength + " bytes).");

        Reader decoderReader = new CompressedReader(afterEncodingFile);
        Writer decoderWriter = new FileWriter(afterDecodingFile);

        Decoder decoder = new Decoder(decoderReader, decoderWriter);
        decoder.setStrategy(new HuffmanCompressionStrategy());
        Path afterDecodingPath = decoder.decode();

        byte[] originalFileBytes = Files.readAllBytes(originalPath);
        byte[] afterDecodingBytes = Files.readAllBytes(afterDecodingPath);

        assertArrayEquals(originalFileBytes, afterDecodingBytes, "Decoding failed.");
    }
}

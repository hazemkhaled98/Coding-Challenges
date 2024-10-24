package decoder;

import algo.CompressionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reader.Reader;
import writer.Writer;

import java.io.IOException;
import java.nio.file.Path;


@RequiredArgsConstructor
public class Decoder {

    private final Reader reader;
    private final Writer writer;

    @Setter
    private CompressionStrategy strategy;

    public Path decode() throws IOException {
        String compressedText = reader.readInput();
        String originalText = strategy.decompress(compressedText);
        return writer.writeOutput(originalText);
    }


}

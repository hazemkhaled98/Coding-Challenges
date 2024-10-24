package encoder;

import algo.CompressionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reader.Reader;
import writer.Writer;

import java.io.IOException;
import java.nio.file.Path;

@RequiredArgsConstructor
public class Encoder {


    private final Reader reader;
    private final Writer writer;
    @Setter
    private CompressionStrategy strategy;

    public Path compress() throws IOException {
        String input = reader.readInput();
        String compressedText = strategy.compress(input);
        return writer.writeOutput(compressedText);
    }


}

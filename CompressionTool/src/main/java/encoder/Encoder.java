package encoder;

import algo.CompressionStrategy;
import counter.CharacterCounter;
import lombok.Setter;
import reader.Reader;

import java.io.IOException;
import java.util.Map;

public class Encoder {


    private final Reader reader;
    @Setter
    private CompressionStrategy strategy;

    public Encoder(Reader reader) {
        this.reader = reader;
    }

    public String compress() throws IOException {
        String input = reader.read();
        return strategy.compress(input);
    }


}

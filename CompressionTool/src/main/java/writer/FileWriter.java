package writer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriter implements Writer{

    private final String fileName;


    public FileWriter(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public Path writeOutput(String text) throws IOException {
        Path path = Path.of(fileName);
        return Files.writeString(path, text, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

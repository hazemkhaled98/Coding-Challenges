package reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader implements Reader{

    private final String fileName;


    public FileReader(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public String readInput() throws IOException {
        return Files.readString(Path.of(fileName));
    }
}

package lexer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileReader {


    private FileReader() {}


    public static String readJson(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}

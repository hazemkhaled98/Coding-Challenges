import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import reader.FileReader;
import reader.Reader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileReaderTest {

    Reader reader;


    @Test
    void Reader_throws_IOException_when_cant_open_file() {

        reader = new FileReader("invalid_file.txt");

        assertThrows(IOException.class, () -> reader.readInput());
    }

    @Test
    void Reader_returns_content_as_string_when_can_read_file() throws IOException {

        reader = new FileReader("src/test/resources/Les Misérables.txt");

        String input = reader.readInput();

        assertNotEquals(0, input.length());
    }

}

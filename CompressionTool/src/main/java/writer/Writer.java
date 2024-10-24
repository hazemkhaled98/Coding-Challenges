package writer;

import java.io.IOException;
import java.nio.file.Path;

public interface Writer {


    Path writeOutput(String text) throws IOException;
}

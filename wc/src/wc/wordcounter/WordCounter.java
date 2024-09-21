package wc.wordcounter;

import java.io.IOException;
import java.util.List;

public interface WordCounter {

    long getFileSize() throws IOException;

    long getLinesCount() throws IOException;

    long getWordsCount() throws IOException;

    long getCharactersCount() throws IOException;

    String getFileName();

    List<Object> getAll() throws IOException;

}

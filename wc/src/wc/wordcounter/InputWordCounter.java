package wc.wordcounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputWordCounter implements WordCounter {
    private final BufferedReader reader;
    private long fileSize = 0;
    private long linesCount = 0;
    private long wordCount = 0;
    private long charactersCount = 0;
    private final String fileName = "Stdin";

    InputWordCounter() throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        processInput();
    }

    private void processInput() throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            linesCount++;
            fileSize += line.length();
            charactersCount += line.length();
            wordCount += line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
        }
    }

    @Override
    public long getFileSize() throws IOException {
        return fileSize;
    }

    @Override
    public long getLinesCount() throws IOException {
        return linesCount;
    }

    @Override
    public long getWordsCount() throws IOException {
        return wordCount;
    }

    @Override
    public long getCharactersCount() throws IOException {
        return charactersCount;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public List<Object> getAll() throws IOException {
        List<Object> results = new ArrayList<>();
        results.add(linesCount);
        results.add(wordCount);
        results.add(charactersCount);
        results.add(fileSize);
        results.add(fileName);
        return results;
    }
}
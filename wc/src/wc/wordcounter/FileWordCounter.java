package wc.wordcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class FileWordCounter implements WordCounter{

    private final Path path;

    FileWordCounter(String fileUri) {
        path = Paths.get(fileUri);
    }

    @Override
    public long getFileSize() throws IOException {
        return Files.size(path);
    }

    @Override
    public long getLinesCount() throws IOException {
        return Files.readAllLines(path).size();
    }

    @Override
    public long getWordsCount() throws IOException {
        List<String> lines = Files.readAllLines(path);
        long wordsCount = 0;
        for (String line : lines) {
            line = line.trim();
            if(!line.isEmpty()) {
                wordsCount += line.split("\\s+").length;
            }
        }
        return wordsCount;
    }


    @Override
    public long getCharactersCount() throws IOException {
        try(Stream<String> lines = Files.lines(path)){
            return lines.mapToLong(String::length)
                    .sum();
        }
    }

    @Override
    public String getFileName() {
        return path.getFileName().toString();
    }

    @Override
    public List<Object> getAll() throws IOException {
        return List.of(getLinesCount(), getWordsCount(), getCharactersCount(), getFileSize(), getFileName());
    }
}

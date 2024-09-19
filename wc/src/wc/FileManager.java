package wc;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class FileManager {

    private Path path;

    FileManager(String fileUri) throws IOException {
        path = Paths.get(fileUri);
    }

    long getFileSize() throws IOException {
        return Files.size(path);
    }

    long getLinesCount() throws IOException {
        return Files.readAllLines(path).size();
    }

    long getWordsCount() throws IOException {
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


    long getCharactersCount() throws IOException {
        return Files.lines(path)
                .mapToLong(String::length)
                .sum();
    }

}

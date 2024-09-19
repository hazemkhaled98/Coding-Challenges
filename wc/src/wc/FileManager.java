package wc;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileManager {

    private static FileChannel fileChannel;

    static FileChannel openFile(String path) throws IOException {

        Path filePath = Paths.get(path);

        fileChannel = FileChannel.open(filePath);

        return fileChannel;
    }

    static void closeFile() throws IOException {
        fileChannel.close();
    }

}

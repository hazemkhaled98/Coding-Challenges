package writer;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPOutputStream;

@RequiredArgsConstructor
public class CompressedWriter implements Writer {

    private final String fileName;

    @Override
    public Path writeOutput(String text) throws IOException {
        byte[] compressedData = compress(text);
        return Files.write(Path.of(fileName), compressedData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private byte[] compress(String text) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(text.getBytes(Charset.defaultCharset()));
            gzipStream.finish();
            return byteStream.toByteArray();
        }
    }
}
package reader;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

@RequiredArgsConstructor
public class CompressedReader implements Reader {

    private final String fileName;


    @Override
    public String readInput() throws IOException {
        byte[] compressedContent = Files.readAllBytes(Path.of(fileName));
        return decompress(compressedContent);
    }

    private String decompress(byte[] compressedData) throws IOException {
        try (GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream.toString(Charset.defaultCharset());
        }
    }
}
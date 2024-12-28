package psn;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionHandler {
    public static byte[] compress(byte[] data) throws Exception {
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
        int count = deflater.deflate(buffer);
        outputStream.write(buffer, 0, count);
    }
    outputStream.close();
    return outputStream.toByteArray();
}

public static byte[] decompress(byte[] data) throws Exception {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    while (!inflater.finished()) {
        int count = inflater.inflate(buffer);
        outputStream.write(buffer, 0, count);
    }
    outputStream.close();
    return outputStream.toByteArray();
}

}
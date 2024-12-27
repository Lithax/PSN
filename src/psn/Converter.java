package psn;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

public class Converter {
    public static final byte[] PSN_ID = "δPSN".getBytes(Charset.forName("UTF-8"));
    public static final byte[] VERSION = "1.0".getBytes(Charset.forName("ASCII"));
    public static final byte[] HEADER_END = "!%$§!§%§&&$&/$".getBytes(Charset.forName("ASCII"));

    public static byte[] convertCompatibleToPSN(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ByteArrayOutputStream pixelStream = new ByteArrayOutputStream();

            for(int x = 0; x < height; x++) {
                for(int y = 0; y < width; y++) {
                    int rgb = image.getRGB(x, y);
                    byte[] data = integerToBytes(rgb); // 0 -> Alpha Transparency, 1 -> RED, 2 -> GREEN, 3 -> BLUE
                    pixelStream.write(data, 0, data.length);
                }
            }
            byte[] pixels = pixelStream.toByteArray();
            byte[] hash = Crypto.generateSHA256Hash(pixels);
            int header_length = PSN_ID.length + VERSION.length + integerToBytes(width).length + integerToBytes(height).length + hash.length;
            stream.write(integerToBytes(header_length), 0, integerToBytes(header_length).length);
            stream.write(PSN_ID, 0, PSN_ID.length);
            stream.write(VERSION, 0, VERSION.length);
            stream.write(integerToBytes(width), 0, integerToBytes(width).length);
            stream.write(integerToBytes(height), 0, integerToBytes(height).length);
            stream.write(hash, 0, hash.length);
            stream.write(pixels, 0, pixels.length);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] integerToBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) i;
        return bytes;
    }
}

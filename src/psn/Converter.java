package psn;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Converter {
    public static final byte[] PSN_ID = "δPSN".getBytes(Charset.forName("UTF-8"));
    public static final byte[] VERSION = "1.0".getBytes(Charset.forName("ASCII"));

    public static byte[] convertCompatibleToPSN(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ByteArrayOutputStream pixelStream = new ByteArrayOutputStream();

            for(int x = 0; x < height; x++) {
                for(int y = 0; y < width; y++) {
                    int rgb = image.getRGB(y, x);
                    byte[] data = integerToBytes(rgb); // 0 -> Alpha Transparency, 1 -> RED, 2 -> GREEN, 3 -> BLUE
                    pixelStream.write(data, 0, data.length);
                
                }
            }
            pixelStream.close();
            pixelStream.flush();
            byte[] pixels = pixelStream.toByteArray();
            byte[] hash = Crypto.generateSHA256Hash(pixels);
            pixels = CompressionHandler.compress(pixels);
            int header_length = PSN_ID.length + VERSION.length + integerToBytes(width).length + integerToBytes(height).length + hash.length;
            header_length += integerToBytes(header_length).length;
            stream.write(integerToBytes(header_length), 0, integerToBytes(header_length).length);
            stream.write(PSN_ID, 0, PSN_ID.length);
            stream.write(VERSION, 0, VERSION.length);
            stream.write(integerToBytes(width), 0, integerToBytes(width).length);
            stream.write(integerToBytes(height), 0, integerToBytes(height).length);
            stream.write(hash, 0, hash.length);
            stream.write(pixels, 0, pixels.length);
            System.out.println("--------CONVERSION:"+file.getName()+"--------\nUNIQUE_ID:\t"+new String(PSN_ID, "UTF-8")+"\nVERSION:\t"+new String(VERSION)+"\nSHA-256:\t"+Arrays.toString(hash)+"\nHEADER_LENGTH:\t"+header_length+"\nDATA-C LENGTH:\t"+pixels.length);
            stream.close();
            stream.flush();
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void write(BufferedImage img, String name, String type) {
        try {
            File file = new File(name);
            boolean success = ImageIO.write(img, type, file);
            if (success) {
                System.out.println("Image saved successfully at: " + name);
            } else {
                System.out.println("Failed to save the image.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage byteArrayToBufferedImage(byte[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        // Ensure that the pixel array length matches the expected size (width * height * 4 bytes)
        if (pixels.length != width * height * 4) {
            throw new IllegalArgumentException("Pixel data size does not match expected dimensions.");
        }
    
        // Index for iterating through the byte array
        int index = 0;
    
        // Iterate over all the pixels and set them in the BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Extract ARGB values (4 bytes per pixel: alpha, red, green, blue)
                int alpha = pixels[index] & 0xFF;
                int red = pixels[index + 1] & 0xFF;
                int green = pixels[index + 2] & 0xFF;
                int blue = pixels[index + 3] & 0xFF;
    
                // Combine ARGB values into a single int
                int pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
    
                // Set the pixel in the BufferedImage
                image.setRGB(x, y, pixel); // No swapping of x and y
    
                // Move to the next pixel (4 bytes per pixel)
                index += 4;
            }
        }
    
        return image;
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

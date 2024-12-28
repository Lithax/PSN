import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Reader extends ByteString {
    public static void unwrapPSNFile(byte[] psn, boolean gui, String name, String type) {
        try {
            int header_length = bytesToInteger(byteSubstring(psn, 0, 4));
            byte[] id = byteSubstring(psn, 4, 9);
            byte[] version = byteSubstring(psn, 9,12);
            byte[] width = byteSubstring(psn, 12, 16);
            byte[] height = byteSubstring(psn,16, 20);
            byte[] hash = byteSubstring(psn, 20, header_length);
            byte[] pixels = byteSubstring(psn, header_length, psn.length);

            if(equals(id, Converter.PSN_ID)) {
                byte[] decompressed = CompressionHandler.decompress(pixels);
                if(equals(Crypto.generateSHA256Hash(decompressed), hash)) {
                    if(gui) {
                        display(bytesToInteger(width), bytesToInteger(height), decompressed);
                    } else {
                        Converter.write(Converter.byteArrayToBufferedImage(decompressed, bytesToInteger(width), bytesToInteger(height)), name, type);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Hash doesnt Match file, it may have been tampered with", "Hash Mismatch", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID doesnt Match", "ID Mismatch", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("--------READING--------\nUNIQUE_ID:\t"+new String(id, "UTF-8")+"\nVERSION:\t"+new String(version)+"\nSHA-256:\t"+Arrays.toString(hash)+"\nHEADER_LENGTH:\t"+header_length+"\nDATA-C LENGTH:\t"+pixels.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void display(int width, int height, byte[] decompressed) {
        JFrame frame = new JFrame("PSN IMAGE VIEWER 1.0");
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setAutoRequestFocus(true);
        BufferedImage image = Converter.byteArrayToBufferedImage(decompressed, width, height);
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        frame.add(imagePanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                imagePanel.repaint();
            }
        });
    }
}
package psn;

import java.security.MessageDigest;

public class Crypto {
    public static byte[] generateSHA256Hash(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        return hash;
    }
}

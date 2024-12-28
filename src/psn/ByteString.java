package psn;

import java.util.Arrays;

/**
 * Utility class for performing operations on byte arrays, such as substring extraction,
 * searching for patterns, and finding indices.
 */
public class ByteString {

    /**
     * Extracts a subarray (substring) from the given byte array.
     *
     * @param data  The original byte array.
     * @param start The starting index (inclusive).
     * @param end   The ending index (exclusive).
     * @return A new byte array containing the subarray from the specified range.
     * @throws IllegalArgumentException if start < 0, end > data.length, or start > end.
     */
    public static byte[] byteSubstring(byte[] data, int start, int end) {
        if (start < 0 || end > data.length || start > end) {
            throw new IllegalArgumentException("Invalid start or end indices: start=" + start + ", end=" + end + ", data.length=" + data.length);
        }
        byte[] result = new byte[end - start];
        System.arraycopy(data, start, result, 0, end - start);
        return result;
    }

    /**
     * Finds the last index of the specified byte value in the byte array.
     *
     * @param data The byte array to search.
     * @param ch   The byte value to find.
     * @return The last index of the specified byte, or -1 if not found.
     */
    public static int lastIndexOf(byte[] data, int ch) {
        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of a byte pattern in the byte array, starting from a given index.
     *
     * @param data    The byte array to search.
     * @param pattern The byte pattern to search for.
     * @param start   The starting index (inclusive) for the search.
     * @return The index of the first occurrence of the pattern, or -1 if not found.
     * @throws IllegalArgumentException if the starting index is out of bounds or the pattern is null/empty.
     */
    public static int indexOf(byte[] data, byte[] pattern, int start) {
        if (start < 0 || start >= data.length) {
            throw new IllegalArgumentException("Invalid start index: start=" + start + ", data.length=" + data.length);
        }
        if (pattern == null || pattern.length == 0) {
            throw new IllegalArgumentException("Pattern cannot be null or empty.");
        }

        sigma:
        for (int i = start; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) continue sigma;
            }
            return i;
        }
        return -1;
    }

    public static int bytesToInteger(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Byte array must be non-null and of length 4.");
        }
        return ((bytes[0] & 0xFF) << 24) | 
               ((bytes[1] & 0xFF) << 16) | 
               ((bytes[2] & 0xFF) << 8)  | 
               (bytes[3] & 0xFF);
    }

    public static boolean equals(byte[] array1, byte[] array2) {
        return Arrays.equals(array1, array2);
    }
}

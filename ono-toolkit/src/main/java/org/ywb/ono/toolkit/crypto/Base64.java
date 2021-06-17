package org.ywb.ono.toolkit.crypto;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:04
 * @since 1.0.0
 */
public class Base64 {

    public static String encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decode(String text) {
        return java.util.Base64.getDecoder().decode(text);
    }
}

package org.ywb.ono.toolkit.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

/**
 * @author yuwenbo1
 * @date 2021/3/19 9:58
 * @since 1.0.0
 */
public class DES {

    public static String encrypt(String text, String key) {
        return encrypt(text, key, StandardCharsets.UTF_8);
    }

    public static String encrypt(String text, String key, Charset charset) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, getKey(key));
            byte[] textBytes = text.getBytes(charset);
            byte[] bytes = cipher.doFinal(textBytes);
            byte[] encode = Base64.getEncoder().encode(bytes);
            return new String(encode, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String text, String key) {
        return decrypt(text, key, StandardCharsets.UTF_8);
    }

    public static String decrypt(String text, String key, Charset charset) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, getKey(key));
            byte[] textBytes = Base64.getDecoder().decode(text);
            byte[] bytes = cipher.doFinal(textBytes);
            return new String(bytes, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static SecretKey getKey(String key) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            return keyFactory.generateSecret(new DESKeySpec(Base64.getDecoder().decode(key)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成一个随机字符串密钥
     */
    public static String generateRandomKey() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .toLowerCase(Locale.ROOT)
                .substring(0, 16);
    }

}

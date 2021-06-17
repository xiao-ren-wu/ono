package org.ywb.ono.toolkit.crypto;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yuwenbo1
 * @version v1.0.0
 * <p>
 * HMACSHA256
 * </p>
 * @since 2019年12月9日15:09:47
 */
public class HMACSHA256 {
    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     *
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String sha256HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] bytes = sha256HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hash;
    }
}

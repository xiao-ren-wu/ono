package org.ywb.ono.toolkit.crypto;

import java.security.MessageDigest;

/**
 * @author yuwenbo1
 * @date 2021/3/19 9:55
 * @since 1.0.0
 */
public class MD5 {

    /**
     * md5摘要
     *
     * @param text text
     * @return MD5
     */
    public static String md5(String text) {
        try {
            byte[] bytes = text.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                if ((aByte & 255) < 16) {
                    sb.append("0");
                }

                sb.append(Long.toString(aByte & 255, 16));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * md5摘要，加盐
     *
     * @param text 摘要文本
     * @param salt 盐
     * @return MD5 str
     */
    public static String md5(String text, String salt) {
        try {
            byte[] bytes = (text + salt).getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                if ((aByte & 255) < 16) {
                    sb.append("0");
                }
                sb.append(Long.toString((long) (aByte & 255), 16));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证MD5
     *
     * @param text 摘要文本
     * @param salt 盐
     * @param md5  md5串
     * @return bool
     * @throws Exception e
     */
    public static boolean verify(String text, String salt, String md5) throws Exception {
        String md5Text = md5(text, salt);
        return md5Text.equalsIgnoreCase(md5);
    }
}

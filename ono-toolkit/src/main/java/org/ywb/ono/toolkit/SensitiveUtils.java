package org.ywb.ono.toolkit;

/**
 * @author yuwenbo
 * @version v1.0.0
 * <p>
 * 脱敏工具
 * </p>
 * @date 2019/10/22 12:14
 */
public class SensitiveUtils {
    /**
     * 11位电话号码除了前三位和后三位
     * eg：13596016441===>135****6441
     *
     * @param mobileNo 手机号
     * @return 脱敏后的手机号
     */
    public static String sensitiveMobileNo(String mobileNo) {
        return sensitiveMiddle(mobileNo, 3, mobileNo.length() - 4);
    }

    /**
     * 脱敏车牌号码
     * [脱敏规则]
     * 1、普通车牌：
     * 京A12345 ===> 京A***45
     * 2、新能源车牌：
     * 京A123456 ===> 京A***456
     *
     * @param licenseNo 车牌号码
     * @return string
     */
    public static String sensitiveLicenseNo(String licenseNo) {
        if (isNullOrEmpty(licenseNo) || licenseNo.length() < 7) {
            return licenseNo;
        }
        return sensitiveMiddle(licenseNo, 2, 5);
    }

    /**
     * 脱敏身份证号
     * [脱敏规则]
     * 保留前四位后三位
     * eg:
     * 220122198605017231 ===> 2201***********231
     *
     * @param idCard 身份证号
     * @return idCard after sensitive
     */
    public static String sensitiveIdCard(String idCard) {
        return sensitiveMiddle(idCard, 4, idCard.length() - 3);
    }

    /**
     * 将下标为begin->end之间的所有元素替换成‘*’
     * 左闭右开
     *
     * @param source source
     * @param begin  begin index
     * @param end    end index
     * @return target
     */
    public static String sensitiveMiddle(String source, int begin, int end) {
        if (isNullOrEmpty(source)) {
            return source;
        }
        if (begin <= 0 || end >= source.length()) {
            throw new IllegalArgumentException("begin or end point Out of border");
        }
        char[] chars = source.toCharArray();
        for (int i = begin; i < end; i++) {
            chars[i] = '*';
        }
        return String.valueOf(chars);
    }

    /**
     * 将两边的元素替换成*
     *
     * @param source source
     * @param left   left index
     * @param right  right index
     * @return target
     */
    public static String sensitiveSides(String source, int left, int right) {
        if (isNullOrEmpty(source)) {
            return source;
        }
        if (left <= 0 || right >= source.length() || left > right) {
            throw new IllegalArgumentException("left or right point Out of border");
        }
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i < left || i > right) {
                chars[i] = '*';
            }
        }
        return String.valueOf(chars);
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}

package org.ywb.ono.toolkit.consts;

/**
 * @author yuwenbo1
 * @since 2020/7/2 2:49 下午
 */
public interface ConstantFactory {
    /**
     * 删除标记
     */
    interface DelFlag {
        /**
         * 删除
         */
        Integer DEL = 1;
        /**
         * 未删除
         */
        Integer UN_DEL = 0;
    }

    /**
     * 开关
     */
    interface Switch {
        /**
         * 启用
         */
        Integer ENABLE = 1;
        /**
         * 禁用
         */
        Integer DISABLE = 0;
    }

    /**
     * 排序顺序
     *
     * @author yuwenbo10
     * @date 2021年3月18日18:43:04
     * @since 1.0.0
     */
    interface Sorted {
        String ASC = "asc";
        String DESC = "desc";
    }
}

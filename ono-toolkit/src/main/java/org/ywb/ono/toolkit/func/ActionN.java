package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:36
 * @since 1.0.0
 */
@FunctionalInterface
public interface ActionN extends Action {
    void call(Object... args);
}

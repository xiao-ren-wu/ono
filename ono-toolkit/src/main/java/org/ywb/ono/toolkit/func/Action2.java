package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action2<T1, T2> extends Action {
    void call(T1 t1, T2 t2);
}

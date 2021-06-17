package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action3<T1, T2, T3> extends Action {
    void call(T1 t1, T2 t2, T3 t3);
}

package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:34
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action6<T1, T2, T3, T4, T5, T6> extends Action {
    void call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
}

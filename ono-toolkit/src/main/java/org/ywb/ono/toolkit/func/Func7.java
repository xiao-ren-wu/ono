package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:31
 * @since 1.0.0
 */
@FunctionalInterface
public interface Func7<T1, T2, T3, T4, T5, T6, T7, R> extends Func {
    R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
}

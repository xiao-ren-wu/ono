package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:30
 * @since 1.0.0
 */
@FunctionalInterface
public interface Func5<T1, T2, T3, T4, T5, R> extends Func {
    R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
}


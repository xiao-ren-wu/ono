package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:29
 * @since 1.0.0
 */
@FunctionalInterface
public interface Func3<T1, T2, T3, R> extends Func {
    R call(T1 t1, T2 t2, T3 t3);
}

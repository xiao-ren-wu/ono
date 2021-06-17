package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:29
 * @since 1.0.0
 */
@FunctionalInterface
public interface Func1<T, R> extends Func {
    R call(T t);
}


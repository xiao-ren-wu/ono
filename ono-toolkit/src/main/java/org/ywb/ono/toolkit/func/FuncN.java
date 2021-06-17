package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:32
 * @since 1.0.0
 */
@FunctionalInterface
public interface FuncN<R> extends Func {
    R call(Object... args);
}

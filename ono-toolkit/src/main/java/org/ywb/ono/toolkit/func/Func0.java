package org.ywb.ono.toolkit.func;

import java.util.concurrent.Callable;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:28
 * @since 1.0.0
 */
@FunctionalInterface
public interface Func0<R> extends Func, Callable<R> {
    @Override
    R call();
}
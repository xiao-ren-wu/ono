package org.ywb.ono.toolkit.func;

import java.util.function.Consumer;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action1<T> extends Action, Consumer<T> {
    @Override
    void accept(T t);
}

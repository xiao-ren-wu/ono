package org.ywb.ono.toolkit.func;

/**
 * @author yuwenbo1
 * @date 2021/3/19 10:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action0 extends Action,Runnable {
    @Override
    void run();
}
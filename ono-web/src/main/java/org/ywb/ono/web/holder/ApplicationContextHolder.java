package org.ywb.ono.web.holder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午11:31
 * @since 1.0.0
 */
@Slf4j
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("SpringContextHolder initialize success");
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    /**
     * 通过实例类型获取实例
     *
     * @param requiredType 实例类型
     * @param <T>          T
     * @return T
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 通过实例，名称获取实例
     *
     * @param beanName 实例名称
     * @param <T>      T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> Map<String, T> getBeansWithType(Class<T> tClass) {
        assertApplicationContext();
        return applicationContext.getBeansOfType(tClass);
    }

    /**
     * 通过类上的注解获取类
     *
     * @param annotation anno
     * @return map
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        assertApplicationContext();
        return applicationContext.getBeansWithAnnotation(annotation);
    }


    private static void assertApplicationContext() {
        if (ApplicationContextHolder.applicationContext == null) {
            throw new RuntimeException("application Context is null,please check whether inject ApplicationContextHolder ?");
        }
    }

    /**
     * 发布事件
     *
     * @param event event
     */
    public static void publishEvent(ApplicationEvent event) {
        assertApplicationContext();
        applicationContext.publishEvent(event);
    }

}

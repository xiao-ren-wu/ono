package org.ywb.ono.web.advice;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

/**
 * @author yuwenbo1
 * @since 2021/3/23 16:02
 */
public class ExceptionHandlerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> expHandlerMap =
                conditionContext.getBeanFactory().getBeansWithAnnotation(ControllerAdvice.class);
        return expHandlerMap.isEmpty();
    }
}

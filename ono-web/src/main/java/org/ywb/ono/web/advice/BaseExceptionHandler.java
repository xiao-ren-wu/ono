package org.ywb.ono.web.advice;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ywb.ono.web.exceptions.BusinessException;
import org.ywb.ono.web.response.IRespCode;
import org.ywb.ono.web.response.R;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuwenbo1
 * <p>
 * 异常统一处理
 * </p>
 * @since 2020/1/2 14:17
 */
@Slf4j
@RestControllerAdvice
@Conditional(value = ExceptionHandlerCondition.class)
public class BaseExceptionHandler {

    public BaseExceptionHandler() {
        log.info("\ninit BaseExceptionHandler...");
    }

    /**
     * spring validation 自动校验的参数异常
     *
     * @param e BindException
     * @return ResultVO<Void>
     */
    @ResponseStatus(org.springframework.http.HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(BindException.class)
    public R<Void> handler(BindException e) {
        String defaultMsg = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(":"));
        log.warn(defaultMsg);
        return R.of(IRespCode.PARAMETERS_ANOMALIES.getCode(), e.getMessage());
    }

    /**
     * 多个参数校验异常
     *
     * @param ex ConstraintViolationException
     * @return ResultVO<Void>
     */
    @ResponseStatus(code = org.springframework.http.HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<Void> handleBindGetException(ConstraintViolationException ex) {
        String defaultMsg = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(":"));
        log.warn(defaultMsg);
        return R.of(IRespCode.PARAMETERS_ANOMALIES.getCode(), defaultMsg);
    }

    /**
     * 参数异常
     *
     * @param e IllegalArgumentException
     * @return PARAM_ERROR
     */
    @ResponseStatus(code = org.springframework.http.HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R<Void> handler(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return R.of(IRespCode.PARAMETERS_ANOMALIES.getCode(), e.getMessage());
    }

    /**
     * 单个参数校验异常
     * 校验模式为 fail fast
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseResult
     */
    @ResponseStatus(org.springframework.http.HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgsGetException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String reduce = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(":"));
        log.warn(ex.getMessage());
        return R.of(IRespCode.PARAMETERS_ANOMALIES.getCode(), reduce);
    }

    /**
     * 业务异常，一般是手动抛出，不需要打印异常堆栈
     *
     * @param e BusinessException
     * @return code, message, data
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handler(BusinessException e) {
        log.error(e.getMessage());
        return R.of(e.getCode(), e.getMsg(), e.getData());
    }

    /**
     * 该异常发生一般是前端访问姿势不对导致
     * eg:
     * MediaTypeNotAcceptable
     * RequestMethodNotSupported
     * MediaTypeNotSupported
     *
     * @param e ServletException
     * @return 返回异常状态码，并携带具体的错误信息
     */
    @ResponseStatus(org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(ServletException.class)
    public R<Void> handler(ServletException e) {
        log.warn(e.getMessage());
        return R.of(IRespCode.PARAMETERS_ANOMALIES.getCode(), e.getMessage());
    }

    /**
     * 未知异常，屏蔽具体错误信息给前端，
     * 日志打印异常堆栈
     *
     * @param e Throwable
     */
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class, Throwable.class})
    public R<Void> handler(Throwable e) {
        log.error(Throwables.getStackTraceAsString(e));
        return R.of(IRespCode.SYSTEM_ERROR.getCode(), "系统错误");
    }
}
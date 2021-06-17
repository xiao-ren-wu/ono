package org.ywb.ono.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午9:50
 * @since 1.0.0
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    @ApiModelProperty("响应状态码")
    private Integer code;

    @ApiModelProperty("响应描述")
    private String msg;

    @ApiModelProperty("响应数据")
    private T data;

    private static final R<Void> SUCCESS =
            new R<>(IRespCode.OK.getCode(), IRespCode.OK.getMessage());

    private R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 功能描述: 请求成功
     *
     * @param data resp data
     * @return t
     * @date 2020/12/16 13:14
     */
    public static <T> R<T> ok(T data) {
        return new R<T>(IRespCode.OK.getCode(), IRespCode.OK.getMessage(), data);
    }

    /**
     * 构造空的成功相应
     *
     * @return success
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static R<Void> ok() {
        return SUCCESS;
    }

    /**
     * 函数式填充响应
     *
     * @param supplier supplier
     * @param <T>      T
     * @return response
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static <T> R<T> ok(Supplier<T> supplier) {
        return new R<>(
                IRespCode.OK.getCode(),
                IRespCode.OK.getMessage(),
                supplier.get());
    }

    /**
     * 函数式填充响应
     *
     * @param function func
     * @param t        t
     * @param <T>      T
     * @param <R>      R
     * @return response
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static <T, R> org.ywb.ono.web.response.R<R> ok(Function<T, R> function, T t) {
        return new org.ywb.ono.web.response.R<>(
                IRespCode.OK.getCode(),
                IRespCode.OK.getMessage(),
                function.apply(t));
    }

    /**
     * 构造响应
     *
     * @param code    status
     * @param message errMsg
     * @return RespResult
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static R<Void> of(Integer code, String message) {
        return new R<>(code, message);
    }

    /**
     * 构造响应
     *
     * @param code    status
     * @param message errMsg
     * @return RespResult
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static <T> R<T> of(Integer code, String message, T data) {
        return new R<>(code, message, data);
    }

    /**
     * 构造响应
     *
     * @param responseEntity 响应码
     * @return RespResult
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static R<Void> of(RespCodeEntity responseEntity) {
        return of(responseEntity.getCode(), responseEntity.getMessage());
    }

    /**
     * 判断响应是否成功
     *
     * @param r resp
     * @return bool
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static boolean isOk(R<?> r) {
        return Optional.ofNullable(r)
                .map(resp -> IRespCode.OK.getCode().equals(resp.code))
                .orElse(false);
    }

    /**
     * 如果状态码ok尝试获取响应
     *
     * @param r respResult
     * @param exception  状态码不正确时抛出的异常
     * @return Optional of data
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static <T> Optional<T> getDataIfOk(R<T> r, Supplier<? extends RuntimeException> exception) {
        if (!isOk(r)) {
            throw exception.get();
        }
        return Optional.ofNullable(r.data);
    }

    /**
     * 如果状态码ok尝试获取响应
     *
     * @param r respResult
     * @param exception  状态码不正确时抛出的异常
     * @return Optional of data
     * @author yuwenbo1
     * @date 2021年3月23日09:26:57
     * @since 1.0.0
     */
    public static <T> Optional<T> getDataIfOk(R<T> r, Function<R<T>, ? extends RuntimeException> exception) {
        if (!isOk(r)) {
            throw exception.apply(r);
        }
        return Optional.ofNullable(r.data);
    }
}

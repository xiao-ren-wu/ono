package org.ywb.ono.web.exceptions;

import org.ywb.ono.web.response.RespCodeEntity;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午9:53
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

    private final Integer code;

    private final String msg;

    private Object data;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(RespCodeEntity respCodeEntity) {
        this.code = respCodeEntity.getCode();
        this.msg = respCodeEntity.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public BusinessException(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static BusinessException of(Integer code, String msg) {
        return new BusinessException(code, msg);
    }

    public static BusinessException of(RespCodeEntity respCodeEntity) {
        return new BusinessException(respCodeEntity.getCode(), respCodeEntity.getMessage());
    }

}

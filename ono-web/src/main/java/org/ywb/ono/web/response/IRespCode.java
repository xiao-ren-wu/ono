package org.ywb.ono.web.response;

/**
 * 统一状态码
 *
 * @author ywb
 * @version v1.0.0
 * @date 2021/2/9 10:13
 */
public interface IRespCode {
    RespCodeEntity OK = new RespCodeEntity(200, "ok");
    RespCodeEntity PARAMETERS_ANOMALIES = new RespCodeEntity(40001, "param error");
    RespCodeEntity SYSTEM_ERROR = new RespCodeEntity(99999, "system error");
}

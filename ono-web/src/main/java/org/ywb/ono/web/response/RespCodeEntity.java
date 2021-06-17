package org.ywb.ono.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午9:49
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
public class RespCodeEntity {
    private Integer code;
    private String message;
}

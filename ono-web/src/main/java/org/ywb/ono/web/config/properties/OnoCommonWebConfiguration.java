package org.ywb.ono.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/6/18 2:53 下午
 */
@Data
@Component
@ConfigurationProperties(prefix = "ono.web")
public class OnoCommonWebConfiguration {
    /**
     * 跨域配置
     */
    private CorsProperties cors;
    /**
     * 监控日志打印配置
     */
    private LogPrintProperties requestMonitor;
    /**
     * swagger配置类
     */
    private SwaggerProperties swagger;
}

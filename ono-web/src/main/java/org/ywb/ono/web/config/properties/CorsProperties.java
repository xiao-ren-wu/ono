package org.ywb.ono.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/7/1 2:57 下午
 */
@Data
@Component
@ConfigurationProperties(prefix = "ono.web.cors")
public class CorsProperties {
    /**
     * 是否开启跨域解决
     */
    private Boolean enable;
    /**
     * allowedOrigins 默认是*
     */
    private List<String> allowedOrigins;
    /**
     * allowedMethods 默认是*
     * 例如：{GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE}
     */
    private List<String> allowedMethods;
    /**
     * allowedHeaders 默认是*
     */
    private List<String> allowedHeaders;
    /**
     * exposedHeaders 默认是*
     */
    private List<String> exposedHeaders;
    /**
     * allowCredentials 默认是 true
     */
    private Boolean allowCredentials = true;
    /**
     * maxAge
     */
    private Long maxAge;

}

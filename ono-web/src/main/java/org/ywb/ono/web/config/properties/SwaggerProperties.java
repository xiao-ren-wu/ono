package org.ywb.ono.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/7/10 10:52 上午
 */
@Data
@Component
@ConfigurationProperties(prefix = "ono.web.swagger")
public class SwaggerProperties {
    /**
     * 是否swagger3启用，默认不启用
     */
    private Boolean enable= false;
    /**
     * 扫描包路径，可以不指定，系统会通过自动扫描{@link io.swagger.annotations.ApiOperation}
     */
    private String basePackage;
    /**
     * 标题
     */
    private String title;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 服务地址
     */
    private String serviceUrl;
    /**
     * 版本，默认V1.0.0
     */
    private String version = "V1.0.0";
    /**
     * license
     */
    private String license;
    /**
     * licenseUrl
     */
    private String licenseUrl;
}

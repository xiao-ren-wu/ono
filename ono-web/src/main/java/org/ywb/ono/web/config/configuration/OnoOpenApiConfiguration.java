package org.ywb.ono.web.config.configuration;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ywb.ono.web.config.properties.SwaggerProperties;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

import java.util.function.Predicate;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

/**
 * @author yuwenbo
 */
@Slf4j
@Configuration
@EnableOpenApi
public class OnoOpenApiConfiguration {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(apis())
                .paths(PathSelectors.any())
                .build()
                .enable(isEnable());
    }

    private Boolean isEnable() {
        Boolean enable = swaggerProperties.getEnable();
        if (enable) {
            log.info("\nEnable Swagger3...");
        }
        return enable;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getServiceUrl())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .build();
    }

    private Predicate<RequestHandler> apis() {
        String basePackage = swaggerProperties.getBasePackage();
        return Strings.isNullOrEmpty(basePackage) ? withMethodAnnotation(ApiOperation.class) : basePackage(basePackage);
    }

}
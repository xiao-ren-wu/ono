package org.ywb.ono.web.config.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.ywb.ono.web.config.properties.CorsProperties;
import org.ywb.ono.web.toolkit.RequestMonitorFilter;

import java.util.List;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/7/1 3:53 下午
 */
@Slf4j
@Configuration
public class OnoFrameworkBeanConfiguration {

    @Bean
    @ConditionalOnProperty(
            prefix = "ono.web.request-monitor",
            name = "enable",
            havingValue = "true"
    )
    public RequestMonitorFilter filter() {
        return new RequestMonitorFilter();
    }


    @Bean
    @ConditionalOnProperty(
            prefix = "ono.web.cors",
            name = {"enable"},
            havingValue = "true"
    )
    @ConditionalOnMissingBean(value = CorsFilter.class)
    public CorsFilter corsFilter(CorsProperties corsProperties) {
        log.info("\ncors enabled.");
        CorsConfiguration corsConfiguration = generatorCorsConfiguration(corsProperties);
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    private CorsConfiguration generatorCorsConfiguration(CorsProperties corsProperties) {

        final CorsConfiguration corsConfiguration = new CorsConfiguration();

        List<String> allowedOrigins = corsProperties.getAllowedOrigins();
        if (Objects.isNull(allowedOrigins)) {
            corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        } else {
            corsConfiguration.setAllowedOrigins(allowedOrigins);
        }

        List<String> allowedHeaders = corsProperties.getAllowedHeaders();
        if (Objects.isNull(allowedHeaders)) {
            corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        } else {
            corsConfiguration.setAllowedHeaders(allowedHeaders);
        }

        List<String> allowedMethods = corsProperties.getAllowedMethods();
        if (Objects.isNull(allowedMethods)) {
            corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        } else {
            corsConfiguration.setAllowedMethods(allowedMethods);
        }

        Boolean allowCredentials = corsProperties.getAllowCredentials();
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());
        return corsConfiguration;
    }
}
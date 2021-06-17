package org.ywb.ono.web.toolkit;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.ywb.ono.toolkit.GsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/6/13 3:43 下午
 */
@Slf4j
public class RestBuilder {

    private static final RestTemplate REST_TEMPLATE;

    private HttpHeaders httpHeaders = new HttpHeaders();

    private String postBodyStr;

    private Map<String, String> restfulPathParamMap;

    private Map<String, String> getParamMap;

    private Map<String, Object> bodyParam;

    private MultiValueMap<String, Object> formData;

    private HttpEntity<?> httpEntity;

    private String requestPath;

    private Integer readTimeout;

    private Integer connectTimeout;

    private Boolean setTimeout = false;

    private Boolean monitor = false;

    private static final Cache<String, RestTemplate> CACHE;

    private static final Object LOCK = new Object();

    static {
        REST_TEMPLATE = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        REST_TEMPLATE.getMessageConverters().add(converter);
        CACHE = CacheBuilder.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();
    }

    private RestBuilder() {
    }

    public static RestBuilder builder() {
        return new RestBuilder();
    }

    /**
     * 添加header
     *
     * @param headerKey key
     * @param value     value
     * @return {@link RestBuilder}
     */
    public RestBuilder header(String headerKey, String value) {
        Assert.hasText(headerKey, "headerKey must not be null");
        httpHeaders.add(headerKey, value);
        return this;
    }

    /**
     * 设置Http ContentType
     *
     * @param mediaType {@link MediaType}
     * @return {@link RestBuilder}
     */
    public RestBuilder contentType(MediaType mediaType) {
        if (Objects.isNull(httpHeaders)) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.setContentType(mediaType);
        return this;
    }

    /**
     * 设置GET请求参数，URL拼接形式
     *
     * @param pathParam key
     * @param value     value
     * @return {@link RestBuilder}
     */
    public RestBuilder pathParam(String pathParam, String value) {
        Assert.hasText(pathParam, "supplier must not be null");
        if (Objects.isNull(getParamMap)) {
            getParamMap = new HashMap<>();
        }
        getParamMap.put(pathParam, value);
        return this;
    }

    /**
     * 设置post表单
     *
     * @param param key
     * @param value value
     * @return {@link RestBuilder}
     */
    public RestBuilder formData(String param, String value) {
        Assert.hasText(param, "supplier not be null");
        if (Objects.isNull(this.formData)) {
            formData = new LinkedMultiValueMap<>();
        }
        this.formData.add(param, value);
        return this;
    }

    /**
     * 设置body参数，最终会被转换成json
     *
     * @param key   key
     * @param value value
     * @return {@link RestBuilder}
     */
    public <T> RestBuilder bodyParam(String key, Object value) {
        Assert.hasText(key, "function must not be null");
        if (Objects.isNull(bodyParam)) {
            this.bodyParam = new HashMap<>();
        }
        bodyParam.put(key, value);
        return this;
    }

    /**
     * 设置请求body，最终会转换成json
     *
     * @param supplier {@link Object}
     * @return {@link RestBuilder}
     */
    public RestBuilder bodyObj(Supplier<Object> supplier) {
        Assert.notNull(supplier, "supplier must not be null");
        Object obj = supplier.get();
        this.postBodyStr = obj instanceof String ? (String) obj : GsonUtils.object2Json(obj);
        return this;
    }

    /**
     * 设置路径参数
     *
     * @param key   key
     * @param value value
     * @return {@link RestBuilder}
     */
    public RestBuilder restfulPathParam(String key, String value) {
        Assert.hasText(key, "key must not be null");
        if (Objects.isNull(restfulPathParamMap)) {
            this.restfulPathParamMap = new HashMap<>();
        }
        restfulPathParamMap.put(key, value);
        return this;
    }

    /**
     * 发送请求并返回{@link ResponseEntity}
     *
     * @param requestPath  请求路径/url
     * @param responseType 响应类型
     * @param <R>          ResponseType
     * @return response
     */
    public <R> ResponseEntity<R> sendForEntity(HttpMethod requestMethod, String requestPath, Type responseType) {
        build(requestPath);
        return requestForEntity(this.requestPath, this.httpEntity, responseType, requestMethod);
    }

    /**
     * 发送请求并直接返回响应体
     *
     * @param requestMethod {@link HttpMethod}
     * @param requestPath   请求路径
     * @param responseType  响应类型
     * @param <R>           R
     * @return response Body
     */
    public <R> R sendForObj(HttpMethod requestMethod, String requestPath, Type responseType) {
        ResponseEntity<R> responseEntity = sendForEntity(requestMethod, requestPath, responseType);
        return responseEntity.getBody();
    }

    /**
     * 发送请求并返回{@link ResponseEntity}
     *
     * @param requestPath  请求路径/url
     * @param responseType 响应类型
     * @param <R>          ResponseType
     * @return response
     */
    public <R> ResponseEntity<R> sendForEntity(HttpMethod requestMethod, String requestPath, Class<R> responseType) {
        build(requestPath);
        return requestForEntity(this.requestPath, this.httpEntity, responseType, requestMethod);
    }

    /**
     * 发送请求并直接返回响应体
     *
     * @param requestMethod {@link HttpMethod}
     * @param requestPath   请求路径
     * @param responseType  响应类型
     * @param <R>           R
     * @return response Body
     */
    public <R> R sendForObj(HttpMethod requestMethod, String requestPath, Class<R> responseType) {
        ResponseEntity<R> responseEntity = sendForEntity(requestMethod, requestPath, responseType);
        return responseEntity.getBody();
    }

    /**
     * 发送GET请求并反返回响应流
     *
     * @param requestPath 请求路径/URL
     * @return InputStream
     */
    public InputStream sendForInputStream(HttpMethod requestMethod, String requestPath) throws IOException {
        ResponseEntity<Resource> responseEntity = sendForEntity(requestMethod, requestPath, Resource.class);
        return Objects.requireNonNull(responseEntity.getBody()).getInputStream();
    }

    /**
     * 发送请求检测
     *
     * @return this
     */
    public RestBuilder monitor() {
        this.monitor = true;
        return this;
    }

    /**
     * 设置readTimeOut
     *
     * @param connectTimeout connectTimeout
     * @param readTimeout    readTimeout
     * @return {@link RestBuilder}
     */
    public RestBuilder timeout(int readTimeout, int connectTimeout) {
        this.setTimeout = true;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 构造restful路径
     *
     * @param path path
     * @return restful path
     */
    private String generatePath(String path) {
        if (Objects.nonNull(restfulPathParamMap) && !restfulPathParamMap.isEmpty()) {
            // 替换restful值
            Set<Map.Entry<String, String>> entrySet = restfulPathParamMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                path = path.replace(String.format("${%s}", entry.getKey()), entry.getValue());
            }
        }
        if (Objects.nonNull(getParamMap) && !getParamMap.isEmpty()) {
            StringBuilder pathBuilder = new StringBuilder(path).append("?");
            // 拼接请求值
            getParamMap.forEach((k, v) -> pathBuilder.append(k).append("=").append(v).append("&"));
            // 最后一个&
            int length = pathBuilder.length();
            pathBuilder.delete(length - 1, length);
            path = pathBuilder.toString();
        }
        if (monitor) {
            log.info("PATH [ {} ]", path);
        }
        return path;
    }

    /**
     * 构造http的URL和body
     *
     * @param path 请求路径
     */
    private void build(String path) {
        // 构造请求路径
        this.requestPath = generatePath(path);
        Object body = null;

        // 表单和body只能选中一个
        Assert.isTrue(!(formData != null && (postBodyStr != null || bodyParam != null)),
                "body or form data only one can be selected");

        // 没有指定contentType默认'application/json'
        MediaType contentType = httpHeaders.getContentType();
        if (Objects.isNull(formData)) {
            if (Objects.isNull(contentType)) {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            }
            body = Strings.isNullOrEmpty(postBodyStr) ? GsonUtils.object2Json(bodyParam) : postBodyStr;
        } else {
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            body = formData;
        }
        // body允许传空body
        this.httpEntity = new HttpEntity<>(body, httpHeaders);
        if (this.monitor) {
            log.info("requestEntity [ {} ]", httpEntity.toString());
        }
    }

    private <R> ResponseEntity<R> requestForEntity(String url, @Nullable Object request, Type responseType, HttpMethod httpMethod, Object... uriVariables) throws RestClientException {
        long begin = 0;
        if (monitor) {
            begin = System.currentTimeMillis();
        }
        RestTemplate restTemplate = getRestTemplate();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<R>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        ResponseEntity<R> responseEntity = nonNull(restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables));
        if (monitor) {
            log.info("Response Message [{}]", responseEntity);
            log.info("cost time [{}] ms.", System.currentTimeMillis() - begin);
        }
        return responseEntity;
    }

    private <T> T nonNull(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }

    /**
     * 获取restTemplate
     *
     * @return RestTemplate
     */
    private RestTemplate getRestTemplate() {
        if (!setTimeout) {
            return RestBuilder.REST_TEMPLATE;
        }
        synchronized (LOCK) {
            // 先去查看是否已经缓存了相同设置超时时间的restTemplate
            // 拼接规则为 ${readTimeout.toString()}:${connectTimeout.toString()}
            String cacheKey = generateCacheKey();
            RestTemplate timoutRestTemplate = CACHE.getIfPresent(cacheKey);
            if (Objects.nonNull(timoutRestTemplate)) {
                // 重置超时时间
                CACHE.put(cacheKey, timoutRestTemplate);
                return timoutRestTemplate;
            }
            // 之前没有缓存该restTemplate，生成好restTemplate，然后缓存起来
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            if (Objects.nonNull(this.readTimeout)) {
                requestFactory.setReadTimeout(readTimeout);
            }
            if (Objects.nonNull(this.connectTimeout)) {
                requestFactory.setConnectTimeout(connectTimeout);
            }
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            restTemplate.getMessageConverters().add(converter);
            CACHE.put(cacheKey, restTemplate);
            return restTemplate;
        }
    }

    private String generateCacheKey() {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(readTimeout)) {
            sb.append(readTimeout);
        }
        if (Objects.nonNull(connectTimeout)) {
            sb.append(connectTimeout);
        }
        return sb.toString();
    }
}

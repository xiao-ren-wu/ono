package org.ywb.ono.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.ywb.ono.toolkit.UriTree;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yuwenbo1
 * @version V1.0.0
 * @since 2020/6/11 8:57 下午
 */
@Getter
@Component
@ConfigurationProperties(prefix = "ono.web.request-monitor")
public class LogPrintProperties {

    @Resource
    private ServerProperties serverProperties;
    /**
     * 是否启用
     */
    @Setter
    private Boolean enable = false;
    /**
     * 是否打印请求信息
     */
    @Setter
    private Boolean printRequestMsg = true;
    /**
     * 是否打印响应信息
     */
    @Setter
    private Boolean printResponseBody = true;
    /**
     * uri列表类型
     */
    @Setter
    private LogType logType = LogType.exclude;
    /**
     * uriList
     */
    @Setter
    private List<String> uriList;
    /**
     * uriTree
     */
    private volatile UriTree uriTree;

    public UriTree getUriTree() {
        if (Objects.isNull(uriTree) && Objects.nonNull(uriList)) {
            synchronized (this) {
                if (Objects.isNull(uriTree)) {
                    String contextPath = serverProperties.getServlet().getContextPath();
                    String finalContextPath = contextPath == null ? "" : contextPath;
                    if (Objects.isNull(uriList) || uriList.isEmpty()) {
                        return null;
                    }
                    this.uriList = uriList.stream().map(a -> String.format("%s%s", finalContextPath, a)).collect(Collectors.toList());
                    this.uriTree = UriTree.create(this.uriList);
                }
            }
        }
        return uriTree;
    }

    public enum LogType {
        include,
        exclude
    }
}

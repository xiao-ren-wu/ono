package org.ywb.ono.web.toolkit;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpMethod;
import org.ywb.ono.toolkit.IoUtils;
import org.ywb.ono.toolkit.UriTree;
import org.ywb.ono.web.config.properties.LogPrintProperties;
import org.ywb.ono.web.holder.HttpContextHolder;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author yuwenbo
 * @date 2021/6/11 下午2:33
 * @since 1.0.0
 * 该filter可以打印前端请求过来的数据和返回给前端的数据（不支持restful请求）
 * 目前支持：
 * GET,
 * POST
 * - application/json
 * - application/form-data
 * - application/form-data-urlencoded
 * [注意]
 * 如果是post请求并且请求信息存放于body中，该filter会涉及流复制，会对性能有一定的影响
 * 使用时应该注意该filter的使用场景
 */
@Slf4j
public class RequestMonitorFilter implements Filter {

    @Resource
    private ServerProperties serverProperties;

    private volatile UriTree swaggerExcludeUriTree;

    private UriTree swaggerExcludeUriTree() throws IOException {
        if (Objects.isNull(swaggerExcludeUriTree)) {
            synchronized (RequestMonitorFilter.class) {
                if (Objects.isNull(swaggerExcludeUriTree)) {
                    String contextPath = serverProperties.getServlet().getContextPath();
                    contextPath = Strings.isNullOrEmpty(contextPath) ? "" : contextPath;
                    contextPath = contextPath.trim().endsWith("/") ? contextPath.substring(0, contextPath.length() - 1) : contextPath;
                    // 不打印swagger的请求
                    List<String> swaggerUriList = new LinkedList<>();
                    try (InputStream inputStream = RequestMonitorFilter.class.getClassLoader().getResourceAsStream("META-INF/swagger-api.path")) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String buf;
                        while (null != (buf = br.readLine())) {
                            swaggerUriList.add(String.join("", contextPath, buf));
                        }
                    }
                    swaggerExcludeUriTree = UriTree.create(swaggerUriList);
                }
            }
        }
        return swaggerExcludeUriTree;
    }

    @Resource
    private LogPrintProperties printProperties;


    private static final String BANNER = " ________________ \n" +
            "< LogPrintPlugin >\n" +
            " ---------------- \n" +
            "        \\   ^__^\n" +
            "         \\  (oo)\\_______\n" +
            "            (__)\\       )\\/\\\n" +
            "                 ||----w |\n" +
            "                 ||     ||\n";

    public RequestMonitorFilter() {
        System.err.println(BANNER);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        LogPrintProperties.LogType logType = printProperties.getLogType();
        UriTree uriTree = printProperties.getUriTree();
        String requestURI = httpServletRequest.getRequestURI();
        if (logType.equals(LogPrintProperties.LogType.include)) {
            if (Objects.isNull(uriTree) || !uriTree.contains(requestURI)) {
                chain.doFilter(request, response);
                return;
            }
        } else {
            if (Objects.nonNull(uriTree) && uriTree.contains(requestURI)) {
                chain.doFilter(request, response);
                return;
            }
        }
        if (swaggerExcludeUriTree().contains(requestURI)) {
            // 排除swagger路径
            chain.doFilter(request, response);
            return;
        }
        long begin = System.currentTimeMillis();
        if (printProperties.getPrintRequestMsg()) {
            log.info("RequestURI[ {} ]", httpServletRequest.getRequestURI());
            log.info("request method[ {} ]", httpServletRequest.getMethod());
            log.info("Content-Type[ {} ]", httpServletRequest.getHeader("Content-Type"));
            if (HttpMethod.POST.name().equals(httpServletRequest.getMethod())) {
                log.info("RequestBody");
                httpServletRequest = printPostMethodBody(httpServletRequest);
            } else {
                log.info("RequestParam");
                printGetMethodParams();
            }
        }
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
        log.info("-----------------------------");
        chain.doFilter(httpServletRequest, responseWrapper);
        String content = responseWrapper.getTextContent();
        if (printProperties.getPrintResponseBody()) {
            log.info("responseBody");
            log.info(content);
        }
        log.info("cost [ {} ] ms", System.currentTimeMillis() - begin);
        response.getOutputStream().write(content.getBytes());
    }

    private static class ResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        private PrintWriter printWriter = new PrintWriter(outputStream);

        ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return printWriter;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                }

                @Override
                public void write(int b) throws IOException {
                    outputStream.write(b);
                }
            };
        }

        void flush() {
            try {
                printWriter.flush();
                printWriter.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return outputStream;
        }

        String getTextContent() {
            flush();
            return outputStream.toString();
        }
    }

    /**
     * 获取get的参数
     */
    private void printGetMethodParams() {
        Map<String, String> requestParameters = HttpContextHolder.getRequestParameters();
        String getRequestParams = requestParameters.entrySet().stream()
                .map(a -> a.getKey() + "<-->" + a.getValue())
                .reduce((a, b) -> a + System.lineSeparator() + b)
                .orElse("no request param");
        log.info(System.lineSeparator() + getRequestParams);
    }

    /**
     * 获取post的body
     *
     * @param request HttpServletRequest
     */
    private HttpServletRequest printPostMethodBody(HttpServletRequest request) {
        if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
            printGetMethodParams();
            return request;
        }
        CustomerHttpServletRequestWrapper servletRequestWrapper = null;
        servletRequestWrapper = new CustomerHttpServletRequestWrapper(request);
        log.info(servletRequestWrapper.getBody());
        return servletRequestWrapper;
    }

    private static class CustomerHttpServletRequestWrapper extends HttpServletRequestWrapper {

        String body;

        HttpServletRequest httpServletRequest;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        CustomerHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.httpServletRequest = request;
            getBody();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(new CustomerHttpServletRequestWrapper.CustomServletInputStream(getBody())));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new CustomerHttpServletRequestWrapper.CustomServletInputStream(getBody());
        }

        String getBody() {
            try {
                if (Strings.isNullOrEmpty(body)) {
                    body = IoUtils.streamToString(this.httpServletRequest.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
        }

        private static class CustomServletInputStream extends ServletInputStream {

            private ByteArrayInputStream buffer;

            CustomServletInputStream(String body) {
                body = body == null ? "" : body;
                this.buffer = new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public int read() throws IOException {
                return buffer.read();
            }

            @Override
            public boolean isFinished() {
                return buffer.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new RuntimeException("Not implemented");
            }
        }
    }


}
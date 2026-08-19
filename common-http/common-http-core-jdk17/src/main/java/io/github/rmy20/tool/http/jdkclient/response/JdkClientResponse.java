package io.github.rmy20.tool.http.jdkclient.response;

import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.ClientHttpResponse;
import io.github.rmy20.tool.http.core.HttpHeaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JDK客户端响应
 *
 * @author sheng
 */
public class JdkClientResponse implements ClientHttpResponse {
    /**
     * 响应
     */
    private final HttpResponse<InputStream> httpResponse;

    /**
     * 响应头
     */
    private HttpHeaders headers;

    /**
     * 响应流
     */
    private final InputStream responseStream;

    /**
     * 创建#{@link JdkClientResponse}
     */
    public static JdkClientResponse create(HttpResponse<InputStream> httpResponse) {
        return new JdkClientResponse(httpResponse);
    }

    public JdkClientResponse(HttpResponse<InputStream> httpResponse) {
        this.httpResponse = Objects.requireNonNull(httpResponse, "httpResponse must not be null");
        this.responseStream = httpResponse.body();
    }

    @Override
    public int getStatus() {
        return this.httpResponse.statusCode();
    }

    @Override
    public String getMessage() {
        return String.valueOf(getStatus());
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.create();
            Map<String, List<String>> headerMap = httpResponse.headers().map();
            for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
                String name = entry.getKey();
                if (StringUtil.isBlank(name)) {
                    continue;
                }
                for (String value : entry.getValue()) {
                    headers.add(name, value);
                }
            }
            this.headers = headers;
        }
        return headers;
    }

    @Override
    public InputStream getBody() throws Exception {
        return this.responseStream;
    }

    @Override
    public void close() throws IOException {
        try {
            try {
                this.responseStream.transferTo(OutputStream.nullOutputStream());
            } finally {
                this.responseStream.close();
            }
        } catch (Throwable ignore) {
        }
    }
}

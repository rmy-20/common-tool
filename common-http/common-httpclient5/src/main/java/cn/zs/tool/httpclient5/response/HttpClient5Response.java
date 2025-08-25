package cn.zs.tool.httpclient5.response;

import cn.zs.tool.core.io.NullInputStream;
import cn.zs.tool.http.core.ClientHttpResponse;
import cn.zs.tool.http.core.HttpHeaders;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * httpclient5响应
 *
 * @author sheng
 */
public class HttpClient5Response implements ClientHttpResponse {
    /**
     * httpclient5响应
     */
    private final ClassicHttpResponse response;

    /**
     * 响应头
     */
    private HttpHeaders headers;

    /**
     * 创建#{@link HttpClient5Response}
     */
    public static HttpClient5Response create(ClassicHttpResponse response) {
        return new HttpClient5Response(response);
    }

    public HttpClient5Response(ClassicHttpResponse response) {
        this.response = Objects.requireNonNull(response, "httpResponse cannot be null");
    }

    @Override
    public int getStatus() {
        return response.getCode();
    }

    @Override
    public String getMessage() {
        return response.getReasonPhrase();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.create();
            for (Header header : response.getHeaders()) {
                headers.add(header.getName(), header.getValue());
            }
            this.headers = headers;
        }
        return headers;
    }

    @Override
    public InputStream getBody() throws Exception {
        HttpEntity entity = response.getEntity();
        return Objects.nonNull(entity) ? entity.getContent() : NullInputStream.create();
    }

    @Override
    public void close() throws IOException {
        try {
            try {
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } catch (IOException ignored) {
        }
    }
}

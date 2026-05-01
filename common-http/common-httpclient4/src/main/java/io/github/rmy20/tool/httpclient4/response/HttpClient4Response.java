package io.github.rmy20.tool.httpclient4.response;

import io.github.rmy20.tool.core.io.NullInputStream;
import io.github.rmy20.tool.http.core.ClientHttpResponse;
import io.github.rmy20.tool.http.core.HttpHeaders;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * httpclient4 响应
 *
 * @author sheng
 */
public class HttpClient4Response implements ClientHttpResponse {
    /**
     * httpclient4响应
     */
    private final HttpResponse response;

    /**
     * 响应头
     */
    private HttpHeaders headers;

    /**
     * 创建 {@link HttpClient4Response}
     */
    public static HttpClient4Response create(HttpResponse response) {
        return new HttpClient4Response(response);
    }

    public HttpClient4Response(HttpResponse response) {
        this.response = Objects.requireNonNull(response, "response must not be null");
    }

    @Override
    public int getStatus() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public String getMessage() {
        return response.getStatusLine().getReasonPhrase();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.create();
            for (Header header : response.getAllHeaders()) {
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
                HttpEntity entity = response.getEntity();
                if (Objects.nonNull(entity)) {
                    EntityUtils.consume(entity);
                }
            } finally {
                if (response instanceof Closeable) {
                    ((Closeable) response).close();
                }
            }
        } catch (Throwable ignored) {
        }
    }
}

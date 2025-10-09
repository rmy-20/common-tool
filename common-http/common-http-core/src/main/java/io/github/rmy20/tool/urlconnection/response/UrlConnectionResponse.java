package io.github.rmy20.tool.urlconnection.response;

import io.github.rmy20.tool.core.io.NullInputStream;
import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.ClientHttpResponse;
import io.github.rmy20.tool.http.core.HttpHeaders;
import io.github.rmy20.tool.http.core.exception.HttpException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Objects;

/**
 * url connection 响应
 *
 * @author sheng
 */
public class UrlConnectionResponse implements ClientHttpResponse {
    /**
     * url connection
     */
    private final HttpURLConnection connection;

    /**
     * 响应头
     */
    private HttpHeaders headers;

    /**
     * 响应流
     */
    private InputStream responseStream;

    /**
     * 创建#{@link UrlConnectionResponse}
     */
    public static UrlConnectionResponse create(HttpURLConnection connection) {
        return new UrlConnectionResponse(connection);
    }

    public UrlConnectionResponse(HttpURLConnection connection) {
        this.connection = Objects.requireNonNull(connection, "connection cannot be null");
    }

    @Override
    public int getStatus() {
        try {
            return connection.getResponseCode();
        } catch (Exception e) {
            throw new HttpException("HttpURLConnection获取响应码异常", e);
        }
    }

    @Override
    public String getMessage() {
        try {
            String message = connection.getResponseMessage();
            return Objects.nonNull(message) ? message : StringPool.EMPTY;
        } catch (Exception e) {
            throw new HttpException("HttpURLConnection获取响应信息异常", e);
        }
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.create();
            String name = connection.getHeaderFieldKey(0);
            if (StringUtil.isNotBlank(name)) {
                headers.add(name, connection.getHeaderField(0));
            }
            int i = 1;
            while (true) {
                name = connection.getHeaderFieldKey(i);
                if (!StringUtil.isNotBlank(name)) {
                    break;
                }
                headers.add(name, connection.getHeaderField(i));
                i++;
            }
            this.headers = headers;
        }
        return headers;
    }

    @Override
    public InputStream getBody() throws Exception {
        InputStream responseStream = this.responseStream;
        if (Objects.isNull(responseStream)) {
            if (isOk()) {
                responseStream = connection.getInputStream();
            }
            if (Objects.isNull(responseStream)) {
                InputStream errorStream = connection.getErrorStream();
                responseStream = Objects.nonNull(errorStream) ? errorStream : connection.getInputStream();
                responseStream = Objects.nonNull(responseStream) ? responseStream : NullInputStream.create();
            }
            this.responseStream = responseStream;
        }
        return responseStream;
    }

    @Override
    public void close() throws IOException {
        try {
            if (Objects.isNull(responseStream)) {
                getBody();
            }
            if (Objects.nonNull(responseStream)) {
                responseStream.close();
            }
        } catch (Exception ignore) {
        }
    }
}

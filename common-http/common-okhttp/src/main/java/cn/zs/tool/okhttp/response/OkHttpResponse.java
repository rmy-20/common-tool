package cn.zs.tool.okhttp.response;

import cn.zs.tool.http.core.ClientHttpResponse;
import cn.zs.tool.http.core.HttpHeaders;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * okhttp 响应
 *
 * @author sheng
 */
public class OkHttpResponse implements ClientHttpResponse {
    /**
     * 响应
     */
    private final Response response;

    /**
     * 响应头
     */
    private HttpHeaders headers;

    /**
     * 创建{@link OkHttpResponse}
     */
    public static OkHttpResponse create(Response response) {
        return new OkHttpResponse(response);
    }

    public OkHttpResponse(Response response) {
        this.response = Objects.requireNonNull(response, "response must not be null");
    }

    @Override
    public int getStatus() {
        return response.code();
    }

    @Override
    public String getMessage() {
        return response.message();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = this.headers;
        if (Objects.isNull(headers)) {
            headers = HttpHeaders.create();
            for (String headerName : response.headers().names()) {
                for (String headerValue : response.headers(headerName)) {
                    headers.add(headerName, headerValue);
                }
            }
            this.headers = headers;
        }
        return headers;
    }

    @Override
    public InputStream getBody() throws Exception {
        ResponseBody body = response.body();
        return body.byteStream();
    }

    @Override
    public void close() throws IOException {
        response.body().close();
    }
}

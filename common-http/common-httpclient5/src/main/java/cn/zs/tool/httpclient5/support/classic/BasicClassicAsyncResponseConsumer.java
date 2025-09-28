package cn.zs.tool.httpclient5.support.classic;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.AbstractAsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

/**
 * 响应结果处理
 *
 * @author sheng
 */
public class BasicClassicAsyncResponseConsumer extends AbstractAsyncResponseConsumer<BasicClassicHttpResponse, InputStream> {

    /**
     * 创建#{@link BasicClassicAsyncResponseConsumer}
     */
    public static BasicClassicAsyncResponseConsumer create() {
        return new BasicClassicAsyncResponseConsumer();
    }

    /**
     * 创建#{@link BasicClassicAsyncResponseConsumer}
     */
    public static BasicClassicAsyncResponseConsumer create(AsyncEntityConsumer<InputStream> dataConsumer) {
        return new BasicClassicAsyncResponseConsumer(dataConsumer);
    }

    public BasicClassicAsyncResponseConsumer() {
        this(FunctionalClassicEntityConsumer.create((contentType, inputStream) -> inputStream));
    }

    public BasicClassicAsyncResponseConsumer(AsyncEntityConsumer<InputStream> dataConsumer) {
        super(dataConsumer);
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
    }

    @Override
    protected BasicClassicHttpResponse buildResult(HttpResponse response, InputStream entity, ContentType contentType) {
        BasicClassicHttpResponse classicHttpResponse = new BasicClassicHttpResponse(response.getCode(), response.getReasonPhrase());
        classicHttpResponse.setEntity(new InputStreamEntity(entity, contentType));
        Header[] headerArr = response.getHeaders();
        if (Objects.nonNull(headerArr)) {
            for (Header header : headerArr) {
                classicHttpResponse.addHeader(header);
            }
        }
        classicHttpResponse.setVersion(response.getVersion());
        Locale locale = response.getLocale();
        if (Objects.nonNull(locale)) {
            classicHttpResponse.setLocale(locale);
        }
        return classicHttpResponse;
    }
}

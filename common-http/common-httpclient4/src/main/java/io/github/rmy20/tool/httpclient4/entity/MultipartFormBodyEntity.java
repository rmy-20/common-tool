package io.github.rmy20.tool.httpclient4.entity;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.body.MultipartFormBody;
import io.github.rmy20.tool.http.core.constant.HttpHeaderConstant;
import io.github.rmy20.tool.http.core.exception.HttpException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * multipart form
 *
 * @author sheng
 */
public class MultipartFormBodyEntity implements HttpEntity {
    private final MultipartFormBody formBody;

    /**
     * 创建{@link MultipartFormBodyEntity}
     */
    public static MultipartFormBodyEntity create(MultipartFormBody formBody) {
        return new MultipartFormBodyEntity(formBody);
    }

    public MultipartFormBodyEntity(MultipartFormBody formBody) {
        this.formBody = Objects.requireNonNull(formBody, "formBody must not be null");
    }

    @Override
    public boolean isRepeatable() {
        return formBody.repeatable();
    }

    @Override
    public boolean isChunked() {
        return !isRepeatable();
    }

    @Override
    public long getContentLength() {
        return formBody.contentLength();
    }

    @Override
    public Header getContentType() {
        MediaType contentType = formBody.getContentType();
        return new BasicHeader(HttpHeaderConstant.Content_Type, contentType.getMediaType());
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        if (getContentLength() > 25600L) {
            throw new HttpException("Content length is too long: " + getContentLength());
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.writeTo(stream);
        stream.flush();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        this.formBody.writeTo(outputStream);
    }

    @Override
    public boolean isStreaming() {
        return !isRepeatable();
    }

    @Override
    public void consumeContent() throws IOException {
    }
}

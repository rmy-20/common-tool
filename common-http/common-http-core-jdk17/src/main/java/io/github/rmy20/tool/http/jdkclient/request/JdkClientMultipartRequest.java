package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.core.constant.CommonConstant;
import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.body.MultipartFormBody;
import io.github.rmy20.tool.http.core.body.multipart.BaseMultipart;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;
import io.github.rmy20.tool.http.jdkclient.publisher.BoundedQueueOutputStreamPublisher;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * jdk httpclient multipart request
 *
 * @author sheng
 */
public class JdkClientMultipartRequest extends JdkClientBaseRequest<JdkClientMultipartRequest>
        implements BaseMultipartRequest<JdkClientMultipartRequest> {
    /**
     * 表单数据
     */
    private final MultipartFormBody formBody;

    /**
     * 发布订阅数据的线程池
     */
    private Executor executor;

    /**
     * 创建#{@link JdkClientMultipartRequest}
     */
    public static JdkClientMultipartRequest create(String url, HttpMethodEnum method) {
        return new JdkClientMultipartRequest(url, method);
    }

    /**
     * 创建#{@link JdkClientMultipartRequest}
     */
    public static JdkClientMultipartRequest create(String url, HttpMethodEnum method, MediaType contentType) {
        return new JdkClientMultipartRequest(url, method, contentType);
    }

    /**
     * 创建#{@link JdkClientMultipartRequest}
     */
    public static JdkClientMultipartRequest create(String url, HttpMethodEnum method, MultipartFormBody formBody) {
        return new JdkClientMultipartRequest(url, method, formBody);
    }

    public JdkClientMultipartRequest(String url, HttpMethodEnum method) {
        this(url, method, MultipartFormBody.create());
    }

    public JdkClientMultipartRequest(String url, HttpMethodEnum method, MediaType contentType) {
        this(url, method, MultipartFormBody.create(contentType));
    }

    public JdkClientMultipartRequest(String url, HttpMethodEnum method, MultipartFormBody formBody) {
        super(url, method);
        this.formBody = Objects.requireNonNull(formBody, "formBody must not be null");
    }

    public JdkClientMultipartRequest executor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public Executor getExecutor() {
        return Objects.nonNull(executor) ? executor : CommonConstant.EXECUTOR_SERVICE;
    }

    @Override
    public JdkClientMultipartRequest defaultCharset(Charset defaultCharset) {
        formBody.defaultCharset(defaultCharset);
        return super.defaultCharset(defaultCharset);
    }

    @Override
    public JdkClientMultipartRequest addText(String name, String value) {
        formBody.addText(name, value);
        return this;
    }

    /**
     * 添加文本
     *
     * @param name    key
     * @param value   value
     * @param charset 文本编码
     */
    public JdkClientMultipartRequest addText(String name, String value, Charset charset) {
        formBody.addText(name, value, charset);
        return this;
    }

    @Override
    public JdkClientMultipartRequest addBinary(String name, File file) {
        formBody.addBinary(name, file);
        return this;
    }

    @Override
    public JdkClientMultipartRequest addBinary(String name, String filename, File file) {
        formBody.addBinary(name, filename, file);
        return this;
    }

    @Override
    public JdkClientMultipartRequest addBinary(String name, String filename, byte[] bytes) {
        formBody.addBinary(name, filename, bytes);
        return this;
    }

    @Override
    public JdkClientMultipartRequest addBinary(String name, String filename, InputStream stream) {
        formBody.addBinary(name, filename, stream);
        return this;
    }

    /**
     * 添加 multipart part
     */
    public JdkClientMultipartRequest addPart(BaseMultipart<?> part) {
        formBody.addPart(part);
        return this;
    }

    @Override
    protected void executeBefore() {
        getHeaders().setContentType(formBody.getContentType());
        this.bodyPublisher = HttpRequest.BodyPublishers.fromPublisher(BoundedQueueOutputStreamPublisher.create(getExecutor(), formBody::writeTo));
    }

    @Override
    public JdkClientMultipartRequest self() {
        return this;
    }
}

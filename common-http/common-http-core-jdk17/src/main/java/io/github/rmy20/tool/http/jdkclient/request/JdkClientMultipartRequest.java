package io.github.rmy20.tool.http.jdkclient.request;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.body.MultipartFormBody;
import io.github.rmy20.tool.http.core.body.multipart.BaseMultipart;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;
import io.github.rmy20.tool.http.jdkclient.constant.JdkClientConstant;
import io.github.rmy20.tool.http.jdkclient.publisher.AbstractProducerPublisher;
import io.github.rmy20.tool.http.jdkclient.publisher.BoundedQueueOutputStreamPublisher;
import io.github.rmy20.tool.http.jdkclient.publisher.OutputStreamProducer;
import io.github.rmy20.tool.http.jdkclient.publisher.StrictDemandOutputStreamPublisher;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
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
     * 传入的
     */
    private AbstractProducerPublisher<ByteBuffer> producerPublisher;

    /**
     * 默认严格背压
     */
    private PublisherEnum publisherType = PublisherEnum.STRICT_DEMAND;

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
        return Objects.nonNull(executor) ? executor : JdkClientConstant.MULTIPART_WORK_EXECUTOR;
    }

    public JdkClientMultipartRequest producerPublisher(AbstractProducerPublisher<ByteBuffer> producerPublisher) {
        this.producerPublisher = producerPublisher;
        return this;
    }

    public JdkClientMultipartRequest publisherType(PublisherEnum publisherType) {
        this.publisherType = Objects.requireNonNull(publisherType, "publisherType must not be null");
        return this;
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
        AbstractProducerPublisher<ByteBuffer> producerPublisher = this.producerPublisher;
        if (Objects.isNull(producerPublisher)) {
            producerPublisher = publisherType.create(getExecutor(), formBody::writeTo);
        }
        this.bodyPublisher = HttpRequest.BodyPublishers.fromPublisher(producerPublisher);
    }

    @Override
    public JdkClientMultipartRequest self() {
        return this;
    }

    /**
     * 发布器构造类型
     */
    public enum PublisherEnum {
        /**
         * 有界队列
         */
        BOUNDED_QUEUE() {
            @Override
            protected AbstractProducerPublisher<ByteBuffer> create(Executor executor, OutputStreamProducer outputStreamProducer) {
                return BoundedQueueOutputStreamPublisher.create(executor, outputStreamProducer);
            }
        },

        /**
         * 严格背压
         */
        STRICT_DEMAND() {
            @Override
            protected AbstractProducerPublisher<ByteBuffer> create(Executor executor, OutputStreamProducer outputStreamProducer) {
                return StrictDemandOutputStreamPublisher.create(executor, outputStreamProducer);
            }
        },
        ;

        protected abstract AbstractProducerPublisher<ByteBuffer> create(Executor executor, OutputStreamProducer outputStreamProducer);
    }
}

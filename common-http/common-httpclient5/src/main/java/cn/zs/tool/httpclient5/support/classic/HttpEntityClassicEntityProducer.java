package cn.zs.tool.httpclient5.support.classic;

import cn.zs.tool.core.constant.CommonConstant;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.nio.support.classic.AbstractClassicEntityProducer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * #{@link HttpEntity}异步发送数据
 *
 * @author sheng
 */
public class HttpEntityClassicEntityProducer extends AbstractClassicEntityProducer {
    /**
     * #{@link HttpEntity}
     */
    private final HttpEntity entity;

    /**
     * 线程池
     */
    private final ExecutorService executorService;

    /**
     * 是否关闭传入的线程池
     */
    private final boolean shutdownExecutor;

    /**
     * 创建 #{@link HttpEntityClassicEntityProducer}
     *
     * @param entity #{@link HttpEntity}
     */
    public static HttpEntityClassicEntityProducer create(HttpEntity entity) {
        return new HttpEntityClassicEntityProducer(entity);
    }

    /**
     * 创建 #{@link HttpEntityClassicEntityProducer}
     *
     * @param initialBufferSize 初始缓冲区大小
     * @param entity            #{@link HttpEntity}
     * @param executorService   线程池
     * @param shutdownExecutor  是否关闭传入的线程池
     */
    public static HttpEntityClassicEntityProducer create(int initialBufferSize, HttpEntity entity,
                                                         ExecutorService executorService, boolean shutdownExecutor) {
        return new HttpEntityClassicEntityProducer(initialBufferSize, entity, executorService, shutdownExecutor);
    }

    public HttpEntityClassicEntityProducer(HttpEntity entity) {
        this(4096, entity, CommonConstant.EXECUTOR_SERVICE, false);
    }

    public HttpEntityClassicEntityProducer(int initialBufferSize, HttpEntity entity, ExecutorService executorService, boolean shutdownExecutor) {
        super(initialBufferSize, ContentType.parse(Objects.requireNonNull(entity, "entity must not be null").getContentType()), executorService);
        this.entity = entity;
        this.executorService = executorService;
        this.shutdownExecutor = shutdownExecutor;
    }

    @Override
    protected void produceData(ContentType contentType, OutputStream outputStream) throws IOException {
        entity.writeTo(outputStream);
    }

    @Override
    public void releaseResources() {
        super.releaseResources();
        if (shutdownExecutor && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

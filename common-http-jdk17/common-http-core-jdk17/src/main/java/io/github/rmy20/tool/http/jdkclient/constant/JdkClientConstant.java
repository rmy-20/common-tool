package io.github.rmy20.tool.http.jdkclient.constant;

import io.github.rmy20.tool.core.util.RandomUtil;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * jdk httpclient 常量
 *
 * @author sheng
 */
public class JdkClientConstant {
    /**
     * http客户端
     */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10L))
            .build();

    /**
     * multipart-upload-worker 线程池
     */
    public static final Executor MULTIPART_WORK_EXECUTOR = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "multipart-upload-worker-" + RandomUtil.generateSequenceUlid());
        t.setDaemon(true);
        return t;
    });
}

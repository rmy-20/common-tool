package io.github.rmy20.tool.http.core.request;

import io.github.rmy20.tool.http.core.decorator.HttpHeaderDecorator;
import io.github.rmy20.tool.http.core.decorator.UriBuilderDecorator;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.execute.BaseExecutorBuilder;
import io.github.rmy20.tool.http.core.result.HttpByteArrayResultHandle;
import io.github.rmy20.tool.http.core.result.HttpFileResultHandle;
import io.github.rmy20.tool.http.core.result.HttpJsonResultHandle;
import io.github.rmy20.tool.http.core.result.HttpOutputStreamResultHandle;
import io.github.rmy20.tool.http.core.result.HttpResultHandle;
import io.github.rmy20.tool.http.core.result.HttpStringResultHandle;
import io.github.rmy20.tool.http.core.result.HttpXmlResultHandle;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 请求基类
 *
 * @author sheng
 */
public interface BaseRequest<T extends BaseRequest<T>> extends UriBuilderDecorator<T>, HttpHeaderDecorator<T> {
    /**
     * 获取默认编码字符集
     */
    default Charset getDefaultCharset() {
        return StandardCharsets.UTF_8;
    }

    /**
     * 获取处理 UTF_8 {@link String}结果的请求执行器
     */
    default BaseExecutorBuilder<String, ? extends BaseExecutor<String>, ? extends BaseExecutor<String>, ?> stringExecutor() {
        return executor(HttpStringResultHandle.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    default BaseExecutorBuilder<String, ? extends BaseExecutor<String>, ? extends BaseExecutor<String>, ?> stringExecutor(Charset charset) {
        return executor(HttpStringResultHandle.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param resultHandle {@link HttpJsonResultHandle}
     */
    default <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> jsonExecutor(HttpJsonResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param resultHandle {@link HttpXmlResultHandle}
     */
    default <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> xmlExecutor(HttpXmlResultHandle<R> resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取获取 byte[] 结果的请求执行器
     */
    default BaseExecutorBuilder<byte[], ? extends BaseExecutor<byte[]>, ? extends BaseExecutor<byte[]>, ?> bytesExecutor() {
        return executor(HttpByteArrayResultHandle.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return 下载文件大小
     */
    default BaseExecutorBuilder<Long, ? extends BaseExecutor<Long>, ? extends BaseExecutor<Long>, ?> downloadExecutor(File targetFile) {
        return executor(HttpFileResultHandle.create(targetFile));
    }

    /**
     * 获取下载文件请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    default BaseExecutorBuilder<Long, ? extends BaseExecutor<Long>, ? extends BaseExecutor<Long>, ?> downloadExecutor(HttpFileResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param outputStream 输出流
     * @return 下载文件大小
     */
    default BaseExecutorBuilder<Long, ? extends BaseExecutor<Long>, ? extends BaseExecutor<Long>, ?> downloadExecutor(OutputStream outputStream) {
        return executor(HttpOutputStreamResultHandle.create(outputStream));
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param resultHandle 结果处理器
     * @return 下载文件大小
     */
    default BaseExecutorBuilder<Long, ? extends BaseExecutor<Long>, ? extends BaseExecutor<Long>, ?> downloadExecutor(HttpOutputStreamResultHandle resultHandle) {
        return executor(resultHandle);
    }

    /**
     * 获取请求执行器
     *
     * @param resultHandle 结果处理器
     */
    <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> executor(HttpResultHandle<R> resultHandle);
}

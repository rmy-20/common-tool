package cn.zs.tool.http.core.request;

import cn.zs.tool.http.core.converter.ByteArrayHttpMsgConverter;
import cn.zs.tool.http.core.converter.FileHttpMsgConverter;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.converter.JsonHttpMsgConverter;
import cn.zs.tool.http.core.converter.OutputStreamHttpMsgConverter;
import cn.zs.tool.http.core.converter.StringHttpMsgConverter;
import cn.zs.tool.http.core.converter.XmlHttpMsgConverter;
import cn.zs.tool.http.core.decorator.HttpHeaderDecorator;
import cn.zs.tool.http.core.decorator.UriBuilderDecorator;
import cn.zs.tool.http.core.execute.BaseExecutor;
import cn.zs.tool.http.core.execute.BaseExecutorBuilder;

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
        return executor(StringHttpMsgConverter.UTF_8_INSTANCE);
    }

    /**
     * 获取处理{@link String}结果的请求执行器
     *
     * @param charset 结果编码
     */
    default BaseExecutorBuilder<String, ? extends BaseExecutor<String>, ? extends BaseExecutor<String>, ?> stringExecutor(Charset charset) {
        return executor(StringHttpMsgConverter.create(charset));
    }

    /**
     * 获取处理 json 结果的请求执行器
     *
     * @param msgConverter {@link JsonHttpMsgConverter}
     */
    default <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> jsonExecutor(JsonHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取处理 xml 结果的请求执行器
     *
     * @param msgConverter {@link XmlHttpMsgConverter}
     */
    default <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> xmlExecutor(XmlHttpMsgConverter<R> msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取获取 byte[] 结果的请求执行器
     */
    default BaseExecutorBuilder<byte[], ? extends BaseExecutor<byte[]>, ? extends BaseExecutor<byte[]>, ?> bytesExecutor() {
        return executor(ByteArrayHttpMsgConverter.INSTANCE);
    }

    /**
     * 获取下载文件的请求执行器
     *
     * @param targetFile 目标文件
     * @return true 为成功
     */
    default BaseExecutorBuilder<Boolean, ? extends BaseExecutor<Boolean>, ? extends BaseExecutor<Boolean>, ?> downloadExecutor(File targetFile) {
        return executor(FileHttpMsgConverter.create(targetFile));
    }

    /**
     * 获取下载文件请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    default BaseExecutorBuilder<Boolean, ? extends BaseExecutor<Boolean>, ? extends BaseExecutor<Boolean>, ?> downloadExecutor(FileHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param outputStream 输出流
     * @return true 为成功
     */
    default BaseExecutorBuilder<Boolean, ? extends BaseExecutor<Boolean>, ? extends BaseExecutor<Boolean>, ?> downloadExecutor(OutputStream outputStream) {
        return executor(OutputStreamHttpMsgConverter.create(outputStream));
    }

    /**
     * 获取下载文件结果的请求执行器
     *
     * @param msgConverter 结果处理器
     * @return true 为成功
     */
    default BaseExecutorBuilder<Boolean, ? extends BaseExecutor<Boolean>, ? extends BaseExecutor<Boolean>, ?> downloadExecutor(OutputStreamHttpMsgConverter msgConverter) {
        return executor(msgConverter);
    }

    /**
     * 获取请求执行器
     *
     * @param msgConverter 结果处理器
     */
    <R> BaseExecutorBuilder<R, ? extends BaseExecutor<R>, ? extends BaseExecutor<R>, ?> executor(HttpMsgConverter<R> msgConverter);
}
